package org.wearableapp.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.wearableapp.App;
import org.wearableapp.Measurement;
import org.wearableapp.communications.Location;
import org.wearableapp.communications.Publish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BluetoothReader extends Thread {

    private final InputStream inputStream;
    private final String email;

    public BluetoothReader(BluetoothSocket socket) {
        email = App.getPreferences().getString("email", "");

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
            String oxygen = "";
            String pulseRate = "";

            try {
                // Read from the InputStream
                Log.i("Bluetooth", "Before read");
                oxygen = reader.readLine();
                pulseRate = reader.readLine();

                Log.i("OXYGEN", oxygen);
                Log.i("PULSE", pulseRate);

            } catch (IOException e) {
                break;
            }

            try {
                Measurement.OXYGEN = Double.parseDouble(oxygen);
                Measurement.PULSE_RATE = Double.parseDouble(pulseRate);
                Measurement.REMEASUREMENT = true;
            } catch (NumberFormatException e) {
                Log.e("DOUBLE_PARSER", "Double Parser " + e.toString());
                continue;
            }

            String message = Location.LATITUDE + "," + Location.LONGITUDE + "," + oxygen + "," + pulseRate;
            Log.i("Message", message);

            Publish publish = new Publish();
            publish.doPublish("from_" + email, message, 0);
        }
    }
}
