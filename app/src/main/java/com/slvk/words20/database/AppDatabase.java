package com.slvk.words20.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ThemeDescription.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DescriptionDao getDescriptionDao();
}

