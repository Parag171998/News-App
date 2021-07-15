package com.example.appyhightask.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appyhightask.R;
import com.example.appyhightask.adapter.NewsAdapter;
import com.example.appyhightask.models.Article;
import com.example.appyhightask.viewModel.MainViewModel;
import com.google.gson.Gson;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends AppCompatActivity implements NewsAdapter.ItemCallBack {

    public static String SAVED_LIST = "saved_list";

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.searchView)
    SearchView searchView;
    List<Article> articleList;
    List<Article> savedArticleList;
    NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("News");
        ButterKnife.bind(this);
        initViews();
        setSearchView();
    }

    private void setSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });
    }

    void filter(String text){
        List<Article> temp = new ArrayList();
        for(Article d: articleList){
            if(d.getTitle().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        newsAdapter.updateList(temp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String saveListJson = new Gson().toJson(savedArticleList);
        if (id == R.id.mybutton) {
            Intent intent = new Intent(getApplicationContext(), SavedNewsActivity.class);
            intent.putExtra(SAVED_LIST, saveListJson);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        savedArticleList = new ArrayList<>();
        articleList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(MainActivity.this, articleList, this);
        recyclerView.setAdapter(newsAdapter);

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.init(getUserCountry(), getApplication());

        if (isNetworkConnected()) {
            if (isInternetAvailable()) {
                mainViewModel.getHeadlines().observe(this, newsData -> {
                    articleList.addAll(newsData.getArticles());
                    newsAdapter.notifyDataSetChanged();
                });
            } else {
                mainViewModel.getRoomArticles().observe(this, articles -> {
                    articleList.addAll(articles);
                    newsAdapter.notifyDataSetChanged();
                });
            }
        } else {
            mainViewModel.getRoomArticles().observe(this, articles -> {
                articleList.addAll(articles);
                newsAdapter.notifyDataSetChanged();

            });
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public String getUserCountry() {
        try {
            final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void onItemClick(int position) {
        savedArticleList.add(articleList.get(position));
    }
}
