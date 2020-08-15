package com.example.appyhightask.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.appyhightask.R;
import com.example.appyhightask.adapter.NewsAdapter;
import com.example.appyhightask.models.Article;
import com.example.appyhightask.models.NewsData;
import com.example.appyhightask.viewModel.NewsViewModel;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    List<Article> articleList;
    NewsAdapter newsAdapter;

    private NewsViewModel newsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("News");
        ButterKnife.bind(this);

        initViews();
    }


    private void initViews() {
        articleList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(MainActivity.this, articleList);
        recyclerView.setAdapter(newsAdapter);

        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        newsViewModel.init(Locale.getDefault().getCountry());
        newsViewModel.getTopRatedMoviesMutableLiveData().observe(this, new Observer<NewsData>() {
            @Override
            public void onChanged(NewsData newsData) {
                articleList.addAll(newsData.getArticles());
                newsAdapter.notifyDataSetChanged();
            }
        });

    }

}
