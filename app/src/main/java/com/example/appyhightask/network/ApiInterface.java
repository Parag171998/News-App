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

    @GET("current.json?key=6d0df12f66ef4483bad3908f781308b1")
    Call<WeatherInfo> getWeather( @Query("q") String location );
}
