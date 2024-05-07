package com.adanitownship.driver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.adanitownship.driver.network.RestCall;
import com.adanitownship.driver.network.RestClient;
import com.adanitownship.driver.adapter.RequestListAdapter;
import com.adanitownship.driver.networkResponse.BookingRequestListResponse;
import com.adanitownship.driver.networkResponse.CommonResponse;
import com.adanitownship.driver.utils.GzipUtils;
import com.adanitownship.driver.utils.PreferenceManager;
import com.adanitownship.driver.utils.Tools;
import com.adanitownship.driver.utils.VariableBag;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.schedulers.Schedulers;


public class DashBoardActivity extends AppCompatActivity {
    RestCall restCall;
    PreferenceManager preferenceManager;
    SwipeRefreshLayout swipe;
    ImageView imgClose, imgIcon , iv_profile_photo;
    EditText etSearch;
    TextView txt_PersonName , tv_noti_count;
    public static final String CHANNEL_ID = "reminder_channel";
    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 101;


    RelativeLayout rel_nodata;
    LinearLayout lin_ps_load, lin_logout, linLayNoData;
    RecyclerView recy_booking_list;
    RequestListAdapter requestListAdapter;
    Tools tools;
    SwitchCompat switchOnOff;
    int switchStatus;
    FrameLayout notification;
    List<BookingRequestListResponse.Booking> bookingList = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        driverBookingList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dash_board);
        restCall = RestClient.createService(RestCall.class, "https://adanidev.mysmartsociety.app/shantivan/residentApiNewEnc/", "smartapikey");
        preferenceManager = new PreferenceManager(this);
        recy_booking_list = findViewById(R.id.recy_booking_list);
        lin_ps_load = findViewById(R.id.lin_ps_load);
        lin_logout = findViewById(R.id.lin_logout);
        iv_profile_photo = findViewById(R.id.iv_profile_photo);
        linLayNoData = findViewById(R.id.linLayNoData);
        rel_nodata = findViewById(R.id.rel_nodata);
        notification = findViewById(R.id.notification);
        txt_PersonName = findViewById(R.id.txt_PersonName);
        tv_noti_count = findViewById(R.id.tv_noti_count);
        switchOnOff = findViewById(R.id.switchOnOff);
        imgClose = findViewById(R.id.imgClose);
        etSearch = findViewById(R.id.etSearch);
        swipe = findViewById(R.id.swipe);
        tools = new Tools(DashBoardActivity.this);
        lin_ps_load.setVisibility(View.VISIBLE);
        recy_booking_list.setVisibility(View.GONE);
        rel_nodata.setVisibility(View.GONE);
        linLayNoData.setVisibility(View.GONE);
        Tools.displayImage(DashBoardActivity.this, imgIcon, preferenceManager.getNoDataIcon());

        txt_PersonName.setText(preferenceManager.getKeyValueString("driver_name"));
        Glide.with(DashBoardActivity.this)
                        .load(preferenceManager.getKeyValueString("driver_profile"))
                                .placeholder(R.drawable.vector_person)
                                        .into(iv_profile_photo);

        driverBookingList();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION_PERMISSION);
            } else {
                // Permission already granted, create notification channel (if needed)
                createNotificationChannels();
            }
        } else { // Below Android 13
            // Notification permission is always granted on older versions
            createNotificationChannels();
        }
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                driverBookingList();

            }
        });


        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchStatus = 1;//on
                } else {
                    switchStatus = 0;//off
                }
            }
        });


        if (preferenceManager.getNotificationDot()) {
            tv_noti_count.setText(" ");
            tv_noti_count.setVisibility(View.VISIBLE);
        } else {
            tv_noti_count.setVisibility(View.GONE);
        }

        switchOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                driverDutyUpdate();
            }
        });
        imgClose.setOnClickListener(v -> {
            etSearch.getText().clear();
            imgClose.setVisibility(View.GONE);
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                if (requestListAdapter != null && requestListAdapter.getItemCount() > 0) {
                    imgClose.setVisibility(View.VISIBLE);
                    requestListAdapter.search(charSequence, rel_nodata, recy_booking_list);
                }

                if (count < 1) {
                    imgClose.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lin_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutDialog();
            }
        });


    }


    public void acceptbooking() {

        restCall.acceptbooking("acceptbooking", preferenceManager.getKeyValueString("request_id"),
                preferenceManager.getKeyValueString("driver_id"), preferenceManager.getKeyValueString("travel_agent_id"),
                preferenceManager.getKeyValueString("pickup_date"), preferenceManager.getKeyValueString("pickup_time"),
                preferenceManager.getKeyValueString("pickup_location_name"),
                preferenceManager.getKeyValueString("drop_location_name"),
                preferenceManager.getKeyValueString("driver_mobile_no"), preferenceManager.getKeyValueString("driver_name"),
                preferenceManager.getKeyValueString("travel_agent_name"),
                preferenceManager.getKeyValueString("travel_agent_phone_no"),
                preferenceManager.getKeyValueString("society_id"),
                preferenceManager.getKeyValueString("user_id")).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    swipe.setRefreshing(false);
                    lin_ps_load.setVisibility(View.GONE);
                    recy_booking_list.setVisibility(View.GONE);
                    rel_nodata.setVisibility(View.VISIBLE);
                    Tools.toast(DashBoardActivity.this, "no_internet_connection", VariableBag.ERROR);

                });

            }

            @Override
            public void onNext(String encData) {
                runOnUiThread(() -> {
                    swipe.setRefreshing(false);
                    CommonResponse commonResponse = null;
                    try {
                        commonResponse = new Gson().fromJson(GzipUtils.decrypt(encData), CommonResponse.class);
                        if (commonResponse != null && commonResponse.getStatus().equalsIgnoreCase("200")) {
                            requestListAdapter.update(bookingList);
                            Tools.toast(DashBoardActivity.this, commonResponse.getMessage(), 2);

                        } else {
                            Tools.toast(DashBoardActivity.this, commonResponse.getMessage(), 1);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                });

            }
        });

    }

    public void rejectbooking(String reason) {
        restCall.rejectbooking("rejectbooking", preferenceManager.getKeyValueString("request_id"), reason, preferenceManager.getKeyValueString("driver_id"), preferenceManager.getKeyValueString("travel_agent_id")
        ,   preferenceManager.getKeyValueString("pickup_date"), preferenceManager.getKeyValueString("pickup_time"),
                preferenceManager.getKeyValueString("pickup_location_name"),
                preferenceManager.getKeyValueString("drop_location_name"),
                preferenceManager.getKeyValueString("driver_mobile_no"), preferenceManager.getKeyValueString("driver_name"),
                preferenceManager.getKeyValueString("travel_agent_name"),
                preferenceManager.getKeyValueString("travel_agent_phone_no"),preferenceManager.getKeyValueString("society_id"),
                preferenceManager.getKeyValueString("user_id")).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    swipe.setRefreshing(false);
                    lin_ps_load.setVisibility(View.GONE);
                    recy_booking_list.setVisibility(View.GONE);
                    rel_nodata.setVisibility(View.VISIBLE);
                    Tools.toast(DashBoardActivity.this, "no_internet_connection", VariableBag.ERROR);

                });

            }

            @Override
            public void onNext(String encData) {
                runOnUiThread(() -> {
                    swipe.setRefreshing(false);
                    CommonResponse commonResponse = null;
                    try {
                        commonResponse = new Gson().fromJson(GzipUtils.decrypt(encData), CommonResponse.class);
                        if (commonResponse != null && commonResponse.getStatus().equalsIgnoreCase("200")) {
                            requestListAdapter.update(bookingList);
                            Tools.toast(DashBoardActivity.this, commonResponse.getMessage(), 2);


                        } else {
                            Tools.toast(DashBoardActivity.this, commonResponse.getMessage(), 1);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                });

            }
        });

    }


    public void driverPickupUser() {
        restCall.driverPickupUser("driverPickupUser", preferenceManager.getKeyValueString("request_id")).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    swipe.setRefreshing(false);
                    lin_ps_load.setVisibility(View.GONE);
                    recy_booking_list.setVisibility(View.GONE);
                    rel_nodata.setVisibility(View.VISIBLE);
                    Tools.toast(DashBoardActivity.this, "no_internet_connection", VariableBag.ERROR);

                });

            }

            @Override
            public void onNext(String encData) {
                runOnUiThread(() -> {
                    swipe.setRefreshing(false);
                    CommonResponse commonResponse = null;
                    try {
                        commonResponse = new Gson().fromJson(GzipUtils.decrypt(encData), CommonResponse.class);
                        if (commonResponse != null && commonResponse.getStatus().equalsIgnoreCase("200")) {

                            requestListAdapter.update(bookingList);
                            Tools.toast(DashBoardActivity.this, commonResponse.getMessage(), 2);

                        } else {
                            Tools.toast(DashBoardActivity.this, commonResponse.getMessage(), 1);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                });

            }
        });
    }


    public void driverDropUser() {
        restCall.driverDropUser("driverDropUser", preferenceManager.getKeyValueString("request_id")).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    swipe.setRefreshing(false);
                    lin_ps_load.setVisibility(View.GONE);
                    recy_booking_list.setVisibility(View.GONE);
                    rel_nodata.setVisibility(View.VISIBLE);
                    Tools.toast(DashBoardActivity.this, "no_internet_connection", VariableBag.ERROR);

                });

            }

            @Override
            public void onNext(String encData) {
                runOnUiThread(() -> {
                    swipe.setRefreshing(false);
                    CommonResponse commonResponse = null;
                    try {
                        commonResponse = new Gson().fromJson(GzipUtils.decrypt(encData), CommonResponse.class);
                        if (commonResponse != null && commonResponse.getStatus().equalsIgnoreCase("200")) {

                            Tools.toast(DashBoardActivity.this, commonResponse.getMessage(), 2);
                            requestListAdapter.update(bookingList);

                        } else {
                            Tools.toast(DashBoardActivity.this, commonResponse.getMessage(), 1);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                });

            }
        });
    }


    public void driverBookingList() {
        restCall.driverBookingList("driverBookingList", preferenceManager.getKeyValueString("driver_id")).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        runOnUiThread(() -> {
                            swipe.setRefreshing(false);
                            lin_ps_load.setVisibility(View.GONE);
                            recy_booking_list.setVisibility(View.GONE);
                            rel_nodata.setVisibility(View.GONE);
                            linLayNoData.setVisibility(View.VISIBLE);
                            Tools.toast(DashBoardActivity.this, "no_internet_connection", VariableBag.ERROR);

                        });

                    }
                });
            }

            @Override
            public void onNext(String encData) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                        BookingRequestListResponse bookingRequestListResponse = null;
                        try {
                            bookingRequestListResponse = new Gson().fromJson(GzipUtils.decrypt(encData), BookingRequestListResponse.class);
                            if (bookingRequestListResponse != null && bookingRequestListResponse.getStatus().equalsIgnoreCase("200") && bookingRequestListResponse.getBookingList().size() > 0) {

                                lin_ps_load.setVisibility(View.GONE);
                                recy_booking_list.setVisibility(View.VISIBLE);
                                rel_nodata.setVisibility(View.GONE);
                                linLayNoData.setVisibility(View.GONE);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DashBoardActivity.this, LinearLayoutManager.VERTICAL, false);
                                recy_booking_list.setLayoutManager(layoutManager);
                                requestListAdapter = new RequestListAdapter(bookingRequestListResponse.getBookingList(), DashBoardActivity.this);
                                recy_booking_list.setAdapter(requestListAdapter);
                                requestListAdapter.setOnItemClickListener(new RequestListAdapter.ItemSingleClickListener() {
                                    @Override
                                    public void onAcceptItemClickListener(BookingRequestListResponse.Booking booking) {
                                        if (booking.getDutyStatus().equalsIgnoreCase("1")) {
                                            preferenceManager.setKeyValueString("request_id", booking.getRequestId());
                                            preferenceManager.setKeyValueString("pickup_date", booking.getPickupDate());
                                            preferenceManager.setKeyValueString("pickup_time", booking.getPickupTime());
                                            preferenceManager.setKeyValueString("pickup_location_name", booking.getPickupLocationName());
                                            preferenceManager.setKeyValueString("drop_location_name", booking.getDropLocationName());
                                            preferenceManager.setKeyValueString("driver_mobile_no", booking.getDriverMobileNo());
                                            preferenceManager.setKeyValueString("driver_name", booking.getDriverName());
                                            preferenceManager.setKeyValueString("travel_agent_name", booking.getTravelAgentName());
                                            preferenceManager.setKeyValueString("travel_agent_phone_no", booking.getTravelAgentPhoneNo());
                                            preferenceManager.setKeyValueString("society_id", booking.getSocietyId());
                                            preferenceManager.setKeyValueString("user_id", booking.getUserId());
                                            showAcceptDialog();
                                        } else {

                                            Tools.toast(DashBoardActivity.this, "You are off Duty right now!!", 1);
                                        }

                                    }

                                    @Override
                                    public void onRejectItemClickListener(BookingRequestListResponse.Booking booking) {
                                        if (booking.getDutyStatus().equalsIgnoreCase("1")) {
                                            preferenceManager.setKeyValueString("request_id", booking.getRequestId());
                                            preferenceManager.setKeyValueString("pickup_date", booking.getPickupDate());
                                            preferenceManager.setKeyValueString("pickup_time", booking.getPickupTime());
                                            preferenceManager.setKeyValueString("pickup_location_name", booking.getPickupLocationName());
                                            preferenceManager.setKeyValueString("drop_location_name", booking.getDropLocationName());
                                            preferenceManager.setKeyValueString("driver_mobile_no", booking.getDriverMobileNo());
                                            preferenceManager.setKeyValueString("driver_name", booking.getDriverName());
                                            preferenceManager.setKeyValueString("travel_agent_name", booking.getTravelAgentName());
                                            preferenceManager.setKeyValueString("travel_agent_phone_no", booking.getTravelAgentPhoneNo());
                                            preferenceManager.setKeyValueString("society_id", booking.getSocietyId());
                                            preferenceManager.setKeyValueString("user_id", booking.getUserId());
                                            showRejectReasonDialog();
                                        } else {
                                            Tools.toast(DashBoardActivity.this, "You are off Duty right now!!", 1);
                                        }
                                    }

                                    @Override
                                    public void onPickUpItemClickListener(BookingRequestListResponse.Booking booking) {
                                        showPickUpConfrimstionDialog();

                                    }

                                    @Override
                                    public void onDropItemClickListener(BookingRequestListResponse.Booking booking) {

                                        driverDropUser();
                                    }
                                });

                                bookingList = bookingRequestListResponse.getBookingList();

                                // Set switch to "Off" otherwise
                                switchOnOff.setChecked(bookingList.get(0).getDutyStatus().equalsIgnoreCase("1"));  // Set switch to "On" if dutyStatus is "1"


                            } else {
                                lin_ps_load.setVisibility(View.GONE);
                                recy_booking_list.setVisibility(View.VISIBLE);
                                rel_nodata.setVisibility(View.GONE);
                                linLayNoData.setVisibility(View.VISIBLE);
//                                Tools.toast(DashBoardActivity.this, bookingRequestListResponse.getMessage(), 1);

                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

            }
        });
    }

    public void logoutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout Confirmation");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferenceManager.deleteLoginSession();
                finishAffinity();
                Tools.toast(DashBoardActivity.this, "LogOut Successfully", 2);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAcceptDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        final ImageView img_icon = dialogView.findViewById(R.id.img_icon);
        final TextView txtHeading = dialogView.findViewById(R.id.txtHeading);
        final EditText editText_reason = dialogView.findViewById(R.id.editText_reason);
        img_icon.setImageResource(R.drawable.ic_pick_confirmation);
        txtHeading.setText(R.string.are_you_sure_to_accept_this_ride);
        editText_reason.setVisibility(View.GONE);
        builder.setView(dialogView);

        builder.setPositiveButton("OK", (dialog, which) -> {
            acceptbooking();
            requestListAdapter.update(bookingList);
            driverBookingList();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dilaog_bg);
        alertDialog.show();

    }


    public void showRejectReasonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        final EditText input = dialogView.findViewById(R.id.editText_reason);
        builder.setView(dialogView);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String reason = input.getText().toString();
            if (!TextUtils.isEmpty(reason)) {
                rejectbooking(reason);
                requestListAdapter.update(bookingList);
                driverBookingList();
            } else {
                input.setError("Please provide a reason for rejecting the trip");
                input.requestFocus();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dilaog_bg);
        alertDialog.show();


    }


    public void showPickUpConfrimstionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        final ImageView img_icon = dialogView.findViewById(R.id.img_icon);
        final TextView txtHeading = dialogView.findViewById(R.id.txtHeading);
        final EditText editText_reason = dialogView.findViewById(R.id.editText_reason);
        img_icon.setImageResource(R.drawable.ic_pickup);
        txtHeading.setText("Are you sure for Pick Up");
        editText_reason.setVisibility(View.GONE);
        builder.setView(dialogView);

        builder.setPositiveButton("OK", (dialog, which) -> {
            driverPickupUser();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dilaog_bg);
        alertDialog.show();


    }


    public void driverDutyUpdate() {
        restCall.driverDutyUpdate("driverDutyUpdate", preferenceManager.getKeyValueString("driver_id"), switchStatus).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        runOnUiThread(() -> {
                            swipe.setRefreshing(false);
                            lin_ps_load.setVisibility(View.GONE);
                            recy_booking_list.setVisibility(View.GONE);
                            rel_nodata.setVisibility(View.VISIBLE);
                            Tools.toast(DashBoardActivity.this, "no_internet_connection", VariableBag.ERROR);
                        });

                    }
                });
            }

            @Override
            public void onNext(String encData) {
                runOnUiThread(() -> {
                    CommonResponse commonResponse = null;
                    try {
                        commonResponse = new Gson().fromJson(GzipUtils.decrypt(encData), CommonResponse.class);
                        if (commonResponse != null && commonResponse.getStatus().equalsIgnoreCase("200")) {
                            Tools.toast(DashBoardActivity.this, commonResponse.getMessage(), 2);
                            requestListAdapter.update(bookingList);
                        } else {
                            Tools.toast(DashBoardActivity.this, commonResponse.getMessage(), 1);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                });

            }
        });
    }



    private void showPermissionRationaleDialog() {
        // Build a dialog explaining why the permission is needed
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app needs notification permission to show important updates and alerts. Grant permission to enable notifications?");
        builder.setPositiveButton("Grant Permission", (dialog, which) -> {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION_PERMISSION);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void openNotificationSettings() {
        // Open the app settings page where the user can enable notification permission
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        // Verify if it resolves to an activity before starting the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Handle case where there is no matching activity (unlikely)
            Toast.makeText(this, "Unable to open Notification Settings", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"App notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("App  notifications appear here");
            channel.setSound(null,null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, create notification channel
                createNotificationChannels();
            } else {
                // Permission denied, explain why it's needed and offer to go to settings
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                    // Show a rationale dialog explaining why the permission is needed
                    showPermissionRationaleDialog();
                } else {
                    // User denied and checked "Don't ask again"
                    openNotificationSettings();
                }
            }
        }
    }
}