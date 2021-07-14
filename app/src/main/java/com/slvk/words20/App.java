package com.slvk.words20;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Room;

import com.slvk.words20.database.AppDatabase;
import com.slvk.words20.database.helpers.SQLHelper;


public class App extends Application {

    public static App instance;
    private static SQLiteDatabase SQLiteDb;
    private AppDatabase roomDb;
    private static SQLHelper sqlHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        roomDb = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "words").fallbackToDestructiveMigration().allowMainThreadQueries().build();

        SQLiteDb = getBaseContext().openOrCreateDatabase("words.db", MODE_PRIVATE, null);
        sqlHelper = new SQLHelper(SQLiteDb);

    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getRoomDatabase() {
        return roomDb;
    }

    public static SQLiteDatabase getSQLiteDatabase() {
        return SQLiteDb;
    }

    public static SQLHelper getSQLHelper() {
        return sqlHelper;
    }
}