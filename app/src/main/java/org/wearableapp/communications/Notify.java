package org.wearableapp.communications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import org.wearableapp.R;

public class Notify {
    private static int notificationId = 0;

    public static void notification(Context context, String message, Intent intent, String title) {
        Log.i("NOTIFICATION", "Calling notification.....");

        long when = System.currentTimeMillis();
        String ticker = title + " " + message;

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Builder notificationBuilder = new Builder(context);
        notificationBuilder.setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(message)
                .setTicker(ticker)
                .setWhen(when)
                .setSmallIcon(R.drawable.ic_launcher);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notificationBuilder.build());
        notificationId++;

        Log.i("NOTIFICATION", "Notification sended...");
    }


}
