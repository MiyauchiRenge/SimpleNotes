package com.app.youwei.myapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Winky on 2016/8/13.
 */
public class RecycleAlarmReceiver extends BroadcastReceiver {
    NotificationManager manager;
    NotificationCompat.Builder builder;
    RecycleItem new_item;

    Context mcontext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        new_item = (RecycleItem) intent.getSerializableExtra("item");
        String recycle_s = new_item.getRecycle();
        String[] selected_days = recycle_s.split(" ");
        Calendar week= Calendar.getInstance();
        Log.d("week", String.valueOf(week.get(Calendar.DAY_OF_WEEK)));
        int today_week;
        if(week.get(Calendar.DAY_OF_WEEK)==1) {
            today_week = 7;
        } else {
            today_week = week.get(Calendar.DAY_OF_WEEK) - 1;
        }

        for(int i =0; i < selected_days.length; i++) {
            Log.d("selected", selected_days[i]);
            if(selected_days[i].equals(String.valueOf(today_week))) {
                Log.d("selected", "Activited!");
                setNotification();
            }
        }

    }

    private void setNotification() {
        Intent i = new Intent(mcontext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mcontext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        int notification = new_item.getNotification();
        manager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notification == 0) {
            builder = (NotificationCompat.Builder) new NotificationCompat.Builder(mcontext)
                    .setSmallIcon(new item_type().getIcon(new_item.getType()))
                    .setColor(new_item.getColor())
                    .setContentTitle(new_item.getName())
                    .setContentText(new_item.getDetail())
                    .setFullScreenIntent(pendingIntent, false)
                    .setTicker("新的事务提醒!")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(new_item.getDetail()));

        } else if (notification == 1) {

            builder = (NotificationCompat.Builder) new NotificationCompat.Builder(mcontext)
                    .setSmallIcon(new item_type().getIcon(new_item.getType()))
                    .setColor(new_item.getColor())
                    .setContentTitle(new_item.getName())
                    .setContentText(new_item.getDetail())
                    .setTicker("新的事务提醒!")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setFullScreenIntent(pendingIntent, false)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(new_item.getDetail()));

        } else if(notification == 2) {

            builder = (NotificationCompat.Builder) new NotificationCompat.Builder(mcontext)
                    .setSmallIcon(new item_type().getIcon(new_item.getType()))
                    .setColor(new_item.getColor())
                    .setContentTitle(new_item.getName())
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
        manager.notify(3, builder.build());
    }
}
