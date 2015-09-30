package org.wearable.app.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class LocationService extends Service {

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("RECEIVER", "RECEIVER IN LOCATION............");
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.i("GPSPROVIDER", "GPS HABILITADO " + gpsEnabled);

            if (gpsEnabled) {
                LocationListener locationListener = new MyLocationListener();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            }
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.i("LOCATIONLISTENER", "LOCALIZAÇÃO.................");
            Log.i("LATITUDE", String.valueOf(location.getLatitude()));
            Log.i("LONGITUDE", String.valueOf(location.getLongitude()));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) { }

        @Override
        public void onProviderEnabled(String s) { }

        @Override
        public void onProviderDisabled(String s) { }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("LOCATION_SERVICE", "ONCREATE");
        IntentFilter f = new IntentFilter();
        f.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        f.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(new Receiver(), f);
        Log.i("LOCATION_SERVICE", "RECEIVER REGISTERED.............");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("ON_START", "onStartCommand()");
        return START_STICKY;
    }
}
