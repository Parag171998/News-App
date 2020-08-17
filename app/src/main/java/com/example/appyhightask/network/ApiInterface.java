package com.example.appyhightask.network;

import com.example.appyhightask.models.NewsData;
import com.example.appyhightask.models.WeatherInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<NewsData> getHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );

    @GET("current.json?key=3f6a5b20fa77437dbba155853201708")
    Call<WeatherInfo> getWeather( @Query("q") String location );
}
