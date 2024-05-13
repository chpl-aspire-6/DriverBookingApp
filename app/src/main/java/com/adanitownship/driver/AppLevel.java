package com.adanitownship.driver;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.adanitownship.driver.utils.PreferenceManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

public class AppLevel extends MultiDexApplication {

    @SuppressLint("StaticFieldLeak")
    public static volatile Context applicationContext;
    private FirebaseAnalytics mFirebaseAnalytics;

    private static AppLevel instance;
    PreferenceManager preferenceManager;

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
            preferenceManager = new PreferenceManager(this);
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


    public void logOutFromApp(){
//        Intent intent = new Intent(instance, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        preferenceManager.deleteLoginSession();
//        startActivity(intent);



        Intent intent = new Intent(instance, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        preferenceManager.deleteLoginSession();
        instance.startActivity(intent);
    }
}
