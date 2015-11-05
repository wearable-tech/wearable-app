package org.wearableapp.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.wearableapp.receivers.GPSReceiver;

public class LocationService extends Service {
    private GPSReceiver gpsReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("LOCATION_SERVICE", "ONCREATE");
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        gpsReceiver = new GPSReceiver();
        registerReceiver(gpsReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("ON_START", "onStartCommand()");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(gpsReceiver);
        super.onDestroy();
    }
}
