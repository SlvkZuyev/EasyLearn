package com.slvk.words20.database;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

@Entity
public class ThemeDescription {
    @PrimaryKey(autoGenerate = true)
    int theme_id;
    String table_name;
    String theme_name;
    String theme_description;
    String imageURL;
    String imagePath;

    public ThemeDescription(String theme_name, String theme_description, String imageURL){
        this.theme_name = theme_name;
        this.theme_description = theme_description;
        this.imageURL = imageURL;

        //todo написать нормальное регулярное выражение
        this.table_name = theme_name.replaceAll("\\s","");
        this.table_name = table_name.replaceAll("\\d","");
    }

    public ThemeDescription(){

    }



    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(int theme_id) {
        this.theme_id = theme_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTheme_description() {
        return theme_description;
    }

    public void setTheme_description(String theme_description) {
        this.theme_description = theme_description;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
        this.table_name = theme_name.replaceAll("\\s","");
        this.table_name = table_name.replaceAll("\\d","");

    }

    @Override
    public boolean equals(Object object){
        if(object instanceof ThemeDescription){
            //Если совпадают названия тем, темы одинаковый
            return this.theme_name.equals(((ThemeDescription) object).theme_name);
        }
        return false;
    }


    static final Migration MIGRATION_2_3 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE ThemeDescription "
                    + " ADD COLUMN imagePath TEXT");
        }
    };




}


