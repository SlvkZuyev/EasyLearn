package com.slvk.words20.database;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DescriptionDao{
    // Добавление Person в бд
    @Insert
    void addDescription(ThemeDescription theme);

    // Удаление Person из бд
    @Delete
    void delete(ThemeDescription theme);

    // Получение всех названий тем
    @Query("SELECT theme_name FROM ThemeDescription")
    List<String> getAllThemeNames();

    @Query("SELECT * FROM ThemeDescription")
    List<ThemeDescription> getAllThemes();

    @Query("SELECT table_name FROM ThemeDescription WHERE theme_name = :themeName")
    String getTableIdByThemeName(String themeName);

    @Query("DELETE FROM ThemeDescription")
    void deleteTable();

    @Query("UPDATE ThemeDescription SET imagePath = :imgPath WHERE theme_name =:theme_name")
    void setPath(String imgPath, String theme_name);

    @Query("SELECT imagePath FROM ThemeDescription WHERE theme_name = :themeName")
    String getImagePathByThemeName(String themeName);

}
