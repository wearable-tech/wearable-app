package org.wearableapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.wearableapp.services.LocationService;
import org.wearableapp.services.MqttService;
import org.wearableapp.services.SubscribeService;

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

        final Context context = this;

        Button onBtn = (Button) findViewById(R.id.turnOn);
        onBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("BUTTON", "BUTTON CLICKED......");
                Log.i("TAG", "Init Subscribe Service");
                Intent intent = new Intent(context, SubscribeService.class);
                startService(intent);
                Log.i("TAG", "Subscribe Service Created!!!");
            }
        });
    }
}
