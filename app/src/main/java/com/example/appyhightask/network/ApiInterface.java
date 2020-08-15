package com.example.appyhightask.network;

import com.example.appyhightask.models.NewsData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<NewsData> getHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );

}
