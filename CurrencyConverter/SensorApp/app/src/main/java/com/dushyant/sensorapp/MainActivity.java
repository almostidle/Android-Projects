package com.dushyant.sensorapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor accel, light, prox;
    private TextView tvAccel, tvLight, tvProx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAccel = findViewById(R.id.tvAccel);
        tvLight = findViewById(R.id.tvLight);
        tvProx = findViewById(R.id.tvProx);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        light = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        prox = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // check if phone actually has these sensors
        if (accel == null) tvAccel.setText("No Accelerometer");
        if (light == null) tvLight.setText("No Light Sensor");
        if (prox == null) tvProx.setText("No Proximity Sensor");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accel != null) sm.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
        if (light != null) sm.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        if (prox != null) sm.registerListener(this, prox, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop listening to save battery
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            tvAccel.setText("X: " + x + "\nY: " + y + "\nZ: " + z);
        } 
        else if (type == Sensor.TYPE_LIGHT) {
            tvLight.setText("Light: " + event.values[0] + " lux");
        } 
        else if (type == Sensor.TYPE_PROXIMITY) {
            float dist = event.values[0];
            String status = (dist < prox.getMaximumRange()) ? "NEAR" : "FAR";
            tvProx.setText("Distance: " + dist + " cm (" + status + ")");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // don't need this
    }
}