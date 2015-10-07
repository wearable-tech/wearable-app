package org.wearableapp.communications;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.wearableapp.Location;

public class Subscribe {

    private Connection connection;
    private IMqttAsyncClient mqttClient;

    public Subscribe() {
        this.connection = Connection.getConnection();
        this.connection.doConnect();
        this.mqttClient = this.connection.getMqttClient();
    }

    public void doSubscribe(String topic, int qos) {
        if (this.mqttClient == null || !this.mqttClient.isConnected()) {
            return;
        }

        this.mqttClient.setCallback(new MqttEventCallback());

        IMqttToken token = null;
        try {
            token = this.mqttClient.subscribe(topic, qos);
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

            Log.i("LOCATION", "Send my location");
            Publish publish = new Publish();
            publish.doPublish("location", String.valueOf(Location.LATITUDE) + "," + String.valueOf(Location.LONGITUDE), 2);
        }
    }
}
