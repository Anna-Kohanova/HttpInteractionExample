package com.example.http.httpmanagers;

import com.example.http.model.Comment;
import com.example.http.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceholderService {
    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("posts/{id}")
    Call<Post> getPostById(@Path("id") int id);

    @GET("comments")
    Call<List<Comment>> getCommentsById(@Query("postId") int id);

    @PUT("posts/1")
    Call<Post> createPost(@Body Post post);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);
}
