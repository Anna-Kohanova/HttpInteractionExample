package com.example.http;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.http.httpexample.R;
import com.example.http.httpmanagers.CRUD;
import com.example.http.httpmanagers.OkHttpManager;
import com.example.http.model.Comment;
import com.example.http.model.Post;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CRUD<Post, Comment> {

    private static final String API_URL_FOR_GET = "https://api.github.com/users/anna-kohanova";
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

        //Using Retrofit library
        getAll();
        getById(4);
        getAllById(4);

        final Post post = new Post(1, 1, "Title", "Body");
        add(post);
        deleteById(1);

        //Using OkHttp
        makeOkHttpAsyncCall();
        makeOkHttpSyncCall();
    }

    @Override
    public void add(final Post pPost) {
        final Call<Post> call = App.getService().createPost(pPost);

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    final String response = String.valueOf(call.execute().headers());
                    Log.i("Add post", response);
                } catch (final IOException pE) {
                    pE.printStackTrace();
                }
            }
        });
    }

    @Override
    public Post getById(final int id) {
        final Call<Post> call = App.getService().getPostById(id);

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final FutureTask<Post> futureTask = new FutureTask<>(new Callable<Post>() {

            @Override
            public Post call() throws Exception {
                return call.execute().body();
            }
        });
        executorService.execute(futureTask);

        Post post = null;
        try {
            post = futureTask.get();
            Log.i("Post by id", post.getId() + " " + post.getUserId() + " " + post.getTitle() +
                    " " + post.getBody());
        } catch (InterruptedException | ExecutionException pE) {
            pE.printStackTrace();
        }

        return post;
    }

    @Override
    public List<Comment> getAllById(final int id) {
        final Call<List<Comment>> listCall = App.getService().getCommentsById(id);

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final FutureTask<List<Comment>> futureTask = new FutureTask<>(new Callable<List<Comment>>() {

            @Override
            public List<Comment> call() throws Exception {
                return listCall.execute().body();
            }
        });
        executorService.execute(futureTask);

        List<Comment> comments = null;
        try {
            comments = futureTask.get();

            for (final Comment comment : comments) {
                Log.i("Posts", comment.getId() + " " + comment.getPostId() + " " + comment.getName() +
                        " " + comment.getBody() + " " + comment.getEmail());
            }
        } catch (InterruptedException | ExecutionException pE) {
            pE.printStackTrace();
        }

        return comments;
    }

    @Override
    public List<Post> getAll() {
        final Call<List<Post>> call = App.getService().getPosts();

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final FutureTask<List<Post>> futureTask = new FutureTask<>(new Callable<List<Post>>() {

            @Override
            public List<Post> call() throws IOException {
                return call.execute().body();
            }
        });
        executorService.execute(futureTask);

        List<Post> posts = null;
        try {
            posts = futureTask.get();

            for (final Post post : posts) {
                Log.i("Posts", post.getId() + " " + post.getUserId() + " " + post.getTitle() +
                        " " + post.getBody());
            }
        } catch (InterruptedException | ExecutionException pE) {
            pE.printStackTrace();
        }

        return posts;
    }

    @Override
    public void deleteById(final int id) {
        final Call<Void> call = App.getService().deletePost(id);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(final Call<Void> call, final Response<Void> response) {
                Log.i("Delete", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(final Call<Void> call, final Throwable t) {

            }
        });
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
