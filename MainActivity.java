package com.example.newu.hickerwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;

    TextView latitude,longitude,altitude,accuracy;

    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            startlistening();
        }
    }

    public void updatelocationinfo(Location location){

        String address="";
        Log.i("tag",location.toString());


        latitude.setText("Latitude:"+location.getLatitude());

        longitude.setText("Longitude:"+location.getLongitude());

        accuracy.setText("Accuracy:"+location.getAccuracy());

        altitude.setText("Altitude:"+location.getAltitude());

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listaddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(listaddress!=null && listaddress.size()>0){
                Log.i("place address",listaddress.get(0).toString());

                if(listaddress.get(0).getSubThoroughfare()!=null) {
                    address += listaddress.get(0).getSubThoroughfare() + " ";
                }

                if(listaddress.get(0).getThoroughfare()!=null) {
                    address += listaddress.get(0).getThoroughfare() + "\n";
                }

                if(listaddress.get(0).getLocality()!=null) {
                    address += listaddress.get(0).getLocality() + "\n";
                }

                if(listaddress.get(0).getPostalCode()!=null) {
                    address += listaddress.get(0).getPostalCode() + "\n";
                }

                if(listaddress.get(0).getCountryName()!=null) {
                    address += listaddress.get(0).getCountryName() + "\n";
                }

            }

            TextView addresss=(TextView)findViewById(R.id.address);
            addresss.setText("Address:"+"\n"+address);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startlistening(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitude=(TextView)findViewById(R.id.Latitude);

        longitude=(TextView)findViewById(R.id.longitude);

        altitude=(TextView)findViewById(R.id.Altitude);

        accuracy=(TextView)findViewById(R.id.Accuracy);


        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updatelocationinfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(Build.VERSION.SDK_INT<23){
            startlistening();
        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(location!=null) {

                    updatelocationinfo(location);
                }
            }
        }
    }
}