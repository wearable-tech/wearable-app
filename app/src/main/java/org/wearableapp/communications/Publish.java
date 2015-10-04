package org.wearableapp.communications;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Publish {

    private Connection connection;
    private IMqttAsyncClient mqttClient;

    public Publish() {
        this.connection = Connection.getConnection();
        this.connection.doConnect();
        this.mqttClient = this.connection.getMqttClient();
    }

    public void doPublish(String topic, String message, int qos) {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(qos);

        try {
            this.mqttClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            Log.e("MQTTExceptionPublish", e.getMessage());
            e.printStackTrace();
        }
    }
}
