package com.example.appyhightask.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.appyhightask.models.Article;
import com.example.appyhightask.models.WeatherInfo;

import java.util.List;

@Dao
public interface Mydao {

    @Insert
    public void addFavFood(Article article);

    @Query("select * from Article")
    public List<Article> getFavFoods();

    @Query("DELETE FROM Article")
    public void deleteAll();

    @Query("select * from Article where id = :id")
    public Article chekIfPresent(String id);

    @Delete
    public void deleteFood(Article article);
}
