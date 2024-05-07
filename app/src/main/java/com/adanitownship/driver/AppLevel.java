package com.adanitownship.driver;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

public class AppLevel extends MultiDexApplication {

    @SuppressLint("StaticFieldLeak")
    public static volatile Context applicationContext;
    private FirebaseAnalytics mFirebaseAnalytics;

    private static AppLevel instance;


    public AppLevel() {
        super();
    }
    public static AppLevel getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        try {
            applicationContext = getApplicationContext();
            System.gc();
        } catch (Throwable ignore) {

        }
        super.onCreate();
        instance = this;
        if (applicationContext == null) {
            applicationContext = getApplicationContext();
        }
        MultiDex.install(this);
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }



}
