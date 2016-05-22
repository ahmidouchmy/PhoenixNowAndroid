package com.weebly.helloworldclub.phoenixnow;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    LocationManager lm;
    LocationListener mLocationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    public MainActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BackEnd backend = new BackEnd();
        lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (backend.getToken() == null) {
            setContentView(R.layout.titlepage);
        } else {
            setContentView(R.layout.homepage);
            Log.d("MainActivityToken", backend.getToken());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
            TextView welcometext=(TextView)findViewById(R.id.welcometext);
            welcometext.setText("Welcome, " + backend.getEmail()+"!");
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.removeUpdates(mLocationListener);
    }
    @Override
    protected void onResume(){
        super.onResume();
        BackEnd backend=new BackEnd();
        if (backend.getToken() != null) {
            setContentView(R.layout.homepage);
            Log.d("MainActivityToken", backend.getToken());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
            }
            TextView welcometext=(TextView)findViewById(R.id.welcometext);
            welcometext.setText("Welcome, " + backend.getEmail()+"!");
        }
    }
    public void loginActivity(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    public void registerActivity(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    public void signIn(View view) {
        TextView textview=(TextView)findViewById(R.id.signintext);
        TextView welcometext=(TextView)findViewById(R.id.welcometext);
        BackEnd backend=new BackEnd();
        welcometext.setText("Welcome, " + backend.getEmail());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}
        if(gps_enabled){
            Location location=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        while(location==null) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        textview.setText("Latitude: "+Double.toString(location.getLatitude())+" Longitude: "+Double.toString(location.getLongitude()));
        while(location.hasAccuracy()==false){
            location=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d("lat and long",Double.toString(location.getLatitude())+Double.toString(location.getLongitude()));
        }
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            backend.signIn(latitude, longitude, this);
        }else{
            Toast.makeText(getApplicationContext(),"Please enable location",Toast.LENGTH_LONG).show();
        }
    }
}
