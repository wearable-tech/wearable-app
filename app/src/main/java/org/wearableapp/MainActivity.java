package org.wearableapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.wearableapp.services.LocationService;
import org.wearableapp.services.MqttService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent;

        Log.i("TAG", "Init MQTT Service");
        intent = new Intent(this, MqttService.class);
        startService(intent);
        Log.i("TAG", "MQTT Service Created!!!");

        Log.i("TAG", "Init Location Service");
        intent = new Intent(this, LocationService.class);
        startService(intent);
        Log.i("TAG", "Location Service Created!!!");
    }

}