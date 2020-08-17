package com.example.appyhightask.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.appyhightask.R;
import com.example.appyhightask.adapter.NewsAdapter;
import com.example.appyhightask.models.Article;
import com.example.appyhightask.models.WeatherInfo;
import com.example.appyhightask.utils.LocationHelper;
import com.example.appyhightask.viewModel.MainViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    private static final int LOCATION_REQUEST_CODE = 101;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.text_temp_c)
    TextView tempC;
    @BindView(R.id.text_summery)
    TextView summery;
    @BindView(R.id.txt_temp_f)
    TextView tempF;
    @BindView(R.id.text_country)
    TextView country;
    @BindView(R.id.txt_meassage)
    TextView txtMessage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    List<Article> articleList;
    NewsAdapter newsAdapter;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private MainViewModel mainViewModel;
    private Location currentLocation;
    private LocationHelper locationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("News");
        ButterKnife.bind(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        currentLocation = null;
        initViews();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        startLocation();
    }


    private void initViews() {
        articleList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(MainActivity.this, articleList);
        recyclerView.setAdapter(newsAdapter);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.init(getUserCountry());

        mainViewModel.getHeadlines().observe(this, newsData -> {
            articleList.addAll(newsData.getArticles());
            newsAdapter.notifyDataSetChanged();
        });

    }

    private void startLocation() {
        txtMessage.setText(getString(R.string.text_message));
        tempF.setVisibility(View.INVISIBLE);
        summery.setVisibility(View.INVISIBLE);
        tempC.setVisibility(View.INVISIBLE);
        country.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        txtMessage.setVisibility(View.VISIBLE);

        locationHelper = new LocationHelper(this);
        locationHelper.turnGPSOn(enabled -> {
            // turn on GPS
            if (enabled) {
                fetchLastLocation();
            } else {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                fetchLastLocation();
            }
        }else if (resultCode == Activity.RESULT_CANCELED) {
            txtMessage.setText(getString(R.string.text_message_try));
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.txt_meassage)
    public void tryAgain(){
        startLocation();
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;

                mainViewModel.initLocation(String.valueOf(currentLocation.getLatitude()+","+currentLocation.getLongitude()));
                mainViewModel.getWeatherInfo().observe(this, weatherInfo -> {
                    if(weatherInfo != null) {
                        initWeatherInfo(weatherInfo);
                    }
                });

            } else {
                requestLocationUpdates();
               
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                startLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates() {
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                if(currentLocation == null) {
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            fetchLastLocation();
                        }
                    }
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationHelper.getLocationRequest(), mLocationCallback, null);
    }

    @SuppressLint("SetTextI18n")
    private void initWeatherInfo(WeatherInfo weatherInfo) {
        tempF.setVisibility(View.VISIBLE);
        summery.setVisibility(View.VISIBLE);
        tempC.setVisibility(View.VISIBLE);
        country.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        txtMessage.setVisibility(View.INVISIBLE);

        tempF.setText(weatherInfo.getCurrent().getTempC() + " *C / " + weatherInfo.getCurrent().getTempF() + " *C");
        summery.setText(weatherInfo.getCurrent().getCondition().getText());
        tempC.setText(String.valueOf(weatherInfo.getCurrent().getTempC()) +" *C in " + weatherInfo.getLocation().getName());
        country.setText(weatherInfo.getLocation().getCountry());
    }

    public String getUserCountry() {
        try {
            final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }
}
