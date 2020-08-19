package com.example.appyhightask.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.appyhightask.Room.MyappDatabse;
import com.example.appyhightask.Room.Mydao;
import com.example.appyhightask.models.Article;
import com.example.appyhightask.models.WeatherInfo;
import com.example.appyhightask.network.NewsApiClient;
import com.example.appyhightask.models.NewsData;
import com.example.appyhightask.network.WeatherApiClient;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRepository {

	private final String NEWS_API_KEY = "6d0df12f66ef4483bad3908f781308b1";
	private Mydao mydao;

	private static MyRepository myRepositoryInstance;
	public static MyRepository getInstance()
	{
		if(myRepositoryInstance == null)
			myRepositoryInstance = new MyRepository();

		return myRepositoryInstance;
	}

	public void initRoomArticleDb(Application application){
		MyappDatabse db = MyappDatabse.getDatabase(application);
		mydao = db.mydao();
	}

	public MutableLiveData<List<Article>> getRoomArticles(){
		try {
			return new MutableLiveData<>(new getRoomDataAsyncTask(mydao).execute().get());
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void insertInRoomArticles (List<Article> articles) {
		if(mydao != null) {
			new insertAsyncTask(mydao).execute(articles);
		}
	}

	private static class insertAsyncTask extends AsyncTask<List<Article>, Void, Void> {

		private Mydao mAsyncTaskDao;

		insertAsyncTask(Mydao dao) {
			mAsyncTaskDao = dao;
		}

		@Override
		protected Void doInBackground(List<Article>... lists) {
			mAsyncTaskDao.deleteAll();
			for(Article article : lists[0]){
				mAsyncTaskDao.addFavFood(article);
			}
			return null;
		}
	}

	private static class getRoomDataAsyncTask extends AsyncTask<Void, Void, List<Article> >{

		Mydao mydao;

		public getRoomDataAsyncTask(Mydao db) {
			this.mydao = db;
		}

		@Override
		protected List<Article> doInBackground(Void... voids) {
			return mydao.getFavFoods();
		}

		@Override
		protected void onPostExecute(List<Article> articles) {
			returnMethod(articles);
		}

		public List<Article> returnMethod(List<Article> articles) {
			return articles;
		}
	}

	public MutableLiveData<NewsData> getNews(String country){

		final MutableLiveData<NewsData> newData = new MutableLiveData<>();

		Call<NewsData> newsDataCall = NewsApiClient.getInstance().getApi().getHeadlines(country, NEWS_API_KEY);
		newsDataCall.enqueue(new Callback<NewsData>() {
			@Override
			public void onResponse(Call<NewsData> call, Response<NewsData> response) {
				newData.setValue(response.body());
				insertInRoomArticles(response.body().getArticles());
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
