package com.example.project_time;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;


public class alarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "my_channel_id"; // 通知渠道ID
    private static final int NOTIFICATION_ID = 0;    //    建立notificationManager與notification物件
    private NotificationManager notificationManager;
    private Notification notification;

    //    建立能辨識通知差別的ID


    @Override
    public void onReceive(Context context, Intent intent) {

        //        實作觸發通知訊息，開啟首頁動作
        Intent notifyIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);

        //        執行通知
        broadcastNotify(context, pendingIntent);
    }

    //     建立通知方法
    private void broadcastNotify(Context context, PendingIntent pendingIntent) {
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //        建立通知物件內容
        notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("訊息")
                .setContentText("要吃藥3囉!")
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{0, 100, 200, 300, 400, 500})
                .build();

        //        發送通知
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "My Channel"; // 通知渠道名称
            String channelDescription = "Channel Description"; // 通知渠道描述
            int importance = NotificationManager.IMPORTANCE_DEFAULT; // 通知重要性级别

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}