package org.wearableapp.communications;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Connection {
    private static Connection connection;
    private static final String SCHEME = "tcp";
    private static final String HOST = "192.168.1.7";
    private static final int PORT = 1883;
    private static final String DEVICE_ID = "TEST";
    private volatile IMqttAsyncClient mqttClient;
    private static boolean connectivity;

    private Connection() {};

    public static Connection getConnection() {
        if (connection == null) {
            connection = new Connection();
        }

        return connection;
    }

    public IMqttAsyncClient getMqttClient() {
        return this.mqttClient;
    }

    public static void enableConnection() {
        connectivity = true;
    }

    public static void disableConnection() {
        connectivity = false;
    }

    public void doConnect() {
        if (mqttClient != null && mqttClient.isConnected()) {
            return;
        }

        Log.i("CONNECT", "Connecting mqtt...");

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        try {
            mqttClient = new MqttAsyncClient(SCHEME + "://" + HOST + ":" + PORT, DEVICE_ID, new MemoryPersistence());
            IMqttToken token = mqttClient.connect();
            token.waitForCompletion(3500);
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            Log.e("MQTTException", e.getMessage());
            e.printStackTrace();
        }
    }
}