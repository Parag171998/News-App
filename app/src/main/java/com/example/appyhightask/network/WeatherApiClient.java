package com.example.appyhightask.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiClient {

	private static final String WEATHER_BASE_URL = "https://api.weatherapi.com/v1/";
	private static WeatherApiClient weatherApiClient;
	private static Retrofit retrofit;

	private WeatherApiClient(){
		retrofit = new Retrofit.Builder().baseUrl(WEATHER_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
	}

	public static synchronized WeatherApiClient getInstance(){
		if (weatherApiClient == null){
			weatherApiClient = new WeatherApiClient();
		}
		return weatherApiClient;
	}


	public ApiInterface getApi(){
		return retrofit.create(ApiInterface.class);
	}
}
