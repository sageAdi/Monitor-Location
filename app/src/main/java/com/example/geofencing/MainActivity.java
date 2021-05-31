package com.example.geofencing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    Button startBtn;
    TextInputLayout latitude,longitude,radius;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn = findViewById(R.id.startBtn);
        latitude = findViewById(R.id.Latitude);
        longitude = findViewById(R.id.Longitude);
        radius = findViewById(R.id.Radius);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude.getEditText().getText().toString().length()>0
                        && longitude.getEditText().getText().toString().length()>0
                        && radius.getEditText().getText().toString().length()>0)
                {
                    mapActivity();
                }
                else{
                    if(latitude.getEditText().getText().toString().length()==0){
                        latitude.requestFocus();
                        latitude.setError("It should not be empty!!!");
                    }
                    if(longitude.getEditText().getText().toString().length()==0){
                        longitude.requestFocus();
                        longitude.setError("It should not be empty!!!");
                    }
                    if(radius.getEditText().getText().toString().length()==0){
                        radius.requestFocus();
                        radius.setError("It should not be empty!!!");
                    }
                }
            }
        });

    }
    public void mapActivity(){
        Intent intent = new Intent(this,MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("latitude",latitude.getEditText().getText().toString());
        bundle.putString("longitude",longitude.getEditText().getText().toString());
        bundle.putString("radius",radius.getEditText().getText().toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}