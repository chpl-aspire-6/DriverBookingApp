package com.adanitownship.driver.utils;

import android.app.NotificationManager;
import android.content.Intent;
import android.media.MediaPlayer;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import rx.android.BuildConfig;

public class VariableBag {


    public static int INFO = 0;
    public static int SUCCESS = 2;
    public static int ERROR = 1;
    public static int WARNING = 3;

    public static String PREF_NAME = "BMS";
    public static String LOGIN = "LOGIN";

    public static String NO_DATA_ICON = "no_data_icon";
    public static boolean WHITE_LABEL_APP = true;




    public static String AUTHORITY = "com.adanimundra.township.droidninja.filepicker.provider";

    public static String MAP_KEY = WHITE_LABEL_APP ? "QUl6YVN5QU5NOGV2eWZMZER4eU9LdWJXcnBJaHMtWGY4bHk4N1hj" : "QUl6YVN5QUx1N2I3UGhDQWNweWVzXzEtWkRLNGUyaHJoQzBjSDFR";


    public static String DEVELOPER_KEY = "QUl6YVN5REVYb1JvWVZzLWxrNy1kN3ZZbmdDT0p0VjhvamR0aW9r";
    public static String SERVER_KEY = "QUFBQXdULTNtVmc6QVBBOTFiRTlMNmRnc2s2WGRNNE9lUEMxdUNhSF9OVlkwNjV1MExPRzdOZ3hqeWRGbEk1NU1CaWZZSUNDNXlnZ0cxZUNnS1BuZ1Z3SFpMR18tUDdxMmNxbGlTMmhhTkRjTUlWd0QxTDRzTG14aDI1MWgtOVM2U1hkRDV1MTYzYW9aUWQ1ZElOdElIdjE=";
    public static String MAIN_KEY = "smartapikey";



    public static String FolderName = "Mundra";



}
