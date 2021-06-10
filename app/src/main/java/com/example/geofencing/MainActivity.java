package com.example.geofencing;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final int PERMISSION_REQUEST_CODE = 100;
    private Button startBtn;
    private TextInputEditText latitudeText, longitudeText, radiusText;
    private String latitudeString, longitudeString, radiusString;
    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            latitudeString = latitudeText.getText().toString().trim();
            longitudeString = longitudeText.getText().toString().trim();
            radiusString = radiusText.getText().toString().trim();
            /*startBtn.setEnabled(!latitudeString.isEmpty()
                    && !longitudeString.isEmpty()
                    && !radiusString.isEmpty()
            );*/
            startBtn.setEnabled((!radiusString.isEmpty()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private ArrayList<String> permissions = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn = findViewById(R.id.startBtn);

        latitudeText = findViewById(R.id.latitudeText);
        longitudeText = findViewById(R.id.longitudeText);
        radiusText = findViewById(R.id.radiusText);

        latitudeText.addTextChangedListener(textWatcher);
        longitudeText.addTextChangedListener(textWatcher);
        radiusText.addTextChangedListener(textWatcher);

        latitudeString = latitudeText.getText().toString().trim();
        longitudeString = longitudeText.getText().toString().trim();
        radiusString = radiusText.getText().toString().trim();

        checkingPermission();
        //mapActivity();
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapActivity();
                //cameraActivity();
                //cameraService();
            }
        });

    }


    private void checkingPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (!permissions.isEmpty()) {
            String[] per = permissions.toArray(new String[permissions.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,
                    per,
                    PERMISSION_REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions,
                                           @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            Log.d(TAG, "onRequestPermissionsResult: " + permissions[0]);
        }
    }

    private void cameraService() {
        startService(new Intent(this, CameraService.class));
    }

    public void mapActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("latitude", latitudeString);
        bundle.putString("longitude", longitudeString);
        bundle.putString("radius", radiusString);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void cameraActivity() {
        Intent intent = new Intent(this, Camera2.class);
        startActivity(intent);
    }

}