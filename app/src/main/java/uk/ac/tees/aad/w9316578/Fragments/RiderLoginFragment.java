package uk.ac.tees.aad.w9316578.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.concurrent.Executor;

import uk.ac.tees.aad.w9316578.Activity.LandingActivity;
import uk.ac.tees.aad.w9316578.Activity.RiderHomeActivity;
import uk.ac.tees.aad.w9316578.Activity.RiderTrackActivity;
import uk.ac.tees.aad.w9316578.R;

import static android.content.Context.MODE_PRIVATE;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;


public class RiderLoginFragment extends Fragment {
    private static final int REQUEST_CODE = 3231;
    EditText inputEmail, inputPassword;
    TextView forgotPassword, createNewAccount;
    Button btnLogin;
    ImageView fingerprintImageView;
    ProgressDialog progressDialog;

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Executor executor;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserType;


    public RiderLoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_rider_login, container, false);

        inputEmail = view.findViewById(R.id.inputEmail);
        inputPassword = view.findViewById(R.id.inputPassword);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        createNewAccount = view.findViewById(R.id.createNewAccount);
        btnLogin = view.findViewById(R.id.btnLogin);
        fingerprintImageView = view.findViewById(R.id.fingerprintImageView);

        progressDialog = new ProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();
        mUserType= FirebaseDatabase.getInstance().getReference().child("UserType");

        CheckUserAlreadyLogin();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptLogin();
            }
        });

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LandingActivity.fragmentManager.beginTransaction().replace(R.id.conainer, new RiderRegisterFragment(), "customer2").commit();
            }
        });
        fingerprintImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformFingerPrintAuth();
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Forgot Password", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void PerformFingerPrintAuth() {
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("User", MODE_PRIVATE);
        String userType = prefs.getString("userType", "");
        String email = prefs.getString("email", "");
        String password = prefs.getString("password", "");


        BiometricManager biometricManager = BiometricManager.from(getContext());
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getContext(), "Fingerprint sensor Not exist", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getContext(), "Sensor not avail or busy", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
        }

        executor = ContextCompat.getMainExecutor(getContext());
        biometricPrompt = new BiometricPrompt(getActivity(),
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                if (email != null && password != null) {
                    PerformAuthLogin(email, password);
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Place your finger on sensor to login")
                .setNegativeButtonText("Use account password")
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

    private void CheckUserAlreadyLogin() {

        SharedPreferences prefs = getContext().getSharedPreferences("User", MODE_PRIVATE);
        String userType = prefs.getString("userType", "");

        if (userType.equals("rider")) {
            fingerprintImageView.setVisibility(View.VISIBLE);
        } else {
            fingerprintImageView.setVisibility(View.GONE);
        }

    }

    private void PerformAuthLogin(String email, String password) {

        progressDialog.setMessage("Please Wait While Login...");
        progressDialog.setTitle("Login");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    CheckisARider(email,password);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void AttemptLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if (!email.matches(emailPattern)) {
            inputEmail.setError("Enter Connext Email");
            inputEmail.requestFocus();
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter Proper Password");
        } else {
            PerformAuthLogin(email, password);
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(getContext(), RiderHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void AddSharePrefernce(String email, String password) {
        SharedPreferences.Editor editor = getActivity().getApplicationContext().getSharedPreferences("User", MODE_PRIVATE).edit();
        editor.putString("userType", "rider");
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    private void CheckisARider(String email, String password) {

        mUser = mAuth.getCurrentUser();
        mUserType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child("userId").getValue().toString().equals(mUser.getUid())) {
                        if (snapshot1.child("userType").getValue().toString().equals("rider")) {
                            progressDialog.dismiss();
                            sendUserToNextActivity();
                            AddSharePrefernce(email, password);
                            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }
                }

                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong ", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}