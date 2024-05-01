package com.adanitownship.driver;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.adanitownship.driver.network.RestCall;
import com.adanitownship.driver.network.RestClient;
import com.adanitownship.driver.networkResponse.CommonResponse;
import com.adanitownship.driver.networkResponse.VerifyOtpResponse;
import com.adanitownship.driver.utils.GzipUtils;
import com.adanitownship.driver.utils.PreferenceManager;
import com.adanitownship.driver.utils.Tools;
import com.google.firebase.FirebaseApp;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.mukesh.OtpView;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.schedulers.Schedulers;


public class OTPDialogFragment extends DialogFragment {
    RestCall restCall;
    String tokenStr;

    Tools tools;
    LinearLayout OTPDialogFragLlResend;
    ImageView OTPDialogFragIv_truemobile_register;

    TextView OTPDialogFragTv_coundown_otp, OTPDialogFragEt_mobile_register;

    Button OTPDialogFragDone_btn, OTPDialogFragCancel_bt;
    String mNum, versionCode;

    OtpView OTPDialogFragOtp_view;
    PreferenceManager preferenceManager;

    OTPDialogFragment(String mNum) {
        this.mNum = mNum;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_o_t_p_dialog, container, false);
        restCall = RestClient.createService(RestCall.class, "https://adanidev.mysmartsociety.app/shantivan/residentApiNewEnc/", "smartapikey");
        tools = new Tools(requireActivity());

        FirebaseApp.initializeApp(requireActivity());
        OTPDialogFragOtp_view = view.findViewById(R.id.OTPDialogFragOtp_view);
        OTPDialogFragDone_btn = view.findViewById(R.id.OTPDialogFragDone_btn);
        OTPDialogFragEt_mobile_register = view.findViewById(R.id.OTPDialogFragEt_mobile_register);
        OTPDialogFragIv_truemobile_register = view.findViewById(R.id.OTPDialogFragIv_truemobile_register);
        OTPDialogFragLlResend = view.findViewById(R.id.OTPDialogFragLlResend);
        OTPDialogFragCancel_bt = view.findViewById(R.id.OTPDialogFragCancel_bt);
        preferenceManager = new PreferenceManager(requireActivity());
        OTPDialogFragTv_coundown_otp = view.findViewById(R.id.OTPDialogFragTv_coundown_otp);
        OTPDialogFragOtp_view.setItemCount(6);
        OTPDialogFragEt_mobile_register.setText(mNum);
        OTPDialogFragEt_mobile_register.setEnabled(false);
        OTPDialogFragEt_mobile_register.setFocusable(false);
        countDownTimer();

//        getDeviceToken();
//        getToken();

        OTPDialogFragLlResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverResendOtp();
            }
        });
//        App version
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName; // version name
            versionCode = String.valueOf(pInfo.versionCode); // version code
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        OTPDialogFragDone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                driverOtpVerification();
            }
        });

        OTPDialogFragCancel_bt.setOnClickListener(v -> dismiss());

