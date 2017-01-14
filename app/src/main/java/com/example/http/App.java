package com.example.http;

import android.app.Application;

import okhttp3.OkHttpClient;

public class App extends Application {
    private static OkHttpClient mOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();

        mOkHttpClient = new OkHttpClient();
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
}
