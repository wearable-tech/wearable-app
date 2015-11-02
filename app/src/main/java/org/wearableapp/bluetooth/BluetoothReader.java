package org.wearableapp.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.wearableapp.Measurement;
import org.wearableapp.communications.Location;
import org.wearableapp.communications.Publish;
import org.wearableapp.users.LoginActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BluetoothReader extends Thread {

    private final InputStream inputStream;
    private final String email;

    public BluetoothReader(Context context, BluetoothSocket socket) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LoginActivity.USER_FILE, Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");

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

                Measurement.OXYGEN = Double.parseDouble(oxygen);
                Measurement.PULSE_RATE = Double.parseDouble(pulseRate);
                Measurement.REMEASUREMENT = true;

                Publish publish = new Publish();

                String message = Location.LATITUDE + "," + Location.LONGITUDE + "," + oxygen + "," + pulseRate;
                Log.i("Message", message);
                publish.doPublish("from_" + email, message, 2);
            } catch (IOException e) {
                break;
            }
        }
    }
}
