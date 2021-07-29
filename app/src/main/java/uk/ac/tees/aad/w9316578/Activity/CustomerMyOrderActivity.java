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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewViewCartAdapter;
import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewViewCustomerPlacedOrderAdapter;
import uk.ac.tees.aad.w9316578.Model.OrderInfo;
import uk.ac.tees.aad.w9316578.R;
import uk.ac.tees.aad.w9316578.SqliteDatabase.DataBaseHelper;

public class CustomerMyOrderActivity extends AppCompatActivity {

    DatabaseReference mOrderRef, mOrderInfo, mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<OrderInfo>orderInfoList;
    RecyclerviewViewCustomerPlacedOrderAdapter adapter;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("My Orders");

        orderInfoList=new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//       
//        adapter = new RecyclerviewViewCartAdapter(getAllCart(), this);
//        recyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mOrderRef = FirebaseDatabase.getInstance().getReference().child("OrderFoodItems");
        mOrderInfo = FirebaseDatabase.getInstance().getReference().child("OrderInfo");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Customer");
        
        LaodOrder();
    }

    private void LaodOrder() {
        mOrderInfo.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                orderInfoList=new ArrayList<>();
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    OrderInfo orderInfo=snapshot1.getValue(OrderInfo.class);
                    orderInfoList.add(orderInfo);
                }
                adapter=new RecyclerviewViewCustomerPlacedOrderAdapter(orderInfoList,getApplicationContext());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}