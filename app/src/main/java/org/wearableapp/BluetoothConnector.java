package org.wearableapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class BluetoothConnector extends Thread {

    private final BluetoothDevice braceletDevice;
    private final BluetoothSocket bluetoothSocket;
    private final BluetoothAdapter braceletAdapter;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public BluetoothConnector(BluetoothDevice device, BluetoothAdapter adapter) {
        braceletDevice = device;
        braceletAdapter = adapter;
        BluetoothSocket tmp = null;

        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        bluetoothSocket = tmp;
    }

    @Override
    public void run() {
        braceletAdapter.cancelDiscovery();

        try {
            bluetoothSocket.connect();
        }
        catch (IOException e) {
            cancel();
            return;
        }

        manageConnectedSocket();
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void manageConnectedSocket() {
        Log.i("BLUETOOTH_READER", "Calling thread to read data from bluetooth");
        BluetoothReader bluetoothReader = new BluetoothReader(bluetoothSocket);
        bluetoothReader.start();
    }
}
