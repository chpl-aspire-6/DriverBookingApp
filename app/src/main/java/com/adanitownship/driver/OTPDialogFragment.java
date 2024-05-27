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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.adanitownship.driver.network.RestCall;
import com.adanitownship.driver.network.RestClient;
import com.adanitownship.driver.networkResponse.CommonResponse;
import com.adanitownship.driver.networkResponse.VerifyOtpResponse;
import com.adanitownship.driver.utils.GzipUtils;
import com.adanitownship.driver.utils.PreferenceManager;
import com.adanitownship.driver.utils.Tools;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.mukesh.OtpView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.schedulers.Schedulers;


public class OTPDialogFragment extends DialogFragment {
    RestCall restCall;
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
                if(OTPDialogFragOtp_view.length()==6){
                    driverOtpVerification();
                    preferenceManager.setLoginSession();
                }else {
                    OTPDialogFragOtp_view.setError("Enter the Otp");
                    OTPDialogFragOtp_view.requestFocus();
                }


            }
        });

        OTPDialogFragCancel_bt.setOnClickListener(v -> dismiss());

        return view;
    }


    public void driverResendOtp() {
        tools.showLoading();
        restCall.driverResendOtp("driverResendOtp", mNum).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tools.stopLoading();
                        Tools.toast(getActivity(), getString(R.string.no_internet_connection), 1);
                    }
                });
            }

            @Override
            public void onNext(String encData) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tools.stopLoading();
                        CommonResponse commonResponse = null;
                        try {
                            commonResponse = new Gson().fromJson(GzipUtils.decrypt(encData), CommonResponse.class);
                            if (commonResponse != null && commonResponse.getStatus().equalsIgnoreCase("200")) {
                                Tools.toast(requireActivity(), commonResponse.getMessage(), 2);
                                OTPDialogFragLlResend.setVisibility(View.GONE);
                                countDownTimer();

                            } else {
                                Tools.toast(requireActivity(), commonResponse.getMessage(), 1);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

            }
        });

    }

    public void driverOtpVerification() {
        tools.showLoading();
        restCall.driverOtpVerification("driverOtpVerification", mNum, OTPDialogFragOtp_view.getText().toString(), preferenceManager.getKeyValueString("token"), "android", Tools.getDeviceID(requireActivity()), versionCode).subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tools.stopLoading();
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
//                                preferenceManager.setLoginSession();
                                Tools.toast(requireActivity(), verifyOtpResponse.getMessage(), 2);
                                Intent intent = new Intent(requireActivity(), DashBoardActivity.class);
                                startActivity(intent);
                                preferenceManager.setKeyValueString("driver_id", verifyOtpResponse.getDriverId());
                                preferenceManager.setKeyValueString("driver_name", verifyOtpResponse.getDriverName());
                                preferenceManager.setKeyValueString("travel_agent_id", verifyOtpResponse.getTravelAgentId());
                                preferenceManager.setKeyValueString("driver_profile", verifyOtpResponse.getDriverPhoto());
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


    public void countDownTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(20000, 1000) {
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
                OTPDialogFragTv_coundown_otp.setText("00:00");
            }
        };
        countDownTimer.start();
    }

}