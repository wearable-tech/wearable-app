package org.wearableapp;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BluetoothReader extends Thread {

    private final InputStream inputStream;

    public BluetoothReader(BluetoothSocket socket) {
        InputStream tmp = null;

        try {
            tmp = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputStream = tmp;
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        while (true) {
            try {
                // Read from the InputStream
                Log.i("Bluetooth", "Antes do read");
                String oxygen = reader.readLine();
                String pulseRate = reader.readLine();

                Log.i("Bluetooth", oxygen);
                Log.i("Bluetooth", pulseRate);
            } catch (IOException e) {
                break;
            }
        }
    }
}
