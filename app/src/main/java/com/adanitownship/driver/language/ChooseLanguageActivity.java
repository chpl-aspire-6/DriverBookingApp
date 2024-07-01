package com.adanitownship.driver.language;

import static com.adanitownship.driver.utils.PreferenceManager.preferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adanitownship.driver.AppLevel;
import com.adanitownship.driver.DashBoardActivity;
import com.adanitownship.driver.LoginActivity;
import com.adanitownship.driver.R;
import com.adanitownship.driver.network.RestCall;
import com.adanitownship.driver.network.RestClient;
import com.adanitownship.driver.networkResponse.LanguageResponse;
import com.adanitownship.driver.utils.GzipUtils;
import com.adanitownship.driver.utils.LanguagePreferenceManager;
import com.adanitownship.driver.utils.Tools;
import com.adanitownship.driver.utils.VariableBag;
import com.google.gson.Gson;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChooseLanguageActivity extends AppCompatActivity {

    public RestCall callCommonUrlJson, callCommonUrl;
    public Tools tools;
    public LanguagePreferenceManager languagePreferenceManager;
    RecyclerView recy_language;
    ProgressBar ps_bar;
    Button btn_continue;
    String countryId, stateId, cityId, countryCode;
    String urlFile = "", langId;
    boolean isFromSetting = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);
        recy_language = findViewById(R.id.recy_language);
        ps_bar = findViewById(R.id.ps_bar);
        btn_continue = findViewById(R.id.btn_continue);
        recy_language.setVisibility(View.GONE);
        ps_bar.setVisibility(View.VISIBLE);
        tools = new Tools(ChooseLanguageActivity.this);
        languagePreferenceManager = AppLevel.getInstance().getLanPrefLang();
        callCommonUrl = RestClient.createService(RestCall.class, VariableBag.COMMON_URL, preferenceManager.getMainApiKey());
        callCommonUrlJson = RestClient.createServiceJson(RestCall.class, VariableBag.COMMON_URL, preferenceManager.getMainApiKey());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            countryId = bundle.getString("countryId");
            stateId = bundle.getString("stateId");
            cityId = bundle.getString("cityId");
            countryCode = bundle.getString("countryCode");

            isFromSetting = bundle.getBoolean("isFromSetting", false);
        }
        callCommonUrl.getLanguage("getLanguageNew", "101").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String encData) {

                try {
                    LanguageResponse languageResponse = new Gson().fromJson(GzipUtils.decrypt(encData), LanguageResponse.class);


                    if (languageResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {
                        recy_language.setVisibility(View.VISIBLE);
                        ps_bar.setVisibility(View.GONE);

                        if (isFromSetting) {
                            for (int i = 0; i < languageResponse.getLanguage().size(); i++) {
                                if (preferenceManager.getLanguageId().equalsIgnoreCase(languageResponse.getLanguage().get(i).getLanguageId())) {
                                    languageResponse.getLanguage().get(i).setClicked(true);
                                    urlFile = languageResponse.getLanguage().get(i).getLanguageFile();
                                    langId = languageResponse.getLanguage().get(i).getLanguageId();
                                    btn_continue.setEnabled(true);
                                    btn_continue.setBackground(ContextCompat.getDrawable(ChooseLanguageActivity.this, R.drawable.btn_rounded_green));
                                }
                            }
                        } else {

                            languageResponse.getLanguage().get(0).setClicked(true);
                            urlFile = languageResponse.getLanguage().get(0).getLanguageFile();
                            langId = languageResponse.getLanguage().get(0).getLanguageId();

                            btn_continue.setEnabled(true);
                            btn_continue.setBackground(ContextCompat.getDrawable(ChooseLanguageActivity.this, R.drawable.btn_rounded_green));

                        }
                        recy_language.setLayoutManager(new GridLayoutManager(ChooseLanguageActivity.this, 1));
                        LanguageAdapter adapter = new LanguageAdapter(ChooseLanguageActivity.this, languageResponse.getLanguage());
                        recy_language.setAdapter(adapter);
                        adapter.setUp(st -> {
                            if (st.isClicked()) {

                                urlFile = st.getLanguageFile();
                                langId = st.getLanguageId();
                                btn_continue.setEnabled(true);
                                btn_continue.setBackground(ContextCompat.getDrawable(ChooseLanguageActivity.this, R.drawable.btn_rounded_green));
                            } else {
                                btn_continue.setBackground(ContextCompat.getDrawable(ChooseLanguageActivity.this, R.drawable.btn_rounded_disable));
                                btn_continue.setEnabled(false);
                            }

                            adapter.update();
                        });

                        btn_continue.setOnClickListener(view -> {
                            tools.showLoading();
                            downloadLanguage(langId);
                        });
                    }
                } catch (Exception ignored) {

                }
            }
        });

    }

    private void downloadLanguage(String languageId) {

        callCommonUrlJson.getLanguageValues("getLanguageValues", "1", "101", languageId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                tools.stopLoading();
            }

            @Override
            public void onNext(Object responseBody) {
                runOnUiThread(() -> {

                    try {
                        Object obj = new Gson().toJson(responseBody);

                        tools.stopLoading();
                        preferenceManager.setLanguageId(langId);
                        languagePreferenceManager.setLanguageId(langId);
                        languagePreferenceManager.setJSONPref(VariableBag.LANGUAGE, new Gson().toJson(responseBody));

                        if (isFromSetting) {
                            Intent intent = new Intent(ChooseLanguageActivity.this, DashBoardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(ChooseLanguageActivity.this, LoginActivity.class);
                            intent.putExtra("countryId", countryId);
                            intent.putExtra("stateId", stateId);
                            intent.putExtra("cityId", cityId);
                            intent.putExtra("countryCode", countryCode);
                            intent.putExtra("languageId", languageId);
                            intent.putExtra("langResponse", new Gson().toJson(responseBody));
                            startActivity(intent);
                        }
                    } catch (Exception ignored) {

                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}