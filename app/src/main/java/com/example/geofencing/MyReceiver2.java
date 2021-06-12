package com.example.geofencing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver2 extends BroadcastReceiver {
    private static final String TAG = "MyReceiver2";
    private boolean airplaneMode = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (!airplaneMode) {
            Log.d(TAG, "onReceive: " + intent.getAction());
            airplaneMode = true;
            Intent intent1 = new Intent(context, MapsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("airplaneMode", "true");
            intent1.putExtras(bundle);
            Toast.makeText(context, "Your app is blocked", Toast.LENGTH_SHORT).show();
            context.startActivity(intent1);
        } else {
            Log.d(TAG, "onReceive: " + intent.getAction());
            airplaneMode = false;
            Intent intent1 = new Intent(context, MapsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("airplaneMode", "false");
            intent1.putExtras(bundle);
            Toast.makeText(context, "Your app is unblocked", Toast.LENGTH_SHORT).show();
            context.startActivity(intent1);
        }

    }
}