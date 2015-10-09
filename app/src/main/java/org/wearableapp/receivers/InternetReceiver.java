package org.wearableapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.wearableapp.communications.Connection;
import org.wearableapp.communications.Subscribe;

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
            Connection.enableConnection();
        }
        else {
            Log.i("CONNECTIVITY", "Internet disconnected");
            Connection.disableConnection();
        }
    }
}
