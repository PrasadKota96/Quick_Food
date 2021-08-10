package uk.ac.tees.aad.w9316578.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.List;

import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewViewCartAdapter;
import uk.ac.tees.aad.w9316578.Model.CartFood;
import uk.ac.tees.aad.w9316578.Model.Customer;
import uk.ac.tees.aad.w9316578.Model.OrderInfo;
import uk.ac.tees.aad.w9316578.Model.OrderInfoForRider;
import uk.ac.tees.aad.w9316578.R;

public class RiderViewOrderDetialActivity extends AppCompatActivity {

    DatabaseReference mOrderRef, mOrderInfo, mUserRef, mRiderPickedOrder, orderNotification;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Toolbar toolbar;

    RecyclerView recyclerView;
    List<CartFood> orderDetailList;
    RecyclerviewViewCartAdapter adapter;
    OrderInfoForRider orderInfoForRider;


    TextView totalPriceTv, totalPricePlusAllTaxTv;
    Button btnSendOrder, btnViewRoute;
    double totalAmount = 0;
    double totalAmountWithTax = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_view_order_detial);

        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("My Orders");

        orderInfoForRider = (OrderInfoForRider) getIntent().getSerializableExtra("orderInfo");

        orderDetailList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        btnSendOrder = findViewById(R.id.checkout);
        btnViewRoute = findViewById(R.id.viewRoute);
        totalPriceTv = findViewById(R.id.totalPrice);
        totalPricePlusAllTaxTv = findViewById(R.id.totalPricePlusAllTax);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mOrderRef = FirebaseDatabase.getInstance().getReference().child("OrderFoodItems");
        mOrderInfo = FirebaseDatabase.getInstance().getReference().child("OrderInfo");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Customer");
        mRiderPickedOrder = FirebaseDatabase.getInstance().getReference().child("RiderSentOrders");
        orderNotification = FirebaseDatabase.getInstance().getReference().child("OrderNotification");

        if (orderInfoForRider.getStatus().equals("Completed")) {
            btnSendOrder.setVisibility(View.GONE);
        }
        LoadOrderDetail();

        btnViewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserRef.child(orderInfoForRider.getUserID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                        Customer customer = snapshot.getValue(Customer.class);
                        ShowDialog(customer);
                    }

                    @Override
                    public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                    }
                });
            }
        });
        btnSendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap hashMap = new HashMap();
                hashMap.put("status", "Completed");
                mOrderInfo.child(orderInfoForRider.getOrderInfoID()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task task) {
                        if (task.isSuccessful()) {

                            SendNotification();

                            btnSendOrder.setText("Order Status Changed");
                            btnSendOrder.setEnabled(false);
                            HashMap hashMap1 = new HashMap();
                            hashMap1.put("dateOrder", orderInfoForRider.getDateOrder());
                            hashMap1.put("orderID", orderInfoForRider.getOrderID());
                            hashMap1.put("status", "Completed");
                            hashMap1.put("totalAmount", orderInfoForRider.getTotalAmount());
                            hashMap1.put("userID", orderInfoForRider.getUserID());
                            hashMap1.put("OrderInfoID", orderInfoForRider.getOrderInfoID());
                            hashMap1.put("riderID", mUser.getUid());

                            mRiderPickedOrder.push().updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RiderViewOrderDetialActivity.this, "Order Picked", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RiderViewOrderDetialActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(RiderViewOrderDetialActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void SendNotification() {

        HashMap hashMap2 = new HashMap();
        hashMap2.put("dateOrder", orderInfoForRider.getDateOrder());
        hashMap2.put("orderID", orderInfoForRider.getOrderID());
        hashMap2.put("status", "Completed");
        hashMap2.put("totalAmount", orderInfoForRider.getTotalAmount());
        hashMap2.put("userID", orderInfoForRider.getUserID());
        hashMap2.put("OrderInfoID", orderInfoForRider.getOrderInfoID());
        hashMap2.put("notificationStatus", "unseen");
        hashMap2.put("riderID", mUser.getUid());

        orderNotification.child(orderInfoForRider.getUserID()).child(orderInfoForRider.getOrderID()).updateChildren(hashMap2).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task task) {
                if (task.isSuccessful()) {
                } else {
                    Toast.makeText(RiderViewOrderDetialActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void ShowDialog(Customer customer) {
        Dialog builder = new Dialog(this);
        builder.setContentView(R.layout.dialog);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView imageViewClose = builder.findViewById(R.id.imageViewClose);
        Button btnMap = builder.findViewById(R.id.btnMap);
        Button btnCancel = builder.findViewById(R.id.btnCancel);
        TextView username = builder.findViewById(R.id.username);
        TextView address = builder.findViewById(R.id.username2);

        username.setText(customer.getUsername());
        address.setText(customer.getAddress() +"\n" +"Phone #" +customer.getPhone());

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RiderViewOrderDetialActivity.this,RiderTrackActivity.class);
                intent.putExtra("customer",customer);
                startActivity(intent);
            }
        });


        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();

            }
        });
        builder.create();
        builder.show();


    }

    private void LoadOrderDetail() {
        mOrderRef.child(orderInfoForRider.getUserID()).child(orderInfoForRider.getOrderID()).addValueEventListener(new ValueEventListener() {
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
            totalPriceTv.setText("£" + totalAmount);
            totalPricePlusAllTaxTv.setText("£" + totalAmountWithTax);
        }
    }
}