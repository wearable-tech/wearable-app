package org.wearableapp.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.wearableapp.communications.Subscribe;
import org.wearableapp.users.Contact;
import org.wearableapp.users.LoginActivity;

import java.util.HashMap;
import java.util.List;

public class SubscribeService extends Service {
    private List<HashMap<String, String>> contacts;
    private String emailConnected;
    private int levelConnected;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("SUBSCRIBE_SERVICE", "ONCREATE");
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.USER_FILE, MODE_PRIVATE);
        emailConnected = sharedPreferences.getString("email", "");
        levelConnected = sharedPreferences.getInt("level", 0);
        contacts = Contact.list(emailConnected);

        contactsSubscribe();
    }

    @Override
    public void onDestroy() {
        contactsStopSubscribe();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("ON_START", "onStartCommand()");
        return START_STICKY;
    }

    private void contactsSubscribe() {
        Subscribe subscribe = new Subscribe(this);
        Log.i("Subscribe", "to_" + emailConnected + "_" + levelConnected);
        subscribe.doSubscribe("to_" + emailConnected + "_" + levelConnected, 0);

        for (HashMap<String, String> c: contacts) {
            Log.i("Subscribe", "to_" + c.get("email") + "_" + c.get("level"));
            subscribe.doSubscribe("to_" + c.get("email") + "_" + c.get("level"), 0);
        }
    }

    private void contactsStopSubscribe() {
        Subscribe subscribe = new Subscribe(this);
        Log.i("Unsubscribe", "to_" + emailConnected + "_" + levelConnected);
        subscribe.stopSubscribe("to_" + emailConnected + "_" + levelConnected);

        for (HashMap<String, String> c: contacts) {
            Log.i("Unsubscribe", "to_" + c.get("email") + "_" + c.get("level"));
            subscribe.stopSubscribe("to_" + c.get("email") + "_" + c.get("level"));
        }
    }
}
