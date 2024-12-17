package com.ugb.controlesbasicos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView tempval;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        tempval = findViewById(R.id.lblSensorGPS);
        obtenerPosicion();

    }
    private void obtenerPosicion(){
        try {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
                tempval.setText("Solicitando permisos de localizacion...");
            }
            /*Location location;
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mostrarPosicion(location);*/

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    mostrarPosicion(location);
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 5, locationListener);
        }catch (Exception e){
            tempval.setText(e.getMessage());
        }
    }
    private void mostrarPosicion(Location location){
        tempval.setText("Posicion: Latitud"+ location.getLatitude()+ "; Longitud: "+ location.getLongitude()+"; Altitud"+ location.getAltitude());

    }
}