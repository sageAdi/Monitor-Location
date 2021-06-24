package com.example.geofencing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BlockScreenReceiver extends BroadcastReceiver {
    private static final String TAG = "BlockScreenReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        // throw new UnsupportedOperationException("Not yet implemented");
        if (intent.getAction() == "com.example.geofencing.ActiveApps"){
            context.startActivity(new Intent(context, SplashScreen.class));
            Log.d(TAG, "onReceive: inside Blocked receiver");
        }
    }
}