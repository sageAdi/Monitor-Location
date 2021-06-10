package com.example.geofencing;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CameraForegroundService extends Service {
    public CameraForegroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}