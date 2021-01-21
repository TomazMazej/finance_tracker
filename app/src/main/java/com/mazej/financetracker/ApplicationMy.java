package com.mazej.financetracker;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class ApplicationMy extends Application {
    public static final String CHANNEL_ID = "MyNotifications";
    @Override
    public void onCreate() {
        super.onCreate();
        regNotChannel();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void regNotChannel() {
        // Configure the channel
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", importance);
        channel.setDescription("My Notification test");
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);
    }
}