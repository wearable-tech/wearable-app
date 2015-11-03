package org.wearableapp.communications;

import android.content.Intent;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.wearableapp.App;
import org.wearableapp.MenuActivity;

public class Subscribe {

    private IMqttAsyncClient mqttClient;

    public Subscribe() {
        Connection connection = Connection.getConnection();
        connection.doConnect();
        this.mqttClient = connection.getMqttClient();
    }

    public void doSubscribe(String topic, int qos) {
        if (this.mqttClient == null || !this.mqttClient.isConnected()) {
            return;
        }

        this.mqttClient.setCallback(new MqttEventCallback());

        try {
            IMqttToken token = this.mqttClient.subscribe(topic, qos);
            token.waitForCompletion(5000);
        } catch (MqttException e) {
            Log.e("MQTTException", e.getMessage());
            e.printStackTrace();
        }

        Log.i("SUBSCRIBE", "SUBSCRIBE FINISHED....");
    }

    public void stopSubscribe(String topic) {
        try {
            IMqttToken token = this.mqttClient.unsubscribe(topic);
            token.waitForCompletion(5000);
        } catch (MqttException e) {
            Log.e("MQTTException", e.getMessage());
            e.printStackTrace();
        }
    }

    private class MqttEventCallback implements MqttCallback {
        @Override
        public void connectionLost(Throwable arg0) { }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) { }

        @Override
        public void messageArrived(String topic, final MqttMessage msg) throws Exception {
            Log.i("MESSAGE_ARRIVED", "Message arrived from topic: " + topic);
            Log.i("MESSAGE_ARRIVED", "Messagem: " + msg.toString());

            Intent intent = new Intent(App.getContext(), MenuActivity.class);
            Notify.notification(msg.toString(), intent, "titulo");
        }
    }
}
