package uk.ac.tees.aad.w9316578.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

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

import java.util.HashMap;

import uk.ac.tees.aad.w9316578.Activity.CustomerHomeActivity;
import uk.ac.tees.aad.w9316578.MainActivity;
import uk.ac.tees.aad.w9316578.Model.OrderInfoForRider;
import uk.ac.tees.aad.w9316578.R;

public class MyService extends Service {
    DatabaseReference orderNotification;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        orderNotification = FirebaseDatabase.getInstance().getReference().child("OrderNotification");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mUser != null) {

            orderNotification.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            if (snapshot1.child("notificationStatus").getValue().toString().equals("unseen"))
                            {
                                LauchNotification(snapshot1);
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void LauchNotification(DataSnapshot snapshot) {
        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "CHANNEL";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

        Notification notification = new Notification.Builder(getBaseContext())
                .setContentTitle("Order Sent!")
                .setContentText("Visit Order History to See status & Ready To Collect.")
                .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
                .setChannelId(CHANNEL_ID)
                .build();


        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager.notify(notifyID, notification);

        HashMap hashMap=new HashMap();
        hashMap.put("notificationStatus","seen");
        orderNotification.child(mUser.getUid()).child(snapshot.getRef().getKey().toString())
                .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {
                if (!task.isSuccessful())
                {
                    Toast.makeText(MyService.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}