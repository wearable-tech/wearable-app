package org.wearable.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.wearable.app.services.MqttService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        doPost();
        Log.i("TAG", "Criando Serviço");
        Intent intent = new Intent(this, MqttService.class);
        startService(intent);
        Log.i("TAG", "Serviço criado!!!");
        publish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void doPost() {
        AsyncTask<Void, Void, String> postTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                List params = new ArrayList();
                params.add(new BasicNameValuePair("p1", "111"));
                params.add(new BasicNameValuePair("p2", "222"));

                HttpResponse response = null;

                try {
                    response = Web.doPost(params);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                String responseString = null;
                try {
                    responseString = EntityUtils.toString(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return responseString;
            }
        };
        postTask.execute();
    }

    private void publish() {
        AsyncTask<Void, Void, String> postTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                MemoryPersistence mPer = new MemoryPersistence();
                String clientId = UUID.randomUUID().toString();
                String url = "tcp://10.0.2.2:1883";

                final MqttAndroidClient client = new MqttAndroidClient(getApplicationContext(), url, clientId, mPer);

                try {
                    client.connect(getApplicationContext(), new IMqttActionListener() {

                        @Override
                        public void onSuccess(IMqttToken iMqttToken) {
                            Log.i("TAG", "CLIENTE CONECTADO.....................");

                            MqttMessage message = new MqttMessage("MQTT Message Publish.".getBytes());
                            message.setQos(2);
                            message.setRetained(false);

                            try {
                                client.publish("test", message);
                                Log.i("TAG", "Menssagem publicada");

                                client.disconnect();
                                Log.i("TAG", "cliente desconectado");
                            } catch (MqttPersistenceException e) {
                                e.printStackTrace();
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                            Log.i("TAG", "FALHA NA CONEXÃO.....................");
                            Log.i("ERROR", throwable.getMessage());
                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }

                return "Finsish Connection";
            }
        };
        postTask.execute();
    }
}
