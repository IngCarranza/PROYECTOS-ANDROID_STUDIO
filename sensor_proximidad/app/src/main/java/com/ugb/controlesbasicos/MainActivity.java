package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    TextView tempval;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        tempval = findViewById(R.id.lblSensorProximidad);
        activarSensorAcelerometro();
    }

    @Override
    protected void onResume() {
        iniciar();
        super.onResume();
    }

    @Override
    protected void onPause() {
        detener();
        super.onPause();
    }

    private void activarSensorAcelerometro(){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (sensor==null){
            tempval.setText("tu dispositivo no cuenta con el sensor de luz");
            finish();
        }
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
               double valor = sensorEvent.values[0];
               tempval.setText("Proximidad: "+valor);

               if(valor<=4) {
                   getWindow().getDecorView().setBackgroundColor(Color.BLUE);
               } else if (valor<=8) {
                   getWindow().getDecorView().setBackgroundColor(Color.RED);
               } else {
                   getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
               }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }
    private void iniciar(){
        sensorManager.registerListener(sensorEventListener, sensor, 2000*1000);
    }
    private  void detener(){
        sensorManager.unregisterListener(sensorEventListener);
    }
}