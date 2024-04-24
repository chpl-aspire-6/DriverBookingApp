package com.adanitownship.driver;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.adanitownship.driver.network.RestCall;
import com.adanitownship.driver.network.RestClient;
import com.adanitownship.driver.networkResponse.BookingRequestListResponse;
import com.adanitownship.driver.networkResponse.CommonResponse;
import com.adanitownship.driver.utils.GzipUtils;
import com.adanitownship.driver.utils.PreferenceManager;
import com.adanitownship.driver.utils.Tools;
import com.adanitownship.driver.utils.VariableBag;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.schedulers.Schedulers;


public class DashBoardActivity extends AppCompatActivity {
    RestCall restCall;
    PreferenceManager preferenceManager;
    SwipeRefreshLayout swipe;
    ImageView imgClose, imgIcon;
    EditText etSearch;
    TextView txt_PersonName;
    RelativeLayout rel_nodata;
    LinearLayout lin_ps_load, lin_logout;
    RecyclerView recy_booking_list;
    RequestListAdapter requestListAdapter;
    Tools tools;
    SwitchCompat switchOnOff;
    int switchStatus;
    List<BookingRequestListResponse.Booking> bookingList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dash_board);
        restCall = RestClient.createService(RestCall.class, "https://adanidev.mysmartsociety.app/shantivan/residentApiNewEnc/", "smartapikey");
        preferenceManager = new PreferenceManager(this);
        recy_booking_list = findViewById(R.id.recy_booking_list);
        lin_ps_load = findViewById(R.id.lin_ps_load);
        lin_logout = findViewById(R.id.lin_logout);
        rel_nodata = findViewById(R.id.rel_nodata);
        txt_PersonName = findViewById(R.id.txt_PersonName);
        switchOnOff = findViewById(R.id.switchOnOff);
        imgClose = findViewById(R.id.imgClose);
        etSearch = findViewById(R.id.etSearch);
        swipe = findViewById(R.id.swipe);
        tools = new Tools(DashBoardActivity.this);
        lin_ps_load.setVisibility(View.VISIBLE);
        recy_booking_list.setVisibility(View.GONE);
        rel_nodata.setVisibility(View.GONE);
        Tools.displayImage(DashBoardActivity.this, imgIcon, preferenceManager.getNoDataIcon());

        txt_PersonName.setText(preferenceManager.getKeyValueString("driver_name"));

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                driverBookingList();

            }
        });

//
        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchStatus = 1;//on
//                    switchOnOff.setTrackTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
                } else {
                    switchStatus = 0;//off
//                    switchOnOff.setTrackTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }
        });


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
        driverBookingList();

    }


    public void acceptbooking() {

        restCall.acceptbooking("acceptbooking", preferenceManager.getKeyValueString("request_id")).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
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
        restCall.rejectbooking("rejectbooking", preferenceManager.getKeyValueString("request_id"), reason).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
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
                            rel_nodata.setVisibility(View.VISIBLE);
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

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DashBoardActivity.this, LinearLayoutManager.VERTICAL, false);
                                recy_booking_list.setLayoutManager(layoutManager);
                                requestListAdapter = new RequestListAdapter(bookingRequestListResponse.getBookingList(), DashBoardActivity.this);
                                recy_booking_list.setAdapter(requestListAdapter);
                                requestListAdapter.setOnItemClickListener(new RequestListAdapter.ItemSingleClickListener() {
                                    @Override
                                    public void onAcceptItemClickListener(BookingRequestListResponse.Booking booking) {
                                        if (booking.getDutyStatus().equalsIgnoreCase("1")) {
                                            preferenceManager.setKeyValueString("request_id", booking.getRequestId());
                                            showAcceptDialog();
                                        } else {
                                            Tools.toast(DashBoardActivity.this, "You are off Duty right now!!", 1);
                                        }

                                    }

                                    @Override
                                    public void onRejectItemClickListener(BookingRequestListResponse.Booking booking) {
                                        if (booking.getDutyStatus().equalsIgnoreCase("1")) {
                                            preferenceManager.setKeyValueString("request_id", booking.getRequestId());
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

                                if (bookingList.get(0).getDutyStatus().equalsIgnoreCase("1")) {
                                    switchOnOff.setChecked(true);  // Set switch to "On" if dutyStatus is "1"
                                } else {
                                    switchOnOff.setChecked(false); // Set switch to "Off" otherwise
                                }



                            } else {
                                Tools.toast(DashBoardActivity.this, bookingRequestListResponse.getMessage(), 1);

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
        builder.setTitle("Pick Up Confirmation");
        builder.setIcon(R.drawable.ic_confirm);
        builder.setMessage("Are you sure you want to Accept this Ride ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                acceptbooking();
                requestListAdapter.update(bookingList);

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
}