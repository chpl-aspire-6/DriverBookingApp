package com.adanitownship.driver;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.adanitownship.driver.utils.Delegate;
import com.adanitownship.driver.utils.PreferenceManager;
import com.adanitownship.driver.utils.Tools;
import com.adanitownship.driver.utils.VariableBag;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.List;
import java.util.Random;

public class FirebaseDataReceiver extends BroadcastReceiver {

    NotificationManager notificationManager;
    Bitmap bitmap = null;
    Bitmap bitmapSmall = null;
    String TAG = "FirebaseDataReceiver";
    private PreferenceManager preferenceManager;
    Vibrator vibe;

    public void onReceive(final Context context, Intent intent) {
        String var2 = intent.getAction();


        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        preferenceManager = new PreferenceManager(context);
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        System.gc();

        if ("com.google.android.c2dm.intent.RECEIVE".equals(var2)) {

            Bundle var20;

            if ((var20 = intent.getExtras()) == null) {
                var20 = new Bundle();
            }

            var20.remove("android.support.content.wakelockid");
            RemoteMessage remoteMessage = new RemoteMessage(var20);


            if (preferenceManager.getLoginSession()) {
                if (remoteMessage.getData().containsKey("msg_id") && remoteMessage.getData().get("msg_id") != null) {
                    try {

                        Long msgId = Long.parseLong(remoteMessage.getData().get("msg_id"));
                        Long lastIntId = Long.valueOf(preferenceManager.getKeyValueStringWithZero(VariableBag.LAST_NOTIFICATION_ID));

                        Log.e(TAG, "timeNew: " + msgId);
                        Log.e(TAG, "timeLast: " + lastIntId);


                        if (lastIntId < msgId) {

                            preferenceManager.setKeyValueString(VariableBag.LAST_NOTIFICATION_ID, remoteMessage.getData().get("msg_id"));
                            nextStep(context, remoteMessage);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "onReceive: " + e.getLocalizedMessage());
                        nextStep(context, remoteMessage);
                    }

                } else {
                    nextStep(context, remoteMessage);
                }
            }

        } else {
            Log.e(TAG, "onReceive: ");

        }


    }

    private void nextStep(Context context, RemoteMessage remoteMessage) {

        final String title = remoteMessage.getData().get("title");
        final String message = remoteMessage.getData().get("body");
        final String clickAction = remoteMessage.getData().get("click_action");
        String image = remoteMessage.getData().get("image");
        String small_icon = remoteMessage.getData().get("small_icon");
        if (image != null && image.length() > 5) {
            if (image.contains("logo.png")) {
                bitmap = null;
                image = null;

                sendNotification(context, title, message, image);
            } else {
                getBitmapFromUrl(context, title, message, clickAction, image, remoteMessage, true);
            }
        } else if (small_icon != null && small_icon.length() > 5) {
            getBitmapFromUrl(context, title, message, clickAction,  small_icon, remoteMessage, false);
        } else {
            sendNotification(context, title, message, image);
        }


    }

