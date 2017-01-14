package com.example.http;

import android.app.Application;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class App extends Application {
    private static OkHttpClient mOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();

        final int cacheSize = 10 * 1024 * 1024; // 10 MiB
        final Cache cache = new Cache(new File(this.getCacheDir(),"cacheFileName"), cacheSize);

        mOkHttpClient = new OkHttpClient.Builder().cache(cache).build();
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
}
