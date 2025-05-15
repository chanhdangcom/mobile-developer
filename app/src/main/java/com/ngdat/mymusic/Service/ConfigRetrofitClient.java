package com.ngdat.mymusic.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigRetrofitClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(String url_base) {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .addInterceptor(new MockInterceptor(MVPApp.getmContextApp(), true))
                .build();
        Gson mGson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(url_base)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();
        return retrofit;
    }
}
