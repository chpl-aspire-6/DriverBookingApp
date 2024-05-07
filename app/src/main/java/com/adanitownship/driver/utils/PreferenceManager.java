package com.adanitownship.driver.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;


import java.nio.charset.StandardCharsets;


import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceManager {

    public static PreferenceManager preferenceManager;
    private static SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private Context context;

    @SuppressLint("CommitPrefEdits")
    public PreferenceManager(Context context) {
        preferenceManager = this;
        context = context;
        mSharedPreferences = context.getSharedPreferences(VariableBag.PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

    }

    @SuppressLint("CommitPrefEdits")
    @Inject
    public PreferenceManager() {
        preferenceManager = this;
//        mSharedPreferences = AppLevel.getInstance().getSharedPreferences(VariableBag.PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    public static void setPreferenceManager(PreferenceManager preferenceManager) {
        PreferenceManager.preferenceManager = preferenceManager;
    }

    public String getJSONPref(String key) {
        return getStringPreference(key);
    }


    public static PreferenceManager getInstance() {
        return preferenceManager;
    }


    public void setKeyValueString(String key, String value) {
        setStringPreference(key, value);
    }

    public String getKeyValueString(String key) {
        return getStringPreference(key);

    }
    public void setStringPreference(String key, String v) {
        if (v != null && !v.trim().equals("")) {
            byte[] data = v.getBytes(StandardCharsets.UTF_8);
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            mEditor.putString(key, base64);
            mEditor.commit();
        }

    }
    public static String getStringPreference(String key) {

        if(!mSharedPreferences.getString(key, "").trim().equals("")){
            byte[] data = Base64.decode(mSharedPreferences.getString(key, ""), Base64.DEFAULT);
            return new String(data, StandardCharsets.UTF_8);
        }
        return "";

    }

    public void setLoginSession() {
        mEditor.putBoolean(VariableBag.LOGIN, true);
        mEditor.commit();
    }

    public boolean getLoginSession() {
        return mSharedPreferences.getBoolean(VariableBag.LOGIN, false);
    }

    public void deleteLoginSession() {
        mEditor.putBoolean(VariableBag.LOGIN, false);
        mEditor.commit();
    }



    public String getNoDataIcon() {
        return getStringPreference(VariableBag.NO_DATA_ICON);

    }
    public String getKeyValueStringWithZero(String key) {
        return getStringPreference(key);
    }

    public void setNoDataIcon(String icon) {
        setStringPreference(VariableBag.NO_DATA_ICON, icon);
        // mEditor.putString(VariableBag.NO_DATA_ICON, icon).commit();
    }

    public boolean getNotificationDot() {
        return mSharedPreferences.getBoolean(VariableBag.NOTIFICATION_COUNTER, false);
    }

    public void setNotificationDot(boolean v) {
        mEditor.putBoolean(VariableBag.NOTIFICATION_COUNTER, v);
        mEditor.commit();
    }
}


