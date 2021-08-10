package uk.ac.tees.aad.w9316578.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewViewCartAdapter;
import uk.ac.tees.aad.w9316578.Model.CartFood;
import uk.ac.tees.aad.w9316578.Model.Customer;
import uk.ac.tees.aad.w9316578.R;
import uk.ac.tees.aad.w9316578.SqliteDatabase.DataBaseHelper;

public class ViewCartActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    DataBaseHelper helper;
    RecyclerviewViewCartAdapter adapter;
    TextView totalPriceTv, totalPricePlusAllTaxTv;
    Button btnCheckout;
    double totalAmount = 0;
    double totalAmountWithTax = 0;
    DatabaseReference mOrderRef, mOrderInfo, mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;
    Customer customer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Checkout");

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        helper = new DataBaseHelper(this);
        adapter = new RecyclerviewViewCartAdapter(getAllCart(), this);
        recyclerView.setAdapter(adapter);

        totalPriceTv = findViewById(R.id.totalPrice);
        totalPricePlusAllTaxTv = findViewById(R.id.totalPricePlusAllTax);
        btnCheckout = findViewById(R.id.checkout);
        progressDialog = new ProgressDialog(this);
        UpdatePrices();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mOrderRef = FirebaseDatabase.getInstance().getReference().child("OrderFoodItems");
        mOrderInfo = FirebaseDatabase.getInstance().getReference().child("OrderInfo");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Customer");

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOut();
            }
        });
        LoadMyProfile();


    }

    private void LoadMyProfile() {
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    customer = snapshot.getValue(Customer.class);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void checkOut() {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        ArrayList<CartFood> list = getAllCart();

        String orderID = mOrderRef.push().getKey().toString();
        if (list.size() > 0) {

            if (customer.getAddress().equals("not available")) {
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Please Update Your Profile and then Checkout Orders");
                builder.setPositiveButton("Go to Profile", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ViewCartActivity.this,CustomerProfileActivity.class));
                    }
                }).setNegativeButton("Cancel",null);
                builder.create();
                builder.show();

            } else {
                progressDialog.setMessage("Please wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                for (int i = 0; i < list.size(); i++) {
                    mOrderRef.child(mUser.getUid()).child(orderID).push().setValue(list.get(i));
                }
                HashMap hashMap = new HashMap();
                hashMap.put("totalAmount", totalAmountWithTax + "");
                hashMap.put("orderID", orderID);
                hashMap.put("status", "pending");
                hashMap.put("dateOrder", strDate);
                hashMap.put("userID", mUser.getUid());
                mOrderInfo.child(orderID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task task) {
                        if (task.isSuccessful()) {
                            helper.removeAllItem();
                            Toast.makeText(ViewCartActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ViewCartActivity.this, CustomerHomeActivity.class));
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ViewCartActivity.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            progressDialog.dismiss();
        }


    }

    private void UpdatePrices() {
        ArrayList<CartFood> list = getAllCart();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                totalAmount = totalAmount + (Double.parseDouble(list.get(i).getFoodPrice())) * (Double.parseDouble(list.get(i).getFoodItems()));
            }

            totalAmountWithTax = totalAmount + 1 + 1;
            totalPriceTv.setText("£" + totalAmount);
            totalPricePlusAllTaxTv.setText("£" + totalAmountWithTax);
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
}