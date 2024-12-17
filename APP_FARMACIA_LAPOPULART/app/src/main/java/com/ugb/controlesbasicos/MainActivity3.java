package com.ugb.controlesbasicos;


import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private ProgressBar lightProgress;
    private ImageView lightBulb;
    private TextView lightInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        lightProgress = findViewById(R.id.light_progress);
        lightBulb = findViewById(R.id.light_bulb);
        lightInfo = findViewById(R.id.light_info);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];
            lightProgress.setProgress((int) lightValue);
            lightInfo.setText("Iluminancia: " + lightValue + " lx");

            if (lightValue < 100) {
                lightBulb.setColorFilter(Color.GRAY);
                lightBulb.setImageResource(R.drawable.img_40);

            } else if (lightValue < 900) {
                lightBulb.setColorFilter(Color.YELLOW);
                lightBulb.setImageResource(R.drawable.img_40);

            } else if (lightValue < 1000) {
                lightBulb.setColorFilter(Color.GREEN);
                lightBulb.setImageResource(R.drawable.img_40);
            } else {
                lightBulb.setColorFilter(Color.RED);
                lightBulb.setImageResource(R.drawable.img_40);
            }
        }
        // ESTE ES EL BOTON QUE HICE PARA RETROCEDER
        ImageButton regresarButton = findViewById(R.id.regresarbtnactiviti3);
        regresarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Esto lo hice para que me cierre la actividad actual y volverá al activity anterior
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No es necesario implementar esto para el sensor de luz.
    }

}
