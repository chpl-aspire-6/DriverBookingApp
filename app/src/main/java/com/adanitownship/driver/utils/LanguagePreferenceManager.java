package com.adanitownship.driver.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.adanitownship.driver.AppLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LanguagePreferenceManager {

    public static LanguagePreferenceManager preferenceManager;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    public LanguagePreferenceManager(Context context) {
        preferenceManager = this;
        mSharedPreferences = context.getSharedPreferences(VariableBag.PREF_NAME_LANG, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    @SuppressLint("CommitPrefEdits")
    @Inject
    public LanguagePreferenceManager() {
        preferenceManager = this;
        mSharedPreferences = AppLevel.getInstance().getSharedPreferences(VariableBag.PREF_NAME_LANG, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }


    public SharedPreferences.Editor getmEditor() {
        return mEditor;
    }

    public void setmEditor(SharedPreferences.Editor mEditor) {
        this.mEditor = mEditor;
    }

    public void clearPreferences() {
        mEditor.clear();
        mEditor.commit();
        //deleteLoginSession();
    }

    /*set preference for registration*/

    public void removePref(Context context, String keyToRemove) {
        mSharedPreferences = context.getSharedPreferences(VariableBag.PREF_NAME_LANG, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.remove(keyToRemove);
        mEditor.apply();
    }

    public void setJSONPref(String key, String json) {
        mEditor.putString(key, json).commit();
    }

    public String getJSONPref(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public JSONObject getJSONObject(String key) {
        try {

            String dataStr = mSharedPreferences.getString(key, "");

            return new JSONObject(dataStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONArray getJSONArray(String key) {
        try {
            return new JSONArray(mSharedPreferences.getString(key, ""));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getJSONKeyStringObject(String stringKey) {
        JSONObject obj = getJSONObject(VariableBag.LANGUAGE);
        if (stringKey != null && obj !=null) {
            try {
                return obj.has(stringKey) ? obj.getString(stringKey) : "";
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public JSONArray getJSONKeyArrayObject(String stringKey) {
        JSONObject obj = getJSONObject(VariableBag.LANGUAGE);
        if (stringKey != null && obj!=null) {
            try {
                return obj.getJSONArray(stringKey);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }


    public String getLanguageId() {
        return mSharedPreferences.getString(VariableBag.LANGUAGE_ID ,"1");
    }

    public void setLanguageId(String s) {
        mEditor.putString(VariableBag.LANGUAGE_ID, s).commit();

    }


    public int getVersionLanguageCode() {
        return mSharedPreferences.getInt(VariableBag.VERSION_CODE_LANGUAGE, 0);
    }

    public void setVersionLanguageCode(int value) {
        mEditor.putInt(VariableBag.VERSION_CODE_LANGUAGE, value).commit();
    }

    public void setKeyValueString(String key, String value) {
        mEditor.putString(key, value).commit();
    }

    public String getKeyValueString(String key) {
        String getValue = mSharedPreferences.getString(key, "");
        return getValue;
    }
}


