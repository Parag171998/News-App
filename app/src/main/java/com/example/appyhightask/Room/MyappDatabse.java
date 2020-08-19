package com.example.appyhightask.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.appyhightask.models.Article;
import com.example.appyhightask.models.WeatherInfo;


@Database(entities = {Article.class},version = 1)
public abstract class MyappDatabse extends RoomDatabase {

    public abstract Mydao mydao();
    private static MyappDatabse INSTANCE;

    public static MyappDatabse getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyappDatabse.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyappDatabse.class, "articles")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
