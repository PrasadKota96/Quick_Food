package uk.ac.tees.aad.w9316578.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import uk.ac.tees.aad.w9316578.R;
import uk.ac.tees.aad.w9316578.Receiver.NetworkReceiver;

public class RiderHomeActivity extends AppCompatActivity {

    private BroadcastReceiver mNetworkReceiver;
    Toolbar toolbar;
    CardView cardViewGoogleMap,cardViewViewOrder,completedOrder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_home);
        mNetworkReceiver = new NetworkReceiver();
        registerNetworkBroadcastForNougat();

        toolbar=findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rider");

        cardViewGoogleMap=findViewById(R.id.googleMap);
        cardViewViewOrder=findViewById(R.id.availableRider);
        cardViewViewOrder=findViewById(R.id.availableRider);
        completedOrder=findViewById(R.id.completedOrder);

        cardViewGoogleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RiderHomeActivity.this,RiderTrackActivity.class));
            }
        });

        cardViewViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RiderHomeActivity.this,RiderAvailableOrderActivity.class));
            }
        });

        completedOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RiderHomeActivity.this,RiderCompletedOrderActivity.class));
            }
        });



    }


    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
}