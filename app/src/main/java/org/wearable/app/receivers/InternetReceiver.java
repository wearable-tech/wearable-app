package org.wearable.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.wearable.app.communications.Publish;
import org.wearable.app.communications.Subscribe;

public class InternetReceiver extends BroadcastReceiver {
    private boolean hasMmobile = false;
    private boolean hasWifi= false;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infos[] = connectivityManager.getAllNetworkInfo();

        for (int i = 0; i < infos.length; i++){
            if (infos[i].getTypeName().equalsIgnoreCase("MOBILE")){
                this.hasMmobile = infos[i].isConnected();
                Log.i("MOBILE", infos[i].getTypeName() + " is " + infos[i].isConnected());
            }
            else if ( infos[i].getTypeName().equalsIgnoreCase("WIFI") ) {
                this.hasWifi = infos[i].isConnected();
                Log.i("WIFI", infos[i].getTypeName() + " is " + infos[i].isConnected());
            }
        }

        boolean connectivity = hasMmobile || hasWifi;

        if (connectivity) {
            Log.i("CONNECTIVITY", "Internet connected");

            Subscribe subscribe = new Subscribe();
            subscribe.doSubscribe("test", 2);

            Publish publish = new Publish();
            publish.doPublish("test", "Send Android Message", 2);
        }
    }
}
