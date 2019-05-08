package com.example.spiritlevel;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mGravity;
    private SpiritLevelView spiritLevelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : deviceSensors) {
            Log.i("MainActivity", sensor.getName());
        }
        mGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        spiritLevelView = findViewById(R.id.spiritView);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // Logs percentage of x, y, z
        Log.i("MainActivity", String.format("x: %.2f%%, y: %.2f%%, z: %.2f%%",
                (event.values[1]-(-9.81))/(9.81-(-9.81))*100,
                (event.values[0]-(-9.81))/(9.81-(-9.81))*100,
                (event.values[2]-(-9.81))/(9.81-(-9.81))*100));

        spiritLevelView.moveCircle(
                (int)(((event.values[0] - (-9.81)) / (9.81 - (-9.81))) * (spiritLevelView.getHeight())),
                (int)(((event.values[1] - (-9.81)) / (9.81 - (-9.81))) * (spiritLevelView.getWidth())));
        spiritLevelView.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}