package org.wearableapp.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.wearableapp.receivers.InternetReceiver;

public class MqttService extends Service {

    private BroadcastReceiver internetReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("MQTT_SERVICE", "ONCREATE");
        internetReceiver = new InternetReceiver();
        registerReceiver(internetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("ON_START", "onStartCommand()");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(internetReceiver);
        super.onDestroy();
    }
}
