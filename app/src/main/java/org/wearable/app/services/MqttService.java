package org.wearable.app.services;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

public class MqttService extends Service {

    private static final String TAG = "MQTTService";
    private static boolean hasWifi = false;
    private static boolean hasMmobile = false;
    private ConnectivityManager mConnMan;
    private volatile IMqttAsyncClient mqttClient;
    private String deviceId;

    class MQTTBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo infos[] = mConnMan.getAllNetworkInfo();

            for (int i = 0; i < infos.length; i++){
                if (infos[i].getTypeName().equalsIgnoreCase("MOBILE")){
                    hasMmobile = infos[i].isConnected();
                    Log.d(TAG, infos[i].getTypeName() + " is " + infos[i].isConnected());
                }
                else if ( infos[i].getTypeName().equalsIgnoreCase("WIFI") ) {
                    hasWifi = infos[i].isConnected();
                    Log.d(TAG, infos[i].getTypeName() + " is " + infos[i].isConnected());
                }
            }

            boolean hasConnectivity = hasMmobile || hasWifi;
            Log.v(TAG, "hasConn: " + hasConnectivity + " - "+(mqttClient == null || !mqttClient.isConnected()));

            if (hasConnectivity && (mqttClient == null || !mqttClient.isConnected())) {
                doConnect();
                doSubscribe();
            }
            else if (!hasConnectivity && mqttClient != null && mqttClient.isConnected()) {
                Log.d(TAG, "doDisconnect()");
                try {
                    IMqttToken token = mqttClient.disconnect();
                    token.waitForCompletion(1000);
                } catch (MqttException e) {
                    Log.e("MQTTException", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onCreate() {
        IntentFilter intentf = new IntentFilter();
        setClientID();
        intentf.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new MQTTBroadcastReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        return START_STICKY;
    }

    private void setClientID(){
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        deviceId = wInfo.getMacAddress();

        if(deviceId == null){
            deviceId = MqttAsyncClient.generateClientId();
        }
    }

    private void doConnect(){
        Log.d(TAG, "doConnect()");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        try {
            mqttClient = new MqttAsyncClient("tcp://10.0.2.2:1883", deviceId, new MemoryPersistence());
            IMqttToken token = mqttClient.connect();
            token.waitForCompletion(3500);
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            Log.e("MQTTException", e.getMessage());
            e.printStackTrace();
        }
    }

    private void doSubscribe() {
        if (mqttClient == null || !mqttClient.isConnected()) {
            return;
        }

        mqttClient.setCallback(new MqttEventCallback());

        IMqttToken token = null;
        try {
            token = mqttClient.subscribe("test", 2);
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
            Log.i(TAG, "Message arrived from topic: " + topic);
            Log.i(TAG, "Messagem: " + msg.toString());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind called");
        return null;
    }

}
