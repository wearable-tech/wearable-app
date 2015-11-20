package org.wearableapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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
        Log.i("BLUETOOTH_SERVICE", "Calling thread to connect bluetooth");
        bluetoothConnector = new BluetoothConnector(BluetoothActivity.getBluetoothDevice(), BluetoothActivity.getBluetoothAdapter());
        bluetoothConnector.start();
    }

    @Override
    public void onDestroy() {
        Log.i("BLUETOOTH_SERVICE", "Destroing bluetooth thread");
        bluetoothConnector.cancel();
    }
}
