package com.example.http.httpmanagers;

import com.example.http.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceholderService {
    @GET("/posts")
    Call<List<Post>> getUsers();

    @GET("/posts/{id}")
    Call<Post> getUserById(@Path("id") int id);

    @POST("/posts")
    Call<Post> createPost(@Body Post post);
}
