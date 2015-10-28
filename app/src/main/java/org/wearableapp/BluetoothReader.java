package org.wearableapp;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

public class BluetoothReader extends Thread {

    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;

    public BluetoothReader(BluetoothSocket socket) {
        bluetoothSocket = socket;

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
        byte[] buffer = new byte[10];
        int bytes;

        while (true) {
            try {
                // Read from the InputStream
                if(inputStream.available() > 0) {
                    inputStream.read();
                }
                else {
                    Log.i("Bluetooth", "Antes do read");
                    bytes = inputStream.read(buffer);

                    // Send the obtained bytes to the UI activity
                    Log.i("READER", bytes + " Readed");
                    String s = new String(buffer);
                    Log.i("Bluetooth", s);
                }
            } catch (IOException e) {
                break;
            }
        }
    }
}
