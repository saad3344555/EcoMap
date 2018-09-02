package com.edgeon.ecomapapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.edgeon.ecomapapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    private static int PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                locationPermissionCheck();
            }
        }, SPLASH_TIME_OUT);

    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent i = new Intent(Splash.this, Login.class);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(this, "Location is necessary", Toast.LENGTH_SHORT).show();
                locationPermissionCheck();
            }
        }

    }

    private void locationPermissionCheck() {
        Intent i = new Intent(Splash.this, Login.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Splash.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST);
            } else {
                startActivity(i);
                finish();
            }
        } else {
            startActivity(i);
            finish();
        }
    }
}
