package com.example.http.httpmanagers;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.example.http.App;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpManager {

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    public String doSyncGetRequest(final String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();

        final Response response = App.getOkHttpClient().newCall(request).execute();
        return response.body().string();
    }

    public String doSyncPostRequest(final String url, final String json) throws IOException {
        final RequestBody requestBody = RequestBody.create(JSON_TYPE, json);
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        final Response response = App.getOkHttpClient().newCall(request).execute();
        return response.body().string();
    }

    public void doAsyncGetRequest(final String url, final TextView pTextView) {
        final Request request = new Request.Builder()
                .url(url)
                .build();

        App.getOkHttpClient().newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected " + response);
                }

                final String responseData = response.body().string();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        pTextView.setText(responseData);
                    }
                });
            }
        });
    }

    public void doAsyncPostRequest(final String url, final String json, final TextView pTextView) {
        final RequestBody requestBody = RequestBody.create(JSON_TYPE, json);
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        App.getOkHttpClient().newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected " + response);
                }

                final String responseData = response.body().string();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        pTextView.setText(responseData);
                    }
                });
            }
        });
    }

    public String constructJson(final String player1, final String player2) {
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }
}
