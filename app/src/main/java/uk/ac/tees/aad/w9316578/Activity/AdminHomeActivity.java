package uk.ac.tees.aad.w9316578.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewAdminFoodAdapter;
import uk.ac.tees.aad.w9316578.Fragments.BottomSheetAddFoodFragment;
import uk.ac.tees.aad.w9316578.Model.Food;
import uk.ac.tees.aad.w9316578.R;

public class AdminHomeActivity extends AppCompatActivity {
    FloatingActionButton fab;
    public static BottomSheetAddFoodFragment btsf;
    RecyclerView recyclerView;
    Toolbar toolbar;
    DatabaseReference mFoodRef;
    List<Food> foodList;
    RecyclerviewAdminFoodAdapter adminFoodAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Admin");
        fab = findViewById(R.id.fab);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFoodRef = FirebaseDatabase.getInstance().getReference().child("Food");

        foodList = new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btsf = new BottomSheetAddFoodFragment();
                btsf.show(getSupportFragmentManager(), btsf.getTag());
            }
        });

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

                adminFoodAdapter = new RecyclerviewAdminFoodAdapter(foodList, AdminHomeActivity.this);
                recyclerView.setAdapter(adminFoodAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addFood) {
            btsf = new BottomSheetAddFoodFragment();
            btsf.show(getSupportFragmentManager(), btsf.getTag());
        } else if (item.getItemId() == R.id.logut) {
            startActivity(new Intent(AdminHomeActivity.this, LandingActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}