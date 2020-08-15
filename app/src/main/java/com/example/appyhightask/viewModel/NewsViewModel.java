package com.example.appyhightask.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appyhightask.models.NewsData;
import com.example.appyhightask.repository.MyRepository;

public class NewsViewModel extends ViewModel {
	private MutableLiveData<NewsData> newsDataMutableLiveData = null;

	private MyRepository myRepository;

	public void init(String country)
	{
		if(newsDataMutableLiveData != null)
			return;

		myRepository = MyRepository.getInstance();
		newsDataMutableLiveData = myRepository.getNews(country);
	}

	public LiveData<NewsData> getTopRatedMoviesMutableLiveData()
	{
		return this.newsDataMutableLiveData;
	}
}
