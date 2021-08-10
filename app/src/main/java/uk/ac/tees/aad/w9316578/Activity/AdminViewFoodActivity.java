package uk.ac.tees.aad.w9316578.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import uk.ac.tees.aad.w9316578.Model.Food;
import uk.ac.tees.aad.w9316578.R;

public class AdminViewFoodActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 111;
    ImageView imageViewFood;
    TextView foodName, foodPrice, foodDesc;
    Button btnEdit, btnDelete;
    EditText inputFoodName, inputFoodPrice, inputFoodDesc;
    Toolbar toolbar;
    Food food;
    boolean isInnEditMode = false;
    ProgressDialog progressDialog;
    DatabaseReference mFoodRef;
    StorageReference mFoodStorageRef;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_food);
        food = (Food) getIntent().getSerializableExtra("food");

        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Admin");

        mFoodRef = FirebaseDatabase.getInstance().getReference().child("Food");
        mFoodStorageRef = FirebaseStorage.getInstance().getReference().child("FoodImage");

        imageViewFood = findViewById(R.id.imageViewFood);
        foodName = findViewById(R.id.foodName);
        foodPrice = findViewById(R.id.foodPrice);
        foodDesc = findViewById(R.id.foodDesc);
        btnEdit = findViewById(R.id.btnEditUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        inputFoodName = findViewById(R.id.inputFoodName);
        inputFoodPrice = findViewById(R.id.inputFoodPrice);
        inputFoodDesc = findViewById(R.id.inputFoodDesc);

        progressDialog = new ProgressDialog(this);

        AssignData();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInnEditMode) {
                    UpdateFoodItem();
                } else {
                    ChangeToEditMode();
                }
            }
        });

        imageViewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInnEditMode) {
                    getImageFromGallery();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInnEditMode) {
                    ChangeToNormalMode();
                }else
                    DeleteFood();
            }
        });


    }

    private void DeleteFood() {
        progressDialog.setMessage("Deleting Food");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mFoodRef.child(food.getFoodId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(AdminViewFoodActivity.this, "Food Deleted", Toast.LENGTH_SHORT).show();
                }else
                {
                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(AdminViewFoodActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

    }

    private void UpdateFoodItem() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);

        String foodName = inputFoodName.getText().toString();
        String foodPrice = inputFoodPrice.getText().toString();
        String foodDesc = inputFoodDesc.getText().toString();


        if (foodName.isEmpty()) {
            inputFoodName.setError("Enter Food Name");
            inputFoodName.requestFocus();
        } else if (foodPrice.isEmpty()) {
            inputFoodPrice.setError("Enter Price");
            inputFoodPrice.requestFocus();
        } else if (foodDesc.isEmpty()) {
            inputFoodDesc.setError("Enter Price");
            inputFoodDesc.requestFocus();
        } else if (imageUri == null) {

            progressDialog.setMessage("Updating Food");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            HashMap hashMap = new HashMap();
            hashMap.put("date", strDate);
            hashMap.put("foodId", food.getFoodId());
            hashMap.put("foodName", foodName);
            hashMap.put("foodPrice", foodPrice);
            hashMap.put("foodDesc", foodDesc);


            mFoodRef.child(food.getFoodId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull @NotNull Task task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        finish();
                        Toast.makeText(AdminViewFoodActivity.this, "Food Update", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(AdminViewFoodActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            progressDialog.setMessage("Updating Food");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            mFoodStorageRef.child(food.getFoodId()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    mFoodStorageRef.child(food.getFoodId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("date", strDate);
                            hashMap.put("foodId", food.getFoodId());
                            hashMap.put("foodName", foodName);
                            hashMap.put("foodPrice", foodPrice);
                            hashMap.put("foodDesc", foodDesc);
                            hashMap.put("foodImageUri", uri.toString());

                            mFoodRef.child(food.getFoodId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        finish();
                                        Toast.makeText(AdminViewFoodActivity.this, "Food Update", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(AdminViewFoodActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void AssignData() {
        foodName.setText(food.getFoodName());
        foodPrice.setText("£ " + food.getFoodPrice());
        foodDesc.setText(food.getFoodDesc());
        Picasso.get().load(food.getFoodImageUri()).into(imageViewFood);


        inputFoodName.setText(food.getFoodName());
        inputFoodPrice.setText(food.getFoodPrice());
        inputFoodDesc.setText(food.getFoodDesc());

    }

    private void ChangeToEditMode() {
        foodName.setVisibility(View.GONE);
        inputFoodName.setVisibility(View.VISIBLE);

        foodPrice.setVisibility(View.GONE);
        inputFoodPrice.setVisibility(View.VISIBLE);

        foodDesc.setVisibility(View.GONE);
        inputFoodDesc.setVisibility(View.VISIBLE);
        isInnEditMode = true;
        btnEdit.setText("Update");
        btnDelete.setText("Cancel");
    }

    private void ChangeToNormalMode() {
        foodName.setVisibility(View.VISIBLE);
        inputFoodName.setVisibility(View.GONE);

        foodPrice.setVisibility(View.VISIBLE);
        inputFoodPrice.setVisibility(View.GONE);

        foodDesc.setVisibility(View.VISIBLE);
        inputFoodDesc.setVisibility(View.GONE);
        isInnEditMode = false;
        btnEdit.setText("Edit Food Item");
        btnDelete.setText("Delete");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE) {

            imageUri = data.getData();
            imageViewFood.setImageURI(imageUri);
            imageViewFood.setVisibility(View.VISIBLE);

        }
    }
}