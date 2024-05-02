package com.adanitownship.driver;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.adanitownship.driver.network.RestCall;
import com.adanitownship.driver.network.RestClient;
import com.adanitownship.driver.adapter.NotificationAdapter;
import com.adanitownship.driver.networkResponse.CommonResponse;
import com.adanitownship.driver.networkResponse.NotificationResponse;
import com.adanitownship.driver.utils.GzipUtils;
import com.adanitownship.driver.utils.PreferenceManager;
import com.adanitownship.driver.utils.Tools;
import com.adanitownship.driver.utils.VariableBag;
import com.google.gson.Gson;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class NotificationActivity extends AppCompatActivity {
    LinearLayout lin_nodata;
    RecyclerView recyclerView;
    LinearLayout lin_ps_load;
    SwipeRefreshLayout swipe;
    TextView tvTitle, tv_no_Data;
    AppCompatImageView imgBackExtra;
    ImageView imgDelete;
    NotificationAdapter notificationAdapter;
    RestCall restCall;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        restCall = RestClient.createService(RestCall.class, "https://adanidev.mysmartsociety.app/shantivan/residentApiNewEnc/", "smartapikey");
        preferenceManager = new PreferenceManager(this);
        lin_nodata = findViewById(R.id.lin_nodata);
        recyclerView = findViewById(R.id.recycler_notification);
        lin_ps_load = findViewById(R.id.lin_ps_load);
        swipe = findViewById(R.id.swipe);
        tvTitle = findViewById(R.id.tvTitle);
        imgBackExtra = findViewById(R.id.imgBackExtra);
        imgDelete = findViewById(R.id.imgDelete);
        tv_no_Data = findViewById(R.id.tv_no_Data);
        tv_no_Data = findViewById(R.id.tv_no_Data);
        tv_no_Data = findViewById(R.id.tv_no_Data);
        tvTitle.setText("Notification");


        imgBackExtra.setOnClickListener(v -> finish());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getNotificationData();
        swipe.setOnRefreshListener(() -> {
            swipe.setRefreshing(true);
            getNotificationData();
            new Handler(Looper.getMainLooper()).postDelayed(() -> swipe.setRefreshing(false), 2500);
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAllNotifyDialog();
            }
        });
        lin_ps_load.setVisibility(View.VISIBLE);
        lin_nodata.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        try{



        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getNotificationData() {
        restCall.getNotificationData("getNotificationData", preferenceManager.getKeyValueString("driver_id")).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    lin_ps_load.setVisibility(View.GONE);
                    lin_nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    imgDelete.setVisibility(View.GONE);
                    Tools.toast(NotificationActivity.this, "no_internet_connection", VariableBag.ERROR);

                });
            }

            @Override
            public void onNext(String encData) {

                runOnUiThread(() -> {
                    NotificationResponse notificationResponse = null;
                    try {

                        notificationResponse = new Gson().fromJson(GzipUtils.decrypt(encData), NotificationResponse.class);

                        if (notificationResponse != null && notificationResponse.getStatus().equalsIgnoreCase("200")) {

                            lin_nodata.setVisibility(View.GONE);
                            lin_ps_load.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            imgDelete.setVisibility(View.VISIBLE);
                            notificationAdapter = new NotificationAdapter(notificationResponse.getNotification(), NotificationActivity.this);
                            recyclerView.setAdapter(notificationAdapter);

                            notificationAdapter.setOnItemClickListener(new NotificationAdapter.ItemSingleNotifyClickListener() {
                                @Override
                                public void onItemClickListener(NotificationResponse.Notification notification) {

                                     Intent intent = new Intent(NotificationActivity.this , DashBoardActivity.class);
                                     startActivity(intent);
                                }

                                @Override
                                public void onDeleteItemClickListener(NotificationResponse.Notification notification) {
                                    preferenceManager.setKeyValueString("notification_id" , notification.getNotificationId());
                                    showDeleteNotifyDialog();
                                }
                            });
                        } else {
                            lin_nodata.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            lin_ps_load.setVisibility(View.GONE);
//                            Tools.toast(NotificationActivity.this, notificationResponse.getMessage(), 1);

                        }


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                });
            }
        });


    }

    public void deleteNotification() {
        restCall.deleteNotification("deleteNotification", preferenceManager.getKeyValueString("driver_id"), preferenceManager.getKeyValueString("notification_id")).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    lin_ps_load.setVisibility(View.GONE);
                    lin_nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    imgDelete.setVisibility(View.GONE);
                    Tools.toast(NotificationActivity.this, "no_internet_connection", VariableBag.ERROR);

                });
            }

            @Override
            public void onNext(String encData) {
                runOnUiThread(() -> {
                    CommonResponse commonResponse = null;
                    try {
                        commonResponse = new Gson().fromJson(GzipUtils.decrypt(encData), CommonResponse.class);
                        if (commonResponse != null && commonResponse.getStatus().equalsIgnoreCase("200")) {

                            Tools.toast(NotificationActivity.this, commonResponse.getMessage(), 2);


                        } else {
                            Tools.toast(NotificationActivity.this, commonResponse.getMessage(), 1);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                });
            }
        });
    }

    public void deleteAllNotification() {
        restCall.deleteAllNotification("deleteAllNotification", preferenceManager.getKeyValueString("driver_id")).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    lin_ps_load.setVisibility(View.GONE);
                    lin_nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    imgDelete.setVisibility(View.GONE);
                    Tools.toast(NotificationActivity.this, "no_internet_connection", VariableBag.ERROR);

                });
            }

            @Override
            public void onNext(String encData) {
                runOnUiThread(() -> {
                    CommonResponse commonResponse = null;
                    try {
                        commonResponse = new Gson().fromJson(GzipUtils.decrypt(encData), CommonResponse.class);
                        if (commonResponse != null && commonResponse.getStatus().equalsIgnoreCase("200")) {

                            Tools.toast(NotificationActivity.this, commonResponse.getMessage(), 2);


                        } else {
                            Tools.toast(NotificationActivity.this, commonResponse.getMessage(), 1);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                });
            }
        });
    }


    public void showDeleteNotifyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.delete_dialog, null);
        builder.setView(dialogView);

        builder.setPositiveButton("OK", (dialog, which) -> {
            deleteNotification();
           getNotificationData();

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dilaog_bg);
        alertDialog.show();
    }

    public void showDeleteAllNotifyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.delete_dialog, null);
         ImageView img_deleteNotify = dialogView.findViewById(R.id.img_deleteNotify);
         TextView txt_del_msg = dialogView.findViewById(R.id.txt_del_msg);
        img_deleteNotify.setImageResource(R.drawable.ic_all_delete_notify);
        txt_del_msg.setText(R.string.want_to_delete_all_the_notification);
        builder.setView(dialogView);

        builder.setPositiveButton("OK", (dialog, which) -> {
            deleteAllNotification();
         getNotificationData();

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dilaog_bg);
        alertDialog.show();
    }

}