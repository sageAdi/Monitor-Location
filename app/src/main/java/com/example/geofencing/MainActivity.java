package com.example.geofencing;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;
    private final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSION_REQUEST_ACCESS_ACCESS_BACKGROUND_LOCATION = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button startBtn = findViewById(R.id.startBtn);


        checkingForeGroundPermission();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkingBackGroundPermission();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkingPackageUserStatePermission();
        }
        startBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        });

        Log.d(TAG, "onCreate: inside Main");
        BlockScreenReceiver bsr = new BlockScreenReceiver();
        IntentFilter filter = new IntentFilter("com.example.geofencing.ActiveApps");
        registerReceiver(bsr,filter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkingPackageUserStatePermission() {
        if (!hasUserStatePermission()) {
            startActivityForResult(
                    new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                    PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void checkingBackGroundPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    PERMISSION_REQUEST_ACCESS_ACCESS_BACKGROUND_LOCATION);
        }
    }

    // detect whether a user of the app to open the "Apps with usage access" permission
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasUserStatePermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }


    private void checkingForeGroundPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions,
                                           @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION:
                Log.d(TAG, "onRequestPermissionsResult: ACCESS_FINE_LOCATION");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We have the permission
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: Permission not granted");
                }
                break;
            case PERMISSION_REQUEST_ACCESS_ACCESS_BACKGROUND_LOCATION:
                Log.d(TAG, "onRequestPermissionsResult: ACCESS_BACKGROUND_LOCATION");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We have the permission
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: Permission not granted");
                }
                break;
            case PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS:
                Log.d(TAG, "onRequestPermissionsResult: PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We have the permission
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: Permission not granted");
                }
                break;
            default:
                Log.d(TAG, "onRequestPermissionsResult: All Permission Granted");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS) {
            Log.d(TAG, "onActivityResult: "+data);
        }
    }
}