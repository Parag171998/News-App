package com.example.appyhightask.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.appyhightask.R;
import com.example.appyhightask.adapter.NewsAdapter;
import com.example.appyhightask.models.Article;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedNewsActivity extends AppCompatActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_news);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        Gson gson = new Gson();
        Intent intent = getIntent();
        List<Article> articleList = gson.fromJson(intent.getStringExtra(MainActivity.SAVED_LIST), new TypeToken<List<Article>>(){}.getType());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(SavedNewsActivity.this, articleList, null);
        recyclerView.setAdapter(newsAdapter);
    }
}