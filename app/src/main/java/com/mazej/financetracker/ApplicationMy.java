package com.mazej.financetracker;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.UUID;

import timber.log.Timber;

public class ApplicationMy extends Application {
    public static final String TAG = ApplicationMy.class.getSimpleName();
    public static final String CHANNEL_ID = "MyNotifications";
    public static final String APP_ID = "APP_ID_KEY";

    public static SharedPreferences sp;
    public static String currency;
    private String idAPP;
    public static boolean firstTime = false;

    @Override
    public void onCreate() {
        super.onCreate();
        setAppId();
        setCurrency();
        regNotChannel();
        Timber.plant(new Timber.DebugTree());
    }

    public void setAppId() {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sp.contains(APP_ID)) //ce ze obstaja ga preberemo
            idAPP = sp.getString(APP_ID,"DEFAULT VALUE ERR");
        else { //zgeneriramo ga prvic
            idAPP = UUID.randomUUID().toString().replace("-", "");
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(APP_ID, idAPP);
            editor.apply();
            firstTime = true;
        }
        Log.d(TAG,"appID:" + idAPP);
    }

    public void setCurrency() {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sp.contains("currency")) //ce ze obstaja ga preberemo
            currency = sp.getString("currency","DEFAULT VALUE ERR");
        else { //zgeneriramo ga prvic
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("currency", "EUR");
            editor.apply();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void regNotChannel() {
        // Configure the channel for notifications
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", importance);
        channel.setDescription("My Notification test");
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);
    }
}
