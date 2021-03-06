package org.wearableapp.communications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import org.wearableapp.App;
import org.wearableapp.NotificationActivity;
import org.wearableapp.R;

public abstract class Notify {

    public static void notification(String title, String message) {
        Log.i("NOTIFICATION", "Calling notification.....");

        long when = System.currentTimeMillis();
        String ticker = title + " " + message;
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(App.getContext(), NotificationActivity.class);
        intent.putExtra("notification", message);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(App.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Builder notificationBuilder = new Builder(App.getContext());
        notificationBuilder.setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(message)
                .setTicker(ticker)
                .setWhen(when)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_launcher);

        NotificationManager notificationManager = (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

        Log.i("NOTIFICATION", "Notification sended...");
    }
}
