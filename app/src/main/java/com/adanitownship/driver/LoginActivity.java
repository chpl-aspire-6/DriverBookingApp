package com.adanitownship.driver;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.adanitownship.driver.network.RestCall;
import com.adanitownship.driver.network.RestClient;
import com.adanitownship.driver.networkResponse.CommonResponse;
import com.adanitownship.driver.utils.GzipUtils;
import com.adanitownship.driver.utils.PreferenceManager;
import com.adanitownship.driver.utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.Objects;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {


    EditText edtEmailOrMobile;
    Button btnContinue;
    RestCall restCall;
    String tokenStr;
    Tools tools;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtEmailOrMobile = findViewById(R.id.edtEmailOrMobile);
        btnContinue = findViewById(R.id.btnContinue);
        preferenceManager = new PreferenceManager(LoginActivity.this);
        tools = new Tools(this);
        restCall = RestClient.createService(RestCall.class, "https://adanidev.mysmartsociety.app/shantivan/residentApiNewEnc/", "smartapikey");
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmailOrMobile.getText().toString().trim().isEmpty()) {
                    edtEmailOrMobile.setError("Enter your number or email" +
                            "!");
                    edtEmailOrMobile.requestFocus();
                } else {

                    driverLogin();
                }
            }

        });

        try {

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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void driverLogin() {

        Log.d("asds", "driverLogin: tokenStr" + tokenStr);

        tools.showLoading();
        restCall.driverLogin("driverLogin", edtEmailOrMobile.getText().toString())
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Tools.toast(LoginActivity.this, getString(R.string.no_internet_connection), 1);

                            }
                        });

                    }

                    @Override
                    public void onNext(String encData) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tools.stopLoading();
                                CommonResponse commonResponse = null;
                                try {
                                    commonResponse = new Gson().fromJson(GzipUtils.decrypt(encData), CommonResponse.class);
                                    if (commonResponse != null && commonResponse.getStatus().equalsIgnoreCase("200")) {
                                        Tools.toast(LoginActivity.this, commonResponse.getMessage(), 2);
//                                        preferenceManager.setLoginSession();
                                        callDialog(edtEmailOrMobile.getText().toString());
                                    } else {
                                        Tools.toast(LoginActivity.this, commonResponse.getMessage(), 1);
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        });
                    }
                });
    }

    public void callDialog(String mNum) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        OTPDialogFragment otpDialogFragment = new OTPDialogFragment(mNum);
        otpDialogFragment.setCancelable(false);
        otpDialogFragment.show(fragmentTransaction, "otpfragment");
    }

    public void checkNetworkConnection(String msg) {
        runOnUiThread(() -> {

            try {

                tokenStr = Tools.generateRandomHexToken(100);
                preferenceManager.setKeyValueString("token", tokenStr);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!isFinishing()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle(msg);
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}