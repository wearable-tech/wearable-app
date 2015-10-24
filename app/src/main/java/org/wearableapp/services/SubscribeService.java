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
        subscribe.doSubscribe(emailConnected, 2);

        for (HashMap<String, String> c: contacts) {
            subscribe.doSubscribe(c.get("email"), 2);
        }
    }

    private void contactsStopSubscribe() {
        Subscribe subscribe = new Subscribe(this);
        subscribe.stopSubscribe(emailConnected);

        for (HashMap<String, String> c: contacts) {
            subscribe.stopSubscribe(c.get("email"));
        }
    }
}
