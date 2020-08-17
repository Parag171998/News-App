package com.example.appyhightask.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.appyhightask.models.WeatherInfo;
import com.example.appyhightask.network.NewsApiClient;
import com.example.appyhightask.models.NewsData;
import com.example.appyhightask.network.WeatherApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRepository {

	private final String NEWS_API_KEY = "6d0df12f66ef4483bad3908f781308b1";


	private static MyRepository myRepositoryInstance;
	public static MyRepository getInstance()
	{
		if(myRepositoryInstance == null)
			myRepositoryInstance = new MyRepository();

		return myRepositoryInstance;
	}

	public MutableLiveData<NewsData> getNews(String country){

		final MutableLiveData<NewsData> newData = new MutableLiveData<>();

		Call<NewsData> newsDataCall = NewsApiClient.getInstance().getApi().getHeadlines(country, NEWS_API_KEY);
		newsDataCall.enqueue(new Callback<NewsData>() {
			@Override
			public void onResponse(Call<NewsData> call, Response<NewsData> response) {
				newData.setValue(response.body());
			}

			@Override
			public void onFailure(Call<NewsData> call, Throwable t) {
				t.printStackTrace();
			}
		});

		return newData;
	}

	public MutableLiveData<WeatherInfo> getWeather(String location){

		final MutableLiveData<WeatherInfo> weatherInfoMutableLiveData = new MutableLiveData<>();

		Call<WeatherInfo> weatherInfoCall = WeatherApiClient.getInstance().getApi().getWeather(location);
		weatherInfoCall.enqueue(new Callback<WeatherInfo>() {
			@Override
			public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
				weatherInfoMutableLiveData.setValue(response.body());
			}

			@Override
			public void onFailure(Call<WeatherInfo> call, Throwable t) {

			}
		});

		return weatherInfoMutableLiveData;
	}

}
