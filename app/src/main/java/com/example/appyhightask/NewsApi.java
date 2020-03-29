package com.example.appyhightask;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApi {
    //6d0df12f66ef4483bad3908f781308b1
    String baseUrl = "http://newsapi.org/v2/";

    @GET("top-headlines?country=ie&category=business&apiKey=6d0df12f66ef4483bad3908f781308b1")
    Call<NewsData> getRecentNews();
}
