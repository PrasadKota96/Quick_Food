package uk.ac.tees.aad.w9316578.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewViewRiderAvailabaleOrderAdapter;
import uk.ac.tees.aad.w9316578.Model.OrderInfo;
import uk.ac.tees.aad.w9316578.Model.OrderInfoForRider;
import uk.ac.tees.aad.w9316578.R;

public class AdminAvailableOrderActivity extends AppCompatActivity {


    DatabaseReference mOrderRef, mOrderInfo, mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<OrderInfoForRider> orderInfoList;
    RecyclerviewViewRiderAvailabaleOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_available_order);

        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Available Orders");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        orderInfoList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mOrderRef = FirebaseDatabase.getInstance().getReference().child("OrderFoodItems");
        mOrderInfo = FirebaseDatabase.getInstance().getReference().child("OrderInfo");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Customer");

        LaodOrder();
    }
    private void LaodOrder() {
        mOrderInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                orderInfoList = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child("status").getValue().toString().equals("pending")) {
                        OrderInfo orderInfo = snapshot1.getValue(OrderInfo.class);
                        orderInfoList.add(new OrderInfoForRider(orderInfo.getDateOrder(), orderInfo.getOrderID(), orderInfo.getStatus(), orderInfo.getTotalAmount(), orderInfo.getUserID(), snapshot1.getRef().getKey().toString(), ""));
                    }
                }
                adapter = new RecyclerviewViewRiderAvailabaleOrderAdapter(orderInfoList, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}