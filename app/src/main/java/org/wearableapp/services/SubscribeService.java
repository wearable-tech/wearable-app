package org.wearableapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.wearableapp.communications.Subscribe;

public class SubscribeService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("SUBSCRIBE_SERVICE", "ONCREATE");
        Subscribe subscribe = new Subscribe(this);
        subscribe.doSubscribe("test", 2);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("ON_START", "onStartCommand()");
        return START_STICKY;
    }
}