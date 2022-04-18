package com.example.proximity_sensor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor proximitysensor;
    Vibrator vibrator;

    boolean isproximitysensoravailable;

    ConstraintLayout constraintLayout;
    TextView tv;
    Uri uri;
    Ringtone ringtone;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.main_textview);


        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null)
        {
            proximitysensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            isproximitysensoravailable = true;
        }
        else
        {
            tv.setText("Proximity sensor not available");
            isproximitysensoravailable= false;
            Toast.makeText(this, "Proximity sensor not available", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onSensorChanged(SensorEvent event) {
        tv.setText(event.values[0]+"cm");


        if (event.values[0] == 0.0)
        {
            if (ringtone != null) {
                ringtone.stop();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            }
            else {
                vibrator.vibrate(500);
            }
        }
        else
        {
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(this, uri);

            ringtone.play();

        }


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (accuracy == 8)
        {
            constraintLayout.setBackgroundColor(R.color.purple_700);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isproximitysensoravailable)
        {
            sensorManager.registerListener(this, proximitysensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isproximitysensoravailable)
        {
            sensorManager.unregisterListener(this);
        }
    }
}