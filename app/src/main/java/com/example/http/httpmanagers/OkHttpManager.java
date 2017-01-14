package com.example.http.httpmanagers;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.example.http.App;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpManager {

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    public String doSyncGetRequest(final String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .cacheControl(new CacheControl.Builder().maxStale(365, TimeUnit.DAYS).build())
                .build();

        final Response response = App.getOkHttpClient().newCall(request).execute();
        final String responseData = response.body().string();

        // Cache response
        final Response cachedData = response.cacheResponse();
        if (cachedData != null) {
            Log.i("cached", cachedData.toString());
        }

        // Get list of response headers
        final Headers responseHeaders = response.headers();
        for (int i = 0; i < responseHeaders.size(); i++) {
            Log.d("DEBUG", responseHeaders.name(i) + ": " + responseHeaders.value(i));
        }

        // Get header by key
        Log.i("Header info", response.header("Date"));
        Log.i("Header info", response.header("Server"));

        //Decode data by converting to JSONObject or JSONArray
        try {
            final JSONObject jsonObject = new JSONObject(responseData);
            Log.i("json", jsonObject.getString("name"));
        } catch (final JSONException pE) {
            pE.printStackTrace();
        }

        return responseData;
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
