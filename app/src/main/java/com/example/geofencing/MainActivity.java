package com.example.geofencing;

import android.Manifest;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.geofencing.backend.utils.ApplicationFetcher;
import com.example.geofencing.userinterface.ApplicationListAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final int PERMISSION_REQUEST_CODE = 100;
    private Button startBtn;
    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            latitudeString = latitudeText.getText().toString().trim();
//            longitudeString = longitudeText.getText().toString().trim();
//            radiusString = radiusText.getText().toString().trim();
//            /*startBtn.setEnabled(!latitudeString.isEmpty()
//                    && !longitudeString.isEmpty()
//                    && !radiusString.isEmpty()
//            );*/
//            startBtn.setEnabled((!radiusString.isEmpty()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private final ArrayList<String> permissions = new ArrayList<String>();

    //    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.startBtn);
        checkingPermission();
        ListView listView = findViewById(R.id.applicationList);
        ApplicationListAdapter applicationListAdapter = new ApplicationListAdapter(this,
                ApplicationFetcher.Companion.getInstalledApplications(this));
        listView.setAdapter(applicationListAdapter);

//        latitudeText = findViewById(R.id.latitudeText);
//        longitudeText = findViewById(R.id.longitudeText);
//        radiusText = findViewById(R.id.radiusText);
//
//        latitudeText.addTextChangedListener(textWatcher);
//        longitudeText.addTextChangedListener(textWatcher);
//        radiusText.addTextChangedListener(textWatcher);
//
//        latitudeString = latitudeText.getText().toString().trim();
//        longitudeString = longitudeText.getText().toString().trim();
//        radiusString = radiusText.getText().toString().trim();


        //mapActivity();
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapActivity();
                //cameraActivity();
                //cameraService();
            }
        });
        PendingIntent pendingIntent;
        //Intent intent = new Intent(this, MyReceiver2.class);
        MyReceiver2 br = new MyReceiver2();

        MainActivity.this.registerReceiver(br,
                new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
        //sendBroadcast(intent);


        Log.d(TAG, "onCreate: inside");
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList =
                activityManager.getRunningAppProcesses();
        for (int i = 0; i < runningAppProcessInfoList.size(); i++) {
            Log.d(TAG,
                    "Running App: " + runningAppProcessInfoList.size() + runningAppProcessInfoList.get(i).processName);
        }
        try {
            Process mLogcatProc = null;
            BufferedReader reader = null;
            mLogcatProc = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});

            reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));

            String line;
            final StringBuilder log = new StringBuilder();
            String separator = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null) {
                log.append(line);
                log.append(separator);
            }
            String w = log.toString();
            Log.d(TAG, "App running: " + w);
            Toast.makeText(getApplicationContext(), w, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d(TAG, "Error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

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
        startActivity(intent);
//        Intent svc = new Intent(this, OverlayShowingService.class);
//        startService(svc);
//        finish();
    }
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void checkDrawOverlayPermission() {
//        Log.v("App", "Package Name: " + getApplicationContext().getPackageName());
//
//        // Check if we already  have permission to draw over other apps
//        if (!Settings.canDrawOverlays(this)) {
//            Log.v("App", "Requesting Permission" + Settings.canDrawOverlays(this));
//            // if not construct intent to request permission
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + getApplicationContext().getPackageName()));
//            // request permission via start activity for result
//            startActivityForResult(intent, REQUEST_CODE); //It will call onActivityResult
//            Function After you press Yes/No and go Back after giving permission
//        } else {
//            Log.v("App", "We already have permission for it.");
//            //overlayLayout();
//            Intent svc = new Intent(this, OverlayShowingService.class);
//        startService(svc);
//        finish();
//            // disablePullNotificationTouch();
//            // Do your stuff, we got permission captain
//        }
    //}

}