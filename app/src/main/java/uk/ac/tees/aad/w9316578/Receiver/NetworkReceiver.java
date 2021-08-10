package uk.ac.tees.aad.w9316578.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkAvailable(context)) {
       //     Toast.makeText(context, "Network Available Do operations", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, "Network is not Available", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable( Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}