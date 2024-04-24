package com.adanitownship.driver.network;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestClient {
    public static <S> S createService(Class<S> serviceClass, String baseurl, String key) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseurl)
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient(key, key, key))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceJson(Class<S> serviceClass, String baseurl, String key) {
        Gson gson = new Gson();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseurl)
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient(key, key, key))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, String baseurl, String key, String userName, String password) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseurl)
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient(key, userName, password))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
