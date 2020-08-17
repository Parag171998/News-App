package com.example.appyhightask.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsApiClient {

    private static final String NEWS_BASE_URL = "https://newsapi.org/v2/";
    private static NewsApiClient newsApiClient;
    private static Retrofit retrofit;

    private NewsApiClient(){
        retrofit = new Retrofit.Builder().baseUrl(NEWS_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized NewsApiClient getInstance(){
        if (newsApiClient == null){
            newsApiClient = new NewsApiClient();
        }
        return newsApiClient;
    }


    public ApiInterface getApi(){
        return retrofit.create(ApiInterface.class);
    }
}