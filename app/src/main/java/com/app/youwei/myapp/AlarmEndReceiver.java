package com.app.youwei.myapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Winky on 2016/8/9.
 */
public class AlarmEndReceiver extends BroadcastReceiver {
    NotificationManager manager;
    NotificationCompat.Builder builder;
    Item new_item;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("AlarmEnd","Actived!");
        new_item = intent.getParcelableExtra("item");
        int notification = new_item.getNotification();
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notification == 0) {
            builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setSmallIcon(new item_type().getIcon(new_item.getType()))
                    .setColor(new_item.getColor())
                    .setContentTitle(new_item.getName()+" 结束了!")
                    .setContentText(new_item.getDetail())
                    .setFullScreenIntent(pendingIntent, false)
                    .setTicker("新的事务提醒!")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(new_item.getDetail()));

        } else if (notification == 1) {

            builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setSmallIcon(new item_type().getIcon(new_item.getType()))
                    .setColor(new_item.getColor())
                    .setContentTitle(new_item.getName()+" 结束了!")
                    .setContentText(new_item.getDetail())
                    .setFullScreenIntent(pendingIntent, false)
                    .setTicker("新的事务提醒!")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(new_item.getDetail()));

        } else if(notification == 2) {

            builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setSmallIcon(new item_type().getIcon(new_item.getType()))
                    .setColor(new_item.getColor())
                    .setContentTitle(new_item.getName()+" 结束了!")
                    .setContentText(new_item.getDetail())
                    .setFullScreenIntent(pendingIntent, false)
                    .setTicker("新的事务提醒!")
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(new_item.getDetail()));
        }
        manager.notify(2, builder.build());
    }
}