    private void sendNotification(Context context, String title, String message , String image
//                                  String clickAction, String image, String menuClick
    ) {
        JSONObject mainObject = null;

        if (title != null && title.length() > 0 && !title.equalsIgnoreCase("sos")) {

            try {

                int NOTIFICATION_COLOR = context.getResources().getColor(R.color.colorPrimary);
                long[] VIBRATE_PATTERN = {0, 500};


                String channelId = "channel-" + 01;
                String channelName = "Channel Name";
                int importance = 0;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    importance = NotificationManager.IMPORTANCE_HIGH;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(
                            channelId, channelName, importance);

                    mChannel.setShowBadge(true);
                    mChannel.setSound(null, null);
                    mChannel.setDescription(title);
                    mChannel.setLightColor(NOTIFICATION_COLOR);
                    mChannel.setVibrationPattern(VIBRATE_PATTERN);
                    mChannel.enableVibration(true);

                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(mChannel);
                    }
                }
                Intent intent;
                intent = new Intent(context, DashBoardActivity.class);

//                if (menuClick.equalsIgnoreCase("custom_notification")) {
//                    intent = new Intent(context, DashBoardActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    if (clickAction != null && clickAction.length() > 0) {
//                        mainObject = new JSONObject(clickAction);
//                        intent.putExtra("img", mainObject.getString("img_url"));
//                        intent.putExtra("title", mainObject.getString("title"));
//                        intent.putExtra("desc", mainObject.getString("description"));
//                        intent.putExtra("time", mainObject.getString("notification_time"));
//                    }
//                }
//                else {
//                    intent = new Intent(context, DashBoardActivity.class);
//                    intent.putExtra("isFromFCM", true);
//
//                }

                PendingIntent pendingIntent = PendingIntent.getActivity(context, getRandomNumber(1, 10000), intent,
                        PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

                //pendingIntent.

                if (bitmap != null) {

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSound(null)
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                            .setLights(Color.RED, 1000, 1000)
                            .setVibrate(new long[]{0, 400, 250, 400})
                            .setAutoCancel(true)
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                            .setContentIntent(pendingIntent);


                    if (notificationManager != null) {

                        notificationManager.notify(getRandomNumber(1, 10000), mBuilder.build());

                    }

                } else if (bitmapSmall != null) {

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                            .setLargeIcon(bitmapSmall)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setSound(null)
                            .setContentText(message)
                            .setLights(Color.RED, 1000, 1000)
                            .setVibrate(new long[]{0, 400, 250, 400})
                            .setAutoCancel(true)
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                            .setContentIntent(pendingIntent);

                    if (message.length() > 40) {
                        mBuilder.setStyle(bigTextStyle);
                    }


                    if (notificationManager != null) {

                        notificationManager.notify(getRandomNumber(1, 10000), mBuilder.build());

                    }

                } else {

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_driver_logo))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSound(null)
                            .setLights(Color.RED, 1000, 1000)
                            .setVibrate(new long[]{0, 400, 250, 400})
                            .setAutoCancel(true)
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                            .setContentIntent(pendingIntent);

                    if (message.length() > 40) {
                        mBuilder.setStyle(bigTextStyle);
                    }


                    if (notificationManager != null) {

                        notificationManager.notify(getRandomNumber(1, 10000), mBuilder.build());


                    }

                }
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                if (am != null && getNotificationPermission(context)) {

                    switch (am.getRingerMode()) {
                        case AudioManager.RINGER_MODE_SILENT:
                            Log.i("MyApp", "Silent mode");
                            break;
                        case AudioManager.RINGER_MODE_VIBRATE:
                            Log.i("MyApp", "Vibrate mode");
                            vibe.vibrate(30);

                            break;
                        case AudioManager.RINGER_MODE_NORMAL:
                            Log.i("MyApp", "Normal mode");

//                            try {
//                                playNotificationSound(context, Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                                        + "://" + context.getPackageName() + "/raw/just_saying"));
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }


                            break;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public void getBitmapFromUrl(Context context, String title, String message, String clickAction,
                                 String imageUrl, RemoteMessage remoteMessage, boolean isBigImg) {


        new Thread(() -> {
            try {

                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap bitmapNew = BitmapFactory.decodeStream(input);
                    if (isBigImg) {
                        bitmap = bitmapNew;
                    } else {
                        bitmapSmall = bitmapNew;
                    }

                    sendNotification(context, title, message, imageUrl);


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    if (isBigImg) {
                        bitmap = null;
                    } else {
                        bitmapSmall = null;
                    }
                    sendNotification(context, title, message, imageUrl);
//                    sendNotification(context, title, message, clickAction, society_id, imageUrl, menuClick);

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }























   /* private void sendNotification(Context context, String title, String message, String clickAction, String society_id, String image, String menuClick) {
        JSONObject mainObject = null;
        Tools.log(TAG, "I'm in!!! sendNotification");

        if (VariableBag.mediaPlayer != null) {
            VariableBag.mediaPlayer.stop();
        }
        if (title != null && title.length() > 0 && !title.equalsIgnoreCase("sos")) {

            try {

                int NOTIFICATION_COLOR = ContextCompat.getColor(context,R.color.colorPrimary);
//                        context.getResources().getColor(R.color.colorPrimary);
                long[] VIBRATE_PATTERN = {0, 500};


                String channelId = "channel-" + 01;
                String channelName = "Channel Name";
                int importance = 0;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    importance = NotificationManager.IMPORTANCE_HIGH;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(
                            channelId, channelName, importance);

                    mChannel.setShowBadge(true);
                    mChannel.setSound(null, null);
                    mChannel.setDescription(title);
                    mChannel.setLightColor(NOTIFICATION_COLOR);
                    mChannel.setVibrationPattern(VIBRATE_PATTERN);
                    mChannel.enableVibration(true);

                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(mChannel);
                    }
                }
                Intent intent;






                PendingIntent pendingIntent;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    pendingIntent = PendingIntent.getActivity(context, getRandomNumber(1, 10000), intent,
                            PendingIntent.FLAG_IMMUTABLE);
                }else {
                    pendingIntent = PendingIntent.getActivity(context, getRandomNumber(1, 10000), intent,
                            PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                }

                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();

                //pendingIntent.

                if (bitmap != null) {

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                            .setSmallIcon(R.drawable.ic_driver_logo)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSound(null)
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                            .setLights(Color.RED, 1000, 1000)
                            .setVibrate(new long[]{0, 400, 250, 400})
                            .setAutoCancel(true)
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                            .setContentIntent(pendingIntent);


                    if (notificationManager != null) {

                        notificationManager.notify(getRandomNumber(1, 10000), mBuilder.build());

                    }

                } else if (bitmapSmall != null) {

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                            .setLargeIcon(bitmapSmall)
                            .setSmallIcon(R.drawable.ic_driver_logo)
                            .setContentTitle(title)
                            .setSound(null)
                            .setContentText(message)
                            .setLights(Color.RED, 1000, 1000)
                            .setVibrate(new long[]{0, 400, 250, 400})
                            .setAutoCancel(true)
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                            .setContentIntent(pendingIntent);

                    if (message.length() > 40) {
                        mBuilder.setStyle(bigTextStyle);
                    }


                    if (notificationManager != null) {

                        notificationManager.notify(getRandomNumber(1, 10000), mBuilder.build());

                    }

                } else {

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_driver_logo))
                            .setSmallIcon(R.drawable.ic_driver_logo)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSound(null)
                            .setLights(Color.RED, 1000, 1000)
                            .setVibrate(new long[]{0, 400, 250, 400})
                            .setAutoCancel(true)
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                            .setContentIntent(pendingIntent);

                    if (message.length() > 40) {
                        mBuilder.setStyle(bigTextStyle);
                    }


                    if (notificationManager != null) {

                        notificationManager.notify(getRandomNumber(1, 10000), mBuilder.build());


                    }

                }
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                if (am != null && getNotificationPermission()) {

                    switch (am.getRingerMode()) {
                        case AudioManager.RINGER_MODE_SILENT:
                            break;
                        case AudioManager.RINGER_MODE_VIBRATE:
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibe.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                vibe.vibrate(30);
                            }

                            break;
                        case AudioManager.RINGER_MODE_NORMAL:
                            if (preferenceManager.getNotificationSoundSetting()) {
                                try {
                                    if (preferenceManager.getRingtoneNotification() != null && preferenceManager.getRingtoneNotification().toString().length() > 2) {
                                        playNotificationSoundCheck(context, preferenceManager.getRingtoneNotification(), false, false, false);
                                    } else {
                                        playNotificationSoundCheck(context, Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                                + "://" + context.getPackageName() + "/raw/just_saying"), false, false, false);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            break;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public void getBitmapFromUrl(Context context, String title, String message, String clickAction,
                                 String society_id, String imageUrl, String menuClick, RemoteMessage remoteMessage, boolean isBigImg) {


        new Thread(() -> {
            try {

                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap bitmapNew = BitmapFactory.decodeStream(input);
                    if (isBigImg) {
                        bitmap = bitmapNew;
                    } else {
                        bitmapSmall = bitmapNew;
                    }

                    checkConditions(context, title, message, clickAction, society_id, imageUrl, menuClick, remoteMessage);


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    if (isBigImg) {
                        bitmap = null;
                    } else {
                        bitmapSmall = null;
                    }

                    checkConditions(context, title, message, clickAction, society_id, imageUrl, menuClick, remoteMessage);

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }*/

    public int getRandomNumber(int min, int max) {
        // min (inclusive) and max (exclusive)
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }


    static
    class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean getNotificationPermission(Context context) {

        if (NotificationManagerCompat.from(AppLevel.getInstance()).areNotificationsEnabled()) {
            //Do your Work
            Tools.log(TAG, "Have Notification access");

            return true;
        } else {
            Tools.log(TAG, "Don't Have Notification access");

            return false;

        }

    }


    public void playSound(Context context, Uri alarmSound){

        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmSound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone.setLooping(false);
        }
        ringtone.play();
        Delegate.ringtone = ringtone;
    }

}
