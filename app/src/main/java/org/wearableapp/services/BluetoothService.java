package org.wearableapp.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.wearableapp.App;
import org.wearableapp.bluetooth.BluetoothActivity;
import org.wearableapp.bluetooth.BluetoothConnector;

public class BluetoothService extends Service {

    private BluetoothConnector bluetoothConnector;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        setPreferences(true);

        Log.i("BLUETOOTH_SERVICE", "Calling thread to connect bluetooth");
        bluetoothConnector = new BluetoothConnector(BluetoothActivity.getBluetoothDevice(), BluetoothActivity.getBluetoothAdapter());
        bluetoothConnector.start();
    }

    @Override
    public void onDestroy() {
        setPreferences(false);

        Log.i("BLUETOOTH_SERVICE", "Destroing bluetooth thread");
        bluetoothConnector.cancel();
    }

    private void setPreferences(boolean status) {
        SharedPreferences.Editor editor = App.getPreferences().edit();

        if (status)
            editor.putBoolean("wearableStatus", status);
        else
            editor.remove("wearableStatus");

        editor.commit();
    }
}
