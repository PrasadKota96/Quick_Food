package uk.ac.tees.aad.w9316578.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.HashMap;

import uk.ac.tees.aad.w9316578.Activity.CustomerHomeActivity;
import uk.ac.tees.aad.w9316578.Activity.LandingActivity;
import uk.ac.tees.aad.w9316578.R;

import static android.content.Context.MODE_PRIVATE;


public class RiderRegisterFragment extends Fragment {

    EditText inputEmail, inputPassword,inputConfirmPassword;
    Button btnRegister;
    TextView alreadyHaveAnAccount;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;
    DatabaseReference mUserType;


    public RiderRegisterFragment() {
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
        View view= inflater.inflate(R.layout.fragment_rider_register, container, false);



        inputEmail = view.findViewById(R.id.inputEmail);
        inputPassword = view.findViewById(R.id.inputPassword);
        inputConfirmPassword = view.findViewById(R.id.inputPassword2);
        alreadyHaveAnAccount = view.findViewById(R.id.alreadyHaveAnAccount);
        btnRegister = view.findViewById(R.id.btnLogin);

        progressDialog=new ProgressDialog(getContext());
        mUserRef= FirebaseDatabase.getInstance().getReference().child("Rider");
        mUserType= FirebaseDatabase.getInstance().getReference().child("UserType");

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptRegister();
            }
        });

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LandingActivity.fragmentManager.beginTransaction().replace(R.id.conainer, new RiderLoginFragment(), "customer").commit();
            }
        });
        return view;
    }

    private void AttemptRegister() {

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confrimPassword = inputConfirmPassword.getText().toString();

        if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter Connext Email");
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter Proper Password");
        } else if (!password.equals(confrimPassword)) {
            inputConfirmPassword.setError("Password Not match Both field");
        } else {
            PerformAuthRegister(email,password);
        }

    }

    private void PerformAuthRegister(String email, String password) {
        progressDialog.setMessage("Please Wait While Registration...");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    AddSharePrefernce(email,password);
                    AdditionalUserInfoinDatabase();
                    sendUserToNextActivity();
                    Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AdditionalUserInfoinDatabase() {
        mUser=mAuth.getCurrentUser();
        HashMap hashMap=new HashMap();
        hashMap.put("userId",mUser.getUid());
        hashMap.put("username","No available");
        hashMap.put("address","No available");
        hashMap.put("address","No available");
        hashMap.put("profileImage","not vaialble");
        hashMap.put("userType", "rider");


        HashMap hashMap1=new HashMap();
        hashMap1.put("userId",mUser.getUid());
        hashMap1.put("userType","rider");



        mUserType.push().updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task task) {

            }
        });
        mUserRef.child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getContext(), "Registration Data Saved  ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void AddSharePrefernce(String email,String password) {
        SharedPreferences.Editor editor = getActivity().getApplicationContext().getSharedPreferences("User", MODE_PRIVATE).edit();
        editor.putString("userType", "rider");
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    private void sendUserToNextActivity () {
        Intent intent = new Intent(getContext(), CustomerHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}