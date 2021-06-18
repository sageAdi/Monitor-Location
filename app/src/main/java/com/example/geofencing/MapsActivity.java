package com.example.geofencing;

import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.geofencing.databinding.ActivityMapsBinding;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 1001;
    private final int REQUEST_PERMISSION_ACCESS_BACKGROUND_LOCATION = 99;
    ActivityMapsBinding binding;
    private GoogleMap mMap;
    private GeofenceHelper geofenceHelper;
    private GeofencingClient geofencingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableForegroundLocation();

        if (Build.VERSION.SDK_INT >= 29) {
            enableBackgroundLocation();
        } else {
            handleGeofence();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void enableBackgroundLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            handleGeofence();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    REQUEST_PERMISSION_ACCESS_BACKGROUND_LOCATION);
        }
    }

    private void handleGeofence() {
        double latitude = 26.6940;
        double longitude = 83.4826;
        /*double latitude = 26.927806;
        double longitude = 80.922944;*/
        float radius = 500;
        // Client
        /*double latitude = 22.532675;
        double longitude = 70.052831;*/
        LatLng latLng = new LatLng(latitude, longitude);
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleOptions.fillColor(Color.argb(64, 255, 0, 0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        Log.d(TAG, "handleGeofence: inside");
        addGeofence(latLng, radius);
    }

    public void addGeofence(LatLng latLng, float radius) {
        Log.d(TAG, "addGeofence: inside");
        // Geofence ID to distinguish between different geofence.
        String GEOFENCE_ID = "GEOFENCE_ID";
        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius,
                Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_DWELL
                        | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_LOCATION_ACCESS_REQUEST_CODE);
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(geofenceHelper, "Geofence Added Successfully",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuccess: Geofence Added");
                })
                .addOnFailureListener(e -> {
                    String errorMessage = geofenceHelper.getErrorMessage(e);
                    Log.d(TAG, "onFailure: " + errorMessage);
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
            int[] grantResults) {
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            Log.d(TAG, "onRequestPermissionsResult: ACCESS_FINE_LOCATION");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We have the permission
                Log.d(TAG, "onRequestPermissionsResult: Permission granted");
            } else {
                Log.d(TAG, "onRequestPermissionsResult: Permission not granted");
            }
        }
        if (Build.VERSION.SDK_INT >= 29) {
            if (requestCode == REQUEST_PERMISSION_ACCESS_BACKGROUND_LOCATION) {
                Log.d(TAG, "onRequestPermissionsResult: ACCESS_BACKGROUND_LOCATION");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We have the permission
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    handleGeofence();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: Permission not granted");
                }
            }
        }
    }

    private void enableForegroundLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
            // Ask for Permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_LOCATION_ACCESS_REQUEST_CODE);

            //mMap.setMyLocationEnabled(true);
        }
    }
}