package com.example.geofencing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
/*
        Toast.makeText(context, "Geofence Triggered...", Toast.LENGTH_SHORT).show();
*/
        Log.d(TAG, "onReceive: GeofenceReceiver");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Geofence has Error");
            return;
        }
        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        Location location = geofencingEvent.getTriggeringLocation();
        int transitionType = geofencingEvent.getGeofenceTransition();
        Log.d(TAG, "onReceive: " + intent.getAction());
       /* if (intent.getAction().equals("android.intent.action.CAMERA_BUTTON"))
            Toast.makeText(context, "Camera is pressed", Toast.LENGTH_SHORT).show();

            // Else, try to do some action
        else {
            // Fetch the number of incoming call

            // Check, whether this is a member of "Black listed" phone numbers stored in the
            // database

        }*/
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                context.startService(new Intent(context, CameraService.class));
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                context.startService(new Intent(context, CameraService.class));
                Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                context.stopService(new Intent(context, CameraService.class));
                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}