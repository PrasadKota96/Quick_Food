package uk.ac.tees.aad.w9316578.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uk.ac.tees.aad.w9316578.Fragments.AdminLoginFragment;
import uk.ac.tees.aad.w9316578.Fragments.CustomerLoginFragment;
import uk.ac.tees.aad.w9316578.Fragments.RiderLoginFragment;
import uk.ac.tees.aad.w9316578.R;

public class LandingActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_landing);
        fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottomNv);


        if (findViewById(R.id.conainer) != null) {
            if (savedInstanceState != null) {
                return;
            }
            fragmentManager.beginTransaction().add(R.id.conainer, new CustomerLoginFragment(), "admin").commit();
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
                if (item.getItemId() == R.id.admin) {
                    fragmentManager.beginTransaction().replace(R.id.conainer, new AdminLoginFragment(), "admin").commit();

                } else if (item.getItemId() == R.id.customer) {
                    fragmentManager.beginTransaction().replace(R.id.conainer, new CustomerLoginFragment(), "customer").commit();
                }else if (item.getItemId() == R.id.rider) {
                    fragmentManager.beginTransaction().replace(R.id.conainer, new RiderLoginFragment(), "rider").commit();
                }
                return false;
            }
        });
    }
}