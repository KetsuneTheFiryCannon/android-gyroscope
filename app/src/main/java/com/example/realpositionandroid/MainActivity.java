package com.example.realpositionandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public SensorManager sensorManager;
    public Sensor sensor;
    public SensorEventListener sensorEventListener;
    public TextView tv;
    public TextView tv2;
    public TextView tv3;
    public TextView tv4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        tv = findViewById(R.id.coordinates);
        tv2 = findViewById(R.id.coordinates2);
        tv3 = findViewById(R.id.coordinates3);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);

                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);
                for (int i = 0; i<3;i++)
                {
                    orientations[i] = (float) (Math.toDegrees(orientations[i]));
                }
                tv.setText("X " + String.valueOf((int)orientations[1]));
                tv2.setText("Z " + String.valueOf((int)orientations[2]));
                tv3.setText("Y " + String.valueOf((int)orientations[0]));
                //tv4.setText(String.valueOf(orientations[3]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }
}