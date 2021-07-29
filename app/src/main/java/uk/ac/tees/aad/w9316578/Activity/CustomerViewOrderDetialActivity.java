package uk.ac.tees.aad.w9316578.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewViewCartAdapter;
import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewViewCustomerPlacedOrderAdapter;
import uk.ac.tees.aad.w9316578.Model.CartFood;
import uk.ac.tees.aad.w9316578.Model.Customer;
import uk.ac.tees.aad.w9316578.Model.OrderInfo;
import uk.ac.tees.aad.w9316578.R;
import uk.ac.tees.aad.w9316578.SqliteDatabase.DataBaseHelper;

public class CustomerViewOrderDetialActivity extends AppCompatActivity {

    DatabaseReference mOrderRef, mOrderInfo, mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<CartFood> orderDetailList;
    RecyclerviewViewCartAdapter adapter;
    OrderInfo orderInfo;


    TextView totalPriceTv, totalPricePlusAllTaxTv;
    Button btnCancelOrder;
    double totalAmount = 0;
    double totalAmountWithTax = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_order_detial);

        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("My Orders");

        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");

        orderDetailList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        btnCancelOrder = findViewById(R.id.checkout);
        totalPriceTv = findViewById(R.id.totalPrice);
        totalPricePlusAllTaxTv = findViewById(R.id.totalPricePlusAllTax);

        mUser = mAuth.getCurrentUser();
        mOrderRef = FirebaseDatabase.getInstance().getReference().child("OrderFoodItems");
        mOrderInfo = FirebaseDatabase.getInstance().getReference().child("OrderInfo");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Customer");

        LoadOrderDetail();

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderRef.child(mUser.getUid()).child(orderInfo.getOrderID()).removeValue();
                mOrderInfo.child(mUser.getUid()).child(orderInfo.getOrderID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            finish();
                            Toast.makeText(CustomerViewOrderDetialActivity.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void LoadOrderDetail() {
        mOrderRef.child(mUser.getUid()).child(orderInfo.getOrderID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                orderDetailList = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    CartFood orderInfo = snapshot1.getValue(CartFood.class);
                    orderDetailList.add(orderInfo);
                }
                adapter = new RecyclerviewViewCartAdapter(orderDetailList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                UpdatePrices();
                if (orderInfo.getStatus().equals("pending")) {
                    btnCancelOrder.setEnabled(true);

                } else {
                    btnCancelOrder.setEnabled(false);
                    btnCancelOrder.setText("Cant Cancel this Order");
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void UpdatePrices() {

        if (orderDetailList.size() > 0) {
            for (int i = 0; i < orderDetailList.size(); i++) {
                totalAmount = totalAmount + (Double.parseDouble(orderDetailList.get(i).getFoodPrice())) * (Double.parseDouble(orderDetailList.get(i).getFoodItems()));
            }

            totalAmountWithTax = totalAmount + 1 + 1;
            totalPriceTv.setText("$" + totalAmount);
            totalPricePlusAllTaxTv.setText("$" + totalAmountWithTax);
        }
    }
}