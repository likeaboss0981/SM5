package com.example.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    public static final String EXTRA_SENSOR_TYPE = "EXTRA_SENSOR_TYPE";
    private SensorManager sensorManager;
    private Sensor sensorLight;
    private TextView sensorText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);
        sensorText = findViewById(R.id.sensor_light_label);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        int type = getIntent().getExtras().getInt(EXTRA_SENSOR_TYPE, -1);
        sensorLight = sensorManager.getDefaultSensor(type);

        if(sensorLight == null){
            sensorText.setText(R.string.missing_sensor);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];

        switch(sensorType){
            case Sensor.TYPE_LIGHT:
                sensorText.setText(getString(R.string.light_sensor_label, currentValue));
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                sensorText.setText(getString(R.string.temperature_sensor_label, currentValue));
                break;
            default:

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("tag", "Metoda onAccuracyChanged z wartością " + accuracy);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sensorLight !=null){
            sensorManager.registerListener(this,sensorLight,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}