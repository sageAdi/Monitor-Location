package com.example.geofencing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Log.d(TAG, "onCreate: Splash Screen Activity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}