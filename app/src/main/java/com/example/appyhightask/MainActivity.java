package com.example.appyhightask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity{

    String country;
    String API_KEY = "6d0df12f66ef4483bad3908f781308b1";

    RecyclerView recyclerView;
    List<Article> articleList;
    NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenResult result) {
                        JSONObject data = result.notification.payload.additionalData;
                        String launchURL;
                        if(data != null)
                        {
                            launchURL = data.optString("link", null);
                            Intent intent = new Intent(getApplicationContext(), NewsFeed.class);
                            intent.putExtra("newsUrl", launchURL);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Invalid Link", Toast.LENGTH_SHORT).show();
                        }
                 }
                })
                .init();

        setTitle("News");
        recyclerView = findViewById(R.id.recyclerview);
        articleList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        country = getCountry();
        retrieveJson(country, API_KEY);
    }

    public void retrieveJson(String country, String apiKey) {

        Call<NewsData> newsDataCall = ApiClient.getInstance().getApi().getHeadlines(country, apiKey);
        newsDataCall.enqueue(new Callback<NewsData>() {
            @Override
            public void onResponse(Call<NewsData> call, Response<NewsData> response) {

                NewsData newsData = response.body();
                articleList.addAll(newsData.getArticles());

                newsAdapter = new NewsAdapter(MainActivity.this, articleList);

                recyclerView.setAdapter(newsAdapter);
            }

            @Override
            public void onFailure(Call<NewsData> call, Throwable t) {

            }
        });
    }

    public String getCountry() {
        try {
            final TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

}