//        try {
//
//            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
//                if (!TextUtils.isEmpty(token)) {
//                    tokenStr = token;
//                    preferenceManager.setKeyValueString("token", token);
//
//
//                } else {
//
//                    if (FirebaseInstallations.getInstance().getToken(true).isSuccessful()) {
//                        tokenStr = FirebaseInstallations.getInstance().getToken(true).getResult().getToken();
//                        preferenceManager.setKeyValueString("token", tokenStr);
//
//                    } else {
//                        checkNetworkConnection(token);
//                    }
//
//                }
//            }).addOnFailureListener(e -> {
//
//                if (FirebaseInstallations.getInstance().getToken(true).isSuccessful()) {
//                    tokenStr = FirebaseInstallations.getInstance().getToken(true).getResult().getToken();
//                    preferenceManager.setKeyValueString("token", tokenStr);
//
//                } else {
//                    checkNetworkConnection(e.getLocalizedMessage());
//                }
//
//                //handle e
//            }).addOnCanceledListener(() -> {
//                if (FirebaseInstallations.getInstance().getToken(true).isSuccessful()) {
//                    tokenStr = FirebaseInstallations.getInstance().getToken(true).getResult().getToken();
//                    preferenceManager.setKeyValueString("token", tokenStr);
//
//                }
//                //handle cancel
//            }).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    tokenStr = task.getResult();
//                    preferenceManager.setKeyValueString("token", task.getResult());
//
//                    Log.v("TAG", "This is the token : " + task.getResult());
//                } else {
//                    if (FirebaseInstallations.getInstance().getToken(true).isSuccessful()) {
//                        tokenStr = FirebaseInstallations.getInstance().getToken(true).getResult().getToken();
//                        preferenceManager.setKeyValueString("token", tokenStr);
//
//                    } else {
//                        checkNetworkConnection(Objects.requireNonNull(task.getException()).getLocalizedMessage());
//                    }
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return view;
    }


    public void driverResendOtp() {
        restCall.driverResendOtp("driverResendOtp", mNum).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Tools.toast(requireActivity(), getString(R.string.no_internet_connection), 1);

                    }
                });
            }

            @Override
            public void onNext(String encData) {
                tools.stopLoading();
                CommonResponse commonResponse = null;
                try {
                    commonResponse = new Gson().fromJson(GzipUtils.decrypt(encData), CommonResponse.class);
                    if (commonResponse != null && commonResponse.getStatus().equalsIgnoreCase("200")) {

                        Tools.toast(requireActivity(), commonResponse.getMessage(), 2);
                        requireActivity().finish();


                    } else {
                        Tools.toast(requireActivity(), commonResponse.getMessage(), 1);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void driverOtpVerification() {
        Log.e("Tanu85", Tools.getDeviceID(requireActivity()));
        Log.e("Tanu85", Tools.getDeviceID(requireActivity()));


        restCall.driverOtpVerification("driverOtpVerification", mNum, OTPDialogFragOtp_view.getText().toString(), preferenceManager.getKeyValueString("token"), "android", Tools.getDeviceID(requireActivity()), versionCode).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Tools.toast(requireActivity(), getString(R.string.no_internet_connection), 1);

                    }
                });
            }

            @Override
            public void onNext(String encData) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tools.stopLoading();
                        VerifyOtpResponse verifyOtpResponse = null;
                        try {
                            verifyOtpResponse = new Gson().fromJson(GzipUtils.decrypt(encData), VerifyOtpResponse.class);
                            if (verifyOtpResponse != null && verifyOtpResponse.getStatus().equalsIgnoreCase("200")) {

                                Tools.toast(requireActivity(), verifyOtpResponse.getMessage(), 2);
                                Intent intent = new Intent(requireActivity(), DashBoardActivity.class);
                                startActivity(intent);
                                preferenceManager.setKeyValueString("driver_id", verifyOtpResponse.getDriverId());
                                preferenceManager.setKeyValueString("driver_name", verifyOtpResponse.getDriverName());
                                preferenceManager.setKeyValueString("travel_agent_id",verifyOtpResponse.getTravelAgentId());
                                preferenceManager.setKeyValueString("driver_profile",verifyOtpResponse.getDriverPhoto());
                                requireActivity().finish();
                            } else {
                                Tools.toast(requireActivity(), verifyOtpResponse.getMessage(), 1);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }

    public void getDeviceToken() {

        //    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//            @SuppressLint("RestrictedApi")
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                if (!task.isSuccessful()) {
//                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                    return;
//                }
//                token = task.getResult();
//                Toast.makeText(requireActivity(), token, Toast.LENGTH_LONG).show();
//            }
//        });


        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                tokenStr = token;
                preferenceManager.setKeyValueString("token", token);

            } else {

                if (FirebaseInstallations.getInstance().getToken(true).isSuccessful()) {
                    tokenStr = FirebaseInstallations.getInstance().getToken(true).getResult().getToken();
                    preferenceManager.setKeyValueString("token", tokenStr);
                } else {
                    checkNetworkConnection(token);
                }

            }
        }).addOnFailureListener(e -> {

            if (FirebaseInstallations.getInstance().getToken(true).isSuccessful()) {
                tokenStr = FirebaseInstallations.getInstance().getToken(true).getResult().getToken();
                preferenceManager.setKeyValueString("token", tokenStr);
            } else {
                checkNetworkConnection(e.getLocalizedMessage());
            }

            //handle e
        }).addOnCanceledListener(() -> {
            if (FirebaseInstallations.getInstance().getToken(true).isSuccessful()) {
                tokenStr = FirebaseInstallations.getInstance().getToken(true).getResult().getToken();
                preferenceManager.setKeyValueString("token", tokenStr);
            }
            //handle cancel
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                tokenStr = task.getResult();
                preferenceManager.setKeyValueString("token", task.getResult());
                Log.v("TAG", "This is the token : " + task.getResult());
            } else {
                if (FirebaseInstallations.getInstance().getToken(true).isSuccessful()) {
                    tokenStr = FirebaseInstallations.getInstance().getToken(true).getResult().getToken();
                    preferenceManager.setKeyValueString("token", tokenStr);
                } else {
                    checkNetworkConnection(Objects.requireNonNull(task.getException()).getLocalizedMessage());
                }
            }
        });

    }


    public void countDownTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                String text = String.format(Locale.getDefault(), "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(l) % 60, TimeUnit.MILLISECONDS.toSeconds(l) % 60);
                OTPDialogFragTv_coundown_otp.setText(text);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {

                OTPDialogFragIv_truemobile_register.setVisibility(View.VISIBLE);
                OTPDialogFragLlResend.setVisibility(View.VISIBLE);

//                if (is_email_otp){
//                    lin_email_otp.setVisibility(View.VISIBLE);
//                }else {
//                    lin_email_otp.setVisibility(View.GONE);
//                }
//                if (is_voice_otp){
//                    lin_call_otp.setVisibility(View.VISIBLE);
//                }else {
//                    lin_call_otp.setVisibility(View.GONE);
//                }

                OTPDialogFragTv_coundown_otp.setText("00:00");

            }
        };
        countDownTimer.start();
    }

    public void checkNetworkConnection(String msg) {
        requireActivity().runOnUiThread(() -> {

            try {

                tokenStr = Tools.generateRandomHexToken(100);
                preferenceManager.setKeyValueString("token", tokenStr);


            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!requireActivity().isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(msg);
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", (dialog, which) -> {
                    dialog.dismiss();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


    private void getToken() {

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                preferenceManager.setKeyValueString("token", token);

            } else {

                if (FirebaseInstallations.getInstance().getToken(true).isSuccessful()) {

                    preferenceManager.setKeyValueString("token", FirebaseInstallations.getInstance().getToken(true).getResult().getToken());

                }

            }
        }).addOnFailureListener(e -> {

            if (FirebaseInstallations.getInstance().getToken(true).isSuccessful()) {
                preferenceManager.setKeyValueString("token", FirebaseInstallations.getInstance().getToken(true).getResult().getToken());
            }

            //handle e
        }).addOnCanceledListener(() -> {
            if (FirebaseInstallations.getInstance().getToken(true).isSuccessful()) {
                preferenceManager.setKeyValueString("token", FirebaseInstallations.getInstance().getToken(true).getResult().getToken());
            }
            //handle cancel
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                preferenceManager.setKeyValueString("token", task.getResult());
                Log.v("TAG", "This is the token : " + task.getResult());
            } else {

                if (FirebaseInstallations.getInstance().getToken(true).isSuccessful()) {
                    preferenceManager.setKeyValueString("token", FirebaseInstallations.getInstance().getToken(true).getResult().getToken());
                }
            }
        });

    }


}