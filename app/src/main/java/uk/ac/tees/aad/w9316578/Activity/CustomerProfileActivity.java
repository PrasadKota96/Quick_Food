package uk.ac.tees.aad.w9316578.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.aad.w9316578.Model.Customer;
import uk.ac.tees.aad.w9316578.R;

public class CustomerProfileActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 10101;
    private static final int IMAGE_PICKER_SELECT = 101011;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;
    Customer customer;
    CircleImageView profileImage;
    EditText username, phone, address;
    Uri imageUri;
    ProgressDialog progressDialog;

    StorageReference mStoragePorofileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);


        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileImage = findViewById(R.id.image);
        username = findViewById(R.id.fullname);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Customer");
        mStoragePorofileImage = FirebaseStorage.getInstance().getReference().child("ProfileImages");


        LoadProfileData();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageFromGallery();
            }
        });


    }

    private void SelectImageFromGallery() {
        //Android device need permission from user ,can app use camera sensor or not
        if (ContextCompat.checkSelfPermission(CustomerProfileActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(CustomerProfileActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            //if permission not allowd then request permision
            ActivityCompat.requestPermissions(CustomerProfileActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        } else {

            //otherwise open camera to take image
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, "New Pic");
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Front Camera Pic");
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, IMAGE_PICKER_SELECT);
        }
    }

    private void LoadProfileData() {
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                customer = snapshot.getValue(Customer.class);
                Picasso.get().load(customer.getProfileImage()).placeholder(R.drawable.user).into(profileImage);
                username.setText(customer.getUsername());
                phone.setText(customer.getPhone());
                address.setText(customer.getAddress());

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            SaveProfile();
        }
        return super.onOptionsItemSelected(item);
    }

    //saved user infor method
    private void SaveProfile() {
        final String mUsername = username.getText().toString();
        final String mAddress = address.getText().toString();
        final String mPhone = phone.getText().toString();


        //show error if user did not typed anything in input field
        if (mUsername.isEmpty() || mUsername.length() < 3) {
            username.setError("Select Correct Username with min 4 letter");
        } else if (mAddress.isEmpty() || mAddress.length() < 3) {
            address.setError("Select Correct Address with min 4 letter");
        } else if (mPhone.isEmpty() || mPhone.length() < 3) {
            phone.setError("Select Proper Phone format");
        } else if (imageUri == null) {
            Toast.makeText(this, "Please Select  Profile Image", Toast.LENGTH_SHORT).show();
        } else {

            //if user put valid info in input field then he can Saved data
            progressDialog.setTitle("Saving Profile");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mStoragePorofileImage.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        mStoragePorofileImage.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("username", mUsername);
                                hashMap.put("address", mAddress);
                                hashMap.put("phone", mPhone);
                                hashMap.put("profileImage", uri.toString());

                                mUserRef.child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(CustomerProfileActivity.this, "Profile Updates", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(CustomerProfileActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }else {
                        Toast.makeText(CustomerProfileActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //check Either user allowed permission or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICKER_SELECT) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(CustomerProfileActivity.this.getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}