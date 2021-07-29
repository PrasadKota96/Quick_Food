package uk.ac.tees.aad.w9316578.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewAdminFoodAdapter;
import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewCustomerFoodAdapter;
import uk.ac.tees.aad.w9316578.Fragments.BottomSheetAddFoodFragment;
import uk.ac.tees.aad.w9316578.Model.Food;
import uk.ac.tees.aad.w9316578.R;

public class CustomerHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton fab;
    public static BottomSheetAddFoodFragment btsf;
    DatabaseReference mFoodRef;
    List<Food> foodList;
    RecyclerviewCustomerFoodAdapter adminFoodAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);



        toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        fab = findViewById(R.id.fab);
        drawerLayout=findViewById(R.id.drawaerLayout);
        navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFoodRef = FirebaseDatabase.getInstance().getReference().child("Food");
        foodList = new ArrayList<>();

        LoadFood();

    }


    private void LoadFood() {
        mFoodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                foodList = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Food food = snapshot1.getValue(Food.class);
                    foodList.add(food);
                }

                adminFoodAdapter = new RecyclerviewCustomerFoodAdapter(foodList, CustomerHomeActivity.this);
                recyclerView.setAdapter(adminFoodAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId()==R.id.profile)
        {
            startActivity(new Intent(CustomerHomeActivity.this,CustomerProfileActivity.class));
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}