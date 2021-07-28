package uk.ac.tees.aad.w9316578.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import uk.ac.tees.aad.w9316578.Activity.AdminHomeActivity;
import uk.ac.tees.aad.w9316578.R;


public class BottomSheetAddFoodFragment extends BottomSheetDialogFragment {

    private static final int SELECT_PICTURE = 111;
    ImageView imageViewFood;
    EditText inputFoodName, inputFoodprice, inputDescription;
    Button btnSave;
    Uri imageUri;
    CardView card;
    TextView selectImageTextView;

    ProgressDialog progressDialog;
    DatabaseReference mFoodRef;
    StorageReference mFoodStorageRef;



    public BottomSheetAddFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_add_food, container, false);
        imageViewFood = view.findViewById(R.id.imageViewFood);
        inputFoodName = view.findViewById(R.id.inputFoodName);
        inputFoodprice = view.findViewById(R.id.inputFoodprice);
        inputDescription = view.findViewById(R.id.inputDescription);
        card = view.findViewById(R.id.card);
        selectImageTextView = view.findViewById(R.id.selectImageTextView);
        btnSave = view.findViewById(R.id.button);
        progressDialog=new ProgressDialog(getContext());

        mFoodRef= FirebaseDatabase.getInstance().getReference().child("Food");
        mFoodStorageRef= FirebaseStorage.getInstance().getReference().child("FoodImage");


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforAddItems();
            }
        });

        imageViewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getImageFromGallery();
            }
        });
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromGallery();
            }
        });
        selectImageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromGallery();
            }
        });

        return view;
    }

    private void PerforAddItems() {


        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(date);

        String foodName=inputFoodName.getText().toString();
        String foodPrice=inputFoodprice.getText().toString();
        String foodDesc=inputDescription.getText().toString();


        if (foodName.isEmpty()) {
            inputFoodName.setError("Enter Food Name");
            inputFoodName.requestFocus();
        } else if (foodPrice.isEmpty() ) {
            inputFoodprice.setError("Enter Price");
            inputFoodprice.requestFocus();
        }else if (foodDesc.isEmpty())
        {
            inputDescription.setError("Enter Price");
            inputDescription.requestFocus();
        }else if (imageUri==null)
        {
            Toast.makeText(getContext(), "Select Food Image", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setMessage("Adding Food");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            String pushKey=mFoodRef.push().getKey().toString();
            mFoodStorageRef.child(pushKey).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                    mFoodStorageRef.child(pushKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            HashMap hashMap=new HashMap();
                            hashMap.put("date",strDate);
                            hashMap.put("foodId",pushKey);
                            hashMap.put("foodName",foodName);
                            hashMap.put("foodPrice",foodPrice);
                            hashMap.put("foodDesc",foodDesc);
                            hashMap.put("foodImageUri",uri.toString());

                            mFoodRef.child(pushKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task task) {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Food Added", Toast.LENGTH_SHORT).show();
                                        AdminHomeActivity.btsf.dismiss();
                                    }else
                                    {
                                        progressDialog.dismiss();
                                        AdminHomeActivity.btsf.dismiss();
                                        Toast.makeText(getContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });

        }
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE) {
            Toast.makeText(getContext(), "dfsdgd", Toast.LENGTH_SHORT).show();
            imageUri = data.getData();
            imageViewFood.setImageURI(imageUri);
            imageViewFood.setVisibility(View.VISIBLE);

        }
    }
}