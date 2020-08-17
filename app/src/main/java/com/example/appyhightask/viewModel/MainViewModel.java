package com.example.appyhightask.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appyhightask.models.NewsData;
import com.example.appyhightask.models.WeatherInfo;
import com.example.appyhightask.repository.MyRepository;

public class MainViewModel extends ViewModel {
	private MutableLiveData<NewsData> newsDataMutableLiveData = null;
	private MutableLiveData<WeatherInfo> weatherInfoMutableLiveData = null;

	private MyRepository myRepository;

	public void init(String country)
	{
		if(newsDataMutableLiveData != null)
			return;

		myRepository = MyRepository.getInstance();
		newsDataMutableLiveData = myRepository.getNews(country);
	}

	public void initLocation(String location){
		weatherInfoMutableLiveData = myRepository.getWeather(location);
	}

	public LiveData<NewsData> getHeadlines()
	{
		return this.newsDataMutableLiveData;
	}

	public LiveData<WeatherInfo> getWeatherInfo()
	{
		return weatherInfoMutableLiveData;
	}
}
