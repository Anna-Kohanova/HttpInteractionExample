package com.example.http;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.http.httpexample.R;
import com.example.http.httpmanagers.OkHttpManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL_FOR_GET = "https://api.github.com/users/vogella";
    private static final String API_URL_FOR_POST = "http://www.roundsapp.com/post";

    private final OkHttpManager mOkHttpManager = new OkHttpManager();
    private TextView mGetTextView;
    private TextView mPostTextView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGetTextView = (TextView) findViewById(R.id.get_result);
        mPostTextView = (TextView) findViewById(R.id.post_result);

        makeOkHttpAsyncCall();
        makeOkHttpSyncCall();
    }


    private void makeOkHttpAsyncCall() {
        mOkHttpManager.doAsyncGetRequest(API_URL_FOR_GET, mGetTextView);
        mOkHttpManager.doAsyncPostRequest(API_URL_FOR_POST,
                mOkHttpManager.constructJson("Jesse", "John"), mPostTextView);
    }

    public void makeOkHttpSyncCall() {
        final String json = mOkHttpManager.constructJson("Jesse", "John");

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    final String getResponse = mOkHttpManager.doSyncGetRequest(API_URL_FOR_GET);
                    Log.i("Sync get response", getResponse);

                    final String postResponse = mOkHttpManager.doSyncPostRequest(API_URL_FOR_POST, json);
                    Log.i("Sync post response", postResponse);
                } catch (final IOException pE) {
                    pE.printStackTrace();
                }
            }
        }).start();
    }
}
