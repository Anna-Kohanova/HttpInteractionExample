package com.example.http;

import android.app.Application;

import com.example.http.httpmanagers.JsonPlaceholderService;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static OkHttpClient mOkHttpClient;
    private static JsonPlaceholderService sService;
    private Retrofit mRetrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        final int cacheSize = 10 * 1024 * 1024; // 10 MiB
        final Cache cache = new Cache(new File(this.getCacheDir(), "cacheFileName"), cacheSize);

        mOkHttpClient = new OkHttpClient.Builder().cache(cache).build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        sService = mRetrofit.create(JsonPlaceholderService.class);
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static JsonPlaceholderService getService() {
        return sService;
    }
}
