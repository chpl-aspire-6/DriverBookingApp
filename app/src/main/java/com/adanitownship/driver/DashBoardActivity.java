package com.adanitownship.driver;

import android.Manifest;
import android.app.Dialog;
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
import android.util.Log;
import android.view.View;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.adanitownship.driver.adapter.RequestListAdapter;
import com.adanitownship.driver.language.ChooseLanguageActivity;
import com.adanitownship.driver.network.RestCall;
import com.adanitownship.driver.network.RestClient;
import com.adanitownship.driver.networkResponse.BookingRequestListResponse;
import com.adanitownship.driver.networkResponse.CommonResponse;
import com.adanitownship.driver.networkResponse.DriverDutyStatusResponse;
import com.adanitownship.driver.networkResponse.DropUserResponse;
import com.adanitownship.driver.utils.FincasysDialog;
import com.adanitownship.driver.utils.GzipUtils;
import com.adanitownship.driver.utils.LanguagePreferenceManager;
import com.adanitownship.driver.utils.PreferenceManager;
import com.adanitownship.driver.utils.Tools;
import com.adanitownship.driver.utils.VariableBag;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DashBoardActivity extends AppCompatActivity {
    private static final int REQUEST_CALL_PERMISSION = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 102;

    public LanguagePreferenceManager languagePreferenceManager;
    RestCall restCall;
    PreferenceManager preferenceManager;
    SwipeRefreshLayout swipe;
    ImageView imgClose, imgIcon, iv_profile_photo, iv_language;
    EditText etSearch;
    TextView txt_PersonName, tv_noti_count, txttagRide;
    String pendingPhoneNumber, dropLatitude, dropLongitude, pickupLatitude, pickupLongitude;
    RelativeLayout rel_nodata;
    LinearLayout lin_ps_load, lin_logout, linLayNoData, lin_layout;
    RecyclerView recy_booking_list;
    RequestListAdapter requestListAdapter;
    Tools tools;
    SwitchCompat switchOnOff;
    int switchStatus;
    String switchDutyStatus;
    FrameLayout notification;
    List<BookingRequestListResponse.Booking> bookingList = new ArrayList<>();
    boolean isFirstTime = true;


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
        languagePreferenceManager = AppLevel.getInstance().getLanPrefLang();
        recy_booking_list = findViewById(R.id.recy_booking_list);
        lin_ps_load = findViewById(R.id.lin_ps_load);
        lin_logout = findViewById(R.id.lin_logout);
        lin_layout = findViewById(R.id.lin_layout);
        iv_profile_photo = findViewById(R.id.iv_profile_photo);
        linLayNoData = findViewById(R.id.linLayNoData);
        iv_language = findViewById(R.id.iv_language);
        rel_nodata = findViewById(R.id.rel_nodata);
        notification = findViewById(R.id.notification);
        txt_PersonName = findViewById(R.id.txt_PersonName);
        txttagRide = findViewById(R.id.txttagRide);
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
        tv_noti_count.setVisibility(View.GONE);
        Tools.displayImage(DashBoardActivity.this, imgIcon, preferenceManager.getNoDataIcon());

        txt_PersonName.setText(preferenceManager.getKeyValueString("driver_name"));
        Glide.with(DashBoardActivity.this).load(preferenceManager.getKeyValueString("driver_profile")).placeholder(R.drawable.vector_person).into(iv_profile_photo);
        downloadLanguage();
        driverDutyStatus();
        driverBookingList();
        requestPermissions();
        setData();
        switchOnOff.setChecked(Boolean.parseBoolean(switchDutyStatus));

        String language = languagePreferenceManager.getJSONPref(VariableBag.LANGUAGE);
        Log.e("###tps", languagePreferenceManager.getJSONPref(VariableBag.LANGUAGE));
        if (language != null && language.trim().length() <= 0) {
            downloadLanguage();
        }

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
        iv_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, ChooseLanguageActivity.class);
                intent.putExtra("isFromSetting", true);
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
//        switchOnOff.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                switchStatus = 1;//on
//
//            } else {
//                switchStatus = 0;//off
//            }
//            driverDutyUpdate();
//        });
//

        switchOnOff.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isFirstTime) {
                if (isChecked) {
                    switchStatus = 1; // on
                } else {
                    switchStatus = 0; // off
                }
                driverDutyUpdate();
            } else {
                isFirstTime = false;
            }
        });
        if (preferenceManager.getNotificationDot()) {
            tv_noti_count.setText(" ");
            tv_noti_count.setVisibility(View.VISIBLE);
        } else {
            tv_noti_count.setVisibility(View.GONE);
        }
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
    }

    public void acceptbooking() {

        restCall.acceptbooking("acceptbooking", preferenceManager.getKeyValueString("request_id"), preferenceManager.getKeyValueString("driver_id"), preferenceManager.getKeyValueString("travel_agent_id"), preferenceManager.getKeyValueString("pickup_date"), preferenceManager.getKeyValueString("pickup_time"), preferenceManager.getKeyValueString("pickup_location_name"), preferenceManager.getKeyValueString("drop_location_name"), preferenceManager.getKeyValueString("driver_mobile_no"), preferenceManager.getKeyValueString("driver_name"), preferenceManager.getKeyValueString("travel_agent_name"), preferenceManager.getKeyValueString("travel_agent_phone_no"), preferenceManager.getKeyValueString("society_id"), preferenceManager.getKeyValueString("user_id")).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
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
                            driverBookingList();

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
        restCall.rejectbooking("rejectbooking", preferenceManager.getKeyValueString("request_id"), reason, preferenceManager.getKeyValueString("driver_id"), preferenceManager.getKeyValueString("travel_agent_id"), preferenceManager.getKeyValueString("pickup_date"), preferenceManager.getKeyValueString("pickup_time"), preferenceManager.getKeyValueString("pickup_location_name"), preferenceManager.getKeyValueString("drop_location_name"), preferenceManager.getKeyValueString("driver_mobile_no"), preferenceManager.getKeyValueString("driver_name"), preferenceManager.getKeyValueString("travel_agent_name"), preferenceManager.getKeyValueString("travel_agent_phone_no"), preferenceManager.getKeyValueString("society_id"), preferenceManager.getKeyValueString("user_id")).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
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
                            driverBookingList();
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


    public void driverPickupUser(String requestId) {
        restCall.driverPickupUser("driverPickupUser", requestId).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
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
                            driverBookingList();

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

    public void driverDropUser(String requestId, String pos) {
        restCall.driverDropUser("driverDropUser", requestId).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
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
                    DropUserResponse dropUserResponse = null;
                    try {
                        dropUserResponse = new Gson().fromJson(GzipUtils.decrypt(encData), DropUserResponse.class);
                        if (dropUserResponse != null && dropUserResponse.getStatus().equalsIgnoreCase("200")) {
                            bookingList.remove(Integer.parseInt(pos));
                            requestListAdapter.update(bookingList);
                            Tools.toast(DashBoardActivity.this, dropUserResponse.getMessage(), 2);
                            driverBookingList();

                        } else {
                            Tools.toast(DashBoardActivity.this, dropUserResponse.getMessage(), 1);
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
                            tools.stopLoading();
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
                                    public void onCallNowItemClickListener(BookingRequestListResponse.Booking booking) {
                                        String userContactNumber = booking.getUserContactNumber();
                                        if (userContactNumber != null && !userContactNumber.isEmpty()) {
                                            makePhoneCall(userContactNumber);
                                        } else {
                                            Toast.makeText(DashBoardActivity.this, "User  phone number is not available", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onDropLocationItemClickListener(String pos, BookingRequestListResponse.Booking booking) {

                                        dropLatitude = booking.getDropLocationLatitude();
                                        dropLongitude = booking.getDropLocationLongitude();
                                        openMapWithDropLocation();

                                    }

                                    @Override
                                    public void onPickUpLocationItemClickListener(String pos, BookingRequestListResponse.Booking booking) {
                                        pickupLatitude = booking.getPickupLocationLatitude();
                                        pickupLongitude = booking.getPickupLocationLongitude();
                                        openMapWithPickUpLocation();

                                    }

                                    @Override
                                    public void onPickUpItemClickListener(BookingRequestListResponse.Booking booking) {

                                        if (booking.getDutyStatus().equalsIgnoreCase("1")) {
                                            showPickUpConfirmationDialog(booking.getRequestId());
                                        } else {
                                            Tools.toast(DashBoardActivity.this, "You are off Duty right now!!", 1);
                                        }

                                    }

                                    @Override
                                    public void onDropItemClickListener(String pos, BookingRequestListResponse.Booking booking) {
                                        if (booking.getDutyStatus().equalsIgnoreCase("1")) {
                                            showDropConfirmationDialog(booking.getRequestId(), pos, booking.getConfirm_message());
                                        } else {
                                            Tools.toast(DashBoardActivity.this, "You are off Duty right now!!", 1);
                                        }
                                    }
                                });

                                bookingList = bookingRequestListResponse.getBookingList();

                                // Set switch to "Off" otherwise
//                                switchOnOff.setChecked(bookingList.get(0).getDutyStatus().equalsIgnoreCase("1"));  // Set switch to "On" if dutyStatus is "1"
                            } else {
                                lin_ps_load.setVisibility(View.GONE);
                                recy_booking_list.setVisibility(View.VISIBLE);
                                rel_nodata.setVisibility(View.GONE);
                                linLayNoData.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

            }
        });
    }

    public void showAcceptDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        final ImageView img_icon = dialogView.findViewById(R.id.img_icon);
        final TextView txtHeading = dialogView.findViewById(R.id.txtHeading);
        final EditText editText_reason = dialogView.findViewById(R.id.editText_reason);
        img_icon.setImageResource(R.drawable.ic_pick_confirmation);
        txtHeading.setText(preferenceManager.getJSONKeyStringObject("are_you_sure_to_accept_this_ride"));
        editText_reason.setVisibility(View.GONE);
        builder.setView(dialogView);
        builder.setCancelable(false);
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
        final TextView txtHeading = dialogView.findViewById(R.id.txtHeading);
        txtHeading.setText(preferenceManager.getJSONKeyStringObject("reason_for_rejecting_trip_tag"));
        input.setHint(preferenceManager.getJSONKeyStringObject("write_the_reason_for_rejecting_this_trip_here"));
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String reason = input.getText().toString().trim();
            if (!TextUtils.isEmpty(reason)) {
                rejectbooking(reason);
                requestListAdapter.update(bookingList);
                driverBookingList();
                dialog.dismiss();
            } else {
                input.setError(preferenceManager.getJSONKeyStringObject("reject_error_msg"));
                input.requestFocus();
                Toast.makeText(DashBoardActivity.this, preferenceManager.getJSONKeyStringObject("reject_error_msg"), Toast.LENGTH_SHORT).show();
            }
        });
        input.requestFocus();
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dilaog_bg);
        alertDialog.show();


    }

    public void showPickUpConfirmationDialog(String requestId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        final ImageView img_icon = dialogView.findViewById(R.id.img_icon);
        final TextView txtHeading = dialogView.findViewById(R.id.txtHeading);
        final EditText editText_reason = dialogView.findViewById(R.id.editText_reason);
        img_icon.setImageResource(R.drawable.ic_pickup);
        txtHeading.setText(preferenceManager.getJSONKeyStringObject("are_you_sure_for_pick_up"));
        editText_reason.setVisibility(View.GONE);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialog, which) -> {
            driverPickupUser(requestId);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dilaog_bg);
        alertDialog.show();


    }

    public void showDropConfirmationDialog(String requestId, String pos, String confrim_message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        final ImageView img_icon = dialogView.findViewById(R.id.img_icon);
        final TextView txtHeading = dialogView.findViewById(R.id.txtHeading);
        final TextView txtAmtInfo = dialogView.findViewById(R.id.txtAmtInfo);
        final EditText editText_reason = dialogView.findViewById(R.id.editText_reason);
        img_icon.setImageResource(R.drawable.drop_confirmation);
        txtHeading.setText(preferenceManager.getJSONKeyStringObject("are_you_sure_for_drop"));
        txtAmtInfo.setText(confrim_message);
        editText_reason.setVisibility(View.GONE);
        txtAmtInfo.setVisibility(View.VISIBLE);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialog, which) -> {
            driverDropUser(requestId, pos);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dilaog_bg);
        alertDialog.show();


    }

    public void driverDutyUpdate() {
        tools.showLoading();
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
                            tools.stopLoading();
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
                    tools.stopLoading();
//                    switchOnOff.setVisibility(View.VISIBLE);
                    CommonResponse commonResponse = null;
                    try {
                        commonResponse = new Gson().fromJson(GzipUtils.decrypt(encData), CommonResponse.class);
                        if (commonResponse != null && commonResponse.getStatus().equalsIgnoreCase("200")) {
                            Tools.toast(DashBoardActivity.this, commonResponse.getMessage(), 2);
                            driverBookingList();
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

    public void driverDutyStatus() {
        tools.showLoading();
        restCall.driverDutyStatus("driverDutyStatus", preferenceManager.getKeyValueString("driver_id")).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        runOnUiThread(() -> {
                            tools.stopLoading();
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
                    tools.stopLoading();
                    DriverDutyStatusResponse driverDutyStatusResponse = null;
                    try {
                        driverDutyStatusResponse = new Gson().fromJson(GzipUtils.decrypt(encData), DriverDutyStatusResponse.class);
                        if (driverDutyStatusResponse != null && driverDutyStatusResponse.getStatus().equalsIgnoreCase("200")) {
                            switchDutyStatus = driverDutyStatusResponse.getDuty_status();
                            switchOnOff.setChecked(driverDutyStatusResponse.getDuty_status().equals("1"));
                            driverBookingList();
                        } else {
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                });

            }
        });
    }

/*    public void requestPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.POST_NOTIFICATIONS};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!arePermissionsGranted(permissions)) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private boolean arePermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allPermissionsGranted = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            showSettingsDialog();
        }
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this).setTitle("Permissions Required").setMessage("This app requires permissions to use certain features. Please grant them in the app settings.").setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAppSettings();
            }
        }).setNegativeButton("Cancel", null).show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void makePhoneCall(String phoneNumber) {
        pendingPhoneNumber = phoneNumber;
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }
    }*/

    public void openMapWithDropLocation() {

        String latitude = "";
        String longitude = "";
        latitude = dropLatitude;
        longitude = dropLongitude;
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {

            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "No Location Found!", Toast.LENGTH_SHORT).show();
        }
    }

    public void openMapWithPickUpLocation() {

        String latitude = "";
        String longitude = "";
        latitude = pickupLatitude;
        longitude = pickupLongitude;
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "No Location Found!", Toast.LENGTH_SHORT).show();
        }
    }


    private void downloadLanguage() {
        tools.showLoading();
        RestCall call2 = RestClient.createServiceJson(RestCall.class, VariableBag.COMMON_URL, preferenceManager.getMainApiKey());
        call2.getLanguageValues("getLanguageValues", "1", "101", preferenceManager.getLanguageId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                tools.stopLoading();

            }
            @Override
            public void onNext(Object responseBody) {

                tools.stopLoading();
                languagePreferenceManager.setKeyValueString(VariableBag.LANGUAGE_ID, preferenceManager.getLanguageId());
                languagePreferenceManager.setJSONPref(VariableBag.LANGUAGE, new Gson().toJson(responseBody));

            }
        });
    }

    public void setData() {
        txttagRide.setText(preferenceManager.getJSONKeyStringObject("your_ride"));
    }


    @Override
    public void onBackPressed() {
        if (lin_layout.getVisibility() == View.VISIBLE) {
            FincasysDialog fincasysDialog = new FincasysDialog(this, FincasysDialog.LOGOUT_TYPE);
            fincasysDialog.setTitleText(preferenceManager.getJSONKeyStringObject("are_you_sure_to_exit"));
            fincasysDialog.setContentText(preferenceManager.getJSONKeyStringObject("thanks_for_using_this_app"));
            fincasysDialog.setCancelText(preferenceManager.getJSONKeyStringObject("cancel"));
            fincasysDialog.setConfirmText(preferenceManager.getJSONKeyStringObject("exit"));
            fincasysDialog.setCancelable(false);
            fincasysDialog.setCancelClickListener(Dialog::dismiss);
            fincasysDialog.setConfirmClickListener(fincasysDialog12 -> {
                fincasysDialog.dismiss();
                finishAffinity();
            });

            try {
                fincasysDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.onBackPressed();
        }
    }


    public void requestPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.CALL_PHONE);

        // Request permissions for Android versions below Android 13 (API level 33)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!arePermissionsGranted(permissions.toArray(new String[0]))) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
            }
        }
    }

    private boolean arePermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {
                showSettingsDialog();
            }
        } else if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(pendingPhoneNumber);
            } else {
                showSettingsDialog();
            }
        } else if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showSettingsDialog();
            }
        }
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permissions Required")
                .setMessage("This app requires permissions to use certain features. Please grant them in the app settings.")
                .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openAppSettings();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void makePhoneCall(String phoneNumber) {
        pendingPhoneNumber = phoneNumber;
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }
    }
}