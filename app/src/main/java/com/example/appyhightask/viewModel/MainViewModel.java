package com.example.appyhightask.viewModel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appyhightask.models.Article;
import com.example.appyhightask.models.NewsData;
import com.example.appyhightask.models.WeatherInfo;
import com.example.appyhightask.repository.MyRepository;

import java.util.List;

public class MainViewModel extends ViewModel {
	private MutableLiveData<NewsData> newsDataMutableLiveData = null;
	private MutableLiveData<WeatherInfo> weatherInfoMutableLiveData = null;
	private MutableLiveData<List<Article>> articleMutableLiveData = null;

	private MyRepository myRepository;

	public void init(String country, Application application)
	{
		if(newsDataMutableLiveData != null)
			return;

		myRepository = MyRepository.getInstance();
		myRepository.initRoomArticleDb(application);
		newsDataMutableLiveData = myRepository.getNews(country);
		articleMutableLiveData = myRepository.getRoomArticles();
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

	public LiveData<List<Article>> getRoomArticles(){
		return articleMutableLiveData;
	}
}
