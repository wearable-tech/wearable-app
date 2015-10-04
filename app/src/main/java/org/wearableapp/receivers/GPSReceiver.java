package org.wearableapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import org.wearableapp.Location;

public class GPSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.i("GPSPROVIDER", "GPS HABILITADO " + gpsEnabled);

        if (gpsEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new GPSListener());
        }
    }

    private class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(android.location.Location location) {
            Log.i("LATITUDE", String.valueOf(location.getLatitude()));
            Log.i("LONGITUDE", String.valueOf(location.getLongitude()));

            Location.LATITUDE = location.getLatitude();
            Location.LONGITUDE = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) { }

        @Override
        public void onProviderEnabled(String s) { }

        @Override
        public void onProviderDisabled(String s) { }
    }
}
