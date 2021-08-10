package uk.ac.tees.aad.w9316578.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewCustomerFoodAdapter;
import uk.ac.tees.aad.w9316578.Model.CartFood;
import uk.ac.tees.aad.w9316578.Model.Customer;
import uk.ac.tees.aad.w9316578.Model.Food;
import uk.ac.tees.aad.w9316578.R;
import uk.ac.tees.aad.w9316578.Services.MyService;
import uk.ac.tees.aad.w9316578.SqliteDatabase.DataBaseHelper;

public class CustomerHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton fab;
    DatabaseReference mFoodRef;
    List<Food> foodList;
    RecyclerviewCustomerFoodAdapter adminFoodAdapter;
    ImageView cartImageView;
    TextView counterNumberToolBar;
    DataBaseHelper helper;
    CircleImageView profileImage;
    TextView usernameTv;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserRef;



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
        drawerLayout = findViewById(R.id.drawaerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFoodRef = FirebaseDatabase.getInstance().getReference().child("Food");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Customer");
        foodList = new ArrayList<>();
        helper = new DataBaseHelper(this);
        counterNumberToolBar = findViewById(R.id.counterTextView);
        cartImageView = findViewById(R.id.cartImageView);
        counterNumberToolBar.setVisibility(View.GONE);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        LoadCartItems();
        LoadFood();
        StartService();

        cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHomeActivity.this, ViewCartActivity.class));
            }
        });
        View view=navigationView.inflateHeaderView(R.layout.drawer_header);
        usernameTv=view.findViewById(R.id.username);
        profileImage=view.findViewById(R.id.profile_image_haeder);


        LoadMyProfile();


    }

    private void StartService() {
        Intent intent=new Intent(CustomerHomeActivity.this,MyService.class);
        startService(intent);

    }

    private void LoadMyProfile() {
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Customer customer=snapshot.getValue(Customer.class);
                usernameTv.setText(""+customer.getUsername());
                Picasso.get().load(customer.getProfileImage()).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadCartItems();
    }

    private void LoadCartItems() {
        getAllCart();

        counterNumberToolBar.setText(getAllCart().size() + "");
        if (getAllCart().size() > 0) {
            counterNumberToolBar.setVisibility(View.VISIBLE);
        }
    }

    public ArrayList<CartFood> getAllCart() {
        ArrayList<CartFood> cartList = new ArrayList<CartFood>();
        // Select All Query
        String selectQuery = "SELECT  * FROM food";

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //countinue here to count total ietms and assign notification cart

                CartFood cartFood;
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                String foodId = cursor.getString(2);
                String foodName = cursor.getString(3);
                String foodPrice = cursor.getString(4);
                String foodDesc = cursor.getString(5);
                String foodItems = cursor.getString(6);
                String foodImageUri = cursor.getString(7);
                cartFood = new CartFood(id, date, foodId, foodName, foodPrice, foodDesc, foodImageUri, foodItems);
                cartList.add(cartFood);

            } while (cursor.moveToNext());
        }

        // return contact list
        return cartList;
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
        if (item.getItemId() == R.id.profile) {
            startActivity(new Intent(CustomerHomeActivity.this, CustomerProfileActivity.class));
        }
        if (item.getItemId() == R.id.myOrder) {
            startActivity(new Intent(CustomerHomeActivity.this, CustomerMyOrderActivity.class));
        }
        if (item.getItemId() == R.id.cart) {
            startActivity(new Intent(CustomerHomeActivity.this, ViewCartActivity.class));
        }
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(CustomerHomeActivity.this, LandingActivity.class));
            finish();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}