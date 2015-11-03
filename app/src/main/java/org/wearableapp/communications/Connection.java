package org.wearableapp.communications;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.wearableapp.App;
import org.wearableapp.users.LoginActivity;

public class Connection {
    private static Connection connection;
    private static String clientId;
    private volatile IMqttAsyncClient mqttClient;
    private static boolean connectivity;

    private Connection() {
        clientId = App.getPreferences().getString("email", "");
        Log.i("CONNECTION", "CLIENT ID " + clientId);
    }

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
        if (!connectivity || (mqttClient != null && mqttClient.isConnected())) {
            return;
        }

        Log.i("CONNECT", "Connecting MQTT...");

        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        try {
            mqttClient = new MqttAsyncClient(Server.SCHEME_MQTT + "://" + Server.HOST + ":" +
                    Server.PORT_MQTT, clientId, new MemoryPersistence());
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