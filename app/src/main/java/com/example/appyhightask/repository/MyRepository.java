package com.example.appyhightask.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.appyhightask.network.ApiClient;
import com.example.appyhightask.models.NewsData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRepository {

	private final String API_KEY = "6d0df12f66ef4483bad3908f781308b1";


	private static MyRepository myRepositoryInstance;
	public static MyRepository getInstance()
	{
		if(myRepositoryInstance == null)
			myRepositoryInstance = new MyRepository();

		return myRepositoryInstance;
	}

	public MutableLiveData<NewsData> getNews(String country){

		final MutableLiveData<NewsData> newData = new MutableLiveData<>();

		Call<NewsData> newsDataCall = ApiClient.getInstance().getApi().getHeadlines(country, API_KEY);
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

}
