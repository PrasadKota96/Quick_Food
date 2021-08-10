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

import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewViewRiderAvailabaleOrderAdapter;
import uk.ac.tees.aad.w9316578.Adapter.RecyclerviewViewRiderCompletedOrderAdapter;
import uk.ac.tees.aad.w9316578.Model.OrderInfo;
import uk.ac.tees.aad.w9316578.Model.OrderInfoForRider;
import uk.ac.tees.aad.w9316578.R;

public class RiderCompletedOrderActivity extends AppCompatActivity {


    DatabaseReference mOrderRef, mOrderInfo, mUserRef,mRiderPickedOrder;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Toolbar toolbar;

    RecyclerView recyclerView;
    List<OrderInfoForRider>orderInfoForRiderList;
    RecyclerviewViewRiderCompletedOrderAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_completed_order);


        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Completed Orders");


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        orderInfoForRiderList=new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mOrderRef = FirebaseDatabase.getInstance().getReference().child("OrderFoodItems");
        mOrderInfo = FirebaseDatabase.getInstance().getReference().child("OrderInfo");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Customer");
        mRiderPickedOrder = FirebaseDatabase.getInstance().getReference().child("RiderSentOrders");


        LoadMySentOrders();


    }

    private void LoadMySentOrders() {
        mRiderPickedOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    orderInfoForRiderList=new ArrayList<>();
                    for (DataSnapshot snapshot1:snapshot.getChildren())
                    {
                        OrderInfoForRider infoForRider=snapshot1.getValue(OrderInfoForRider.class);
                      if (infoForRider.getRiderID().equals(mUser.getUid()))
                      {
                          orderInfoForRiderList.add(infoForRider);
                      }
                    }
                    adapter=new RecyclerviewViewRiderCompletedOrderAdapter(orderInfoForRiderList,RiderCompletedOrderActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}