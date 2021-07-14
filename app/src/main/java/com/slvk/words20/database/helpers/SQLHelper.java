package com.slvk.words20.database.helpers;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.slvk.words20.App;
import com.slvk.words20.PairOfWords;
import com.slvk.words20.database.AppDatabase;
import com.slvk.words20.database.DescriptionDao;



import java.util.ArrayList;
import java.util.List;

public class SQLHelper {
    private final String DB_NAME = "words.db";
    private SQLiteDatabase db;
    private int WORDS_TO_LEARN = 10;
    private String[] ALL_COLS = {"eng", "rus"};
    private int ENG_COL = 0;
    private int RUS_COL = 1;
    private List<String> tableNames = new ArrayList<>();
    DescriptionDao themesDao;

    public SQLHelper(SQLiteDatabase db) {
        this.db = db;

        AppDatabase roomDB = App.getInstance().getRoomDatabase();
        themesDao = roomDB.getDescriptionDao();
       // LoadTableNames();
    }

    public boolean ThemeIsLoaded(String theme){
        return tableNames.contains(theme);
    }

    public String[][] GetArrayOfWords(String themeName) {
        String[][] words;

        String tableName = themesDao.getTableIdByThemeName(themeName);
        long table_length = DatabaseUtils.queryNumEntries(db, tableName); //Получаем количестов строк в таблице

        int arrLength = Math.min((int) table_length, WORDS_TO_LEARN);       // Изучаем WORDS_TO_LEARN слов если в таблице они есть

        words = new String[arrLength][2];

        fillArrayWithEmptyStrings(words, arrLength);

        Cursor cursor = null;
        for (int i = 0; i < arrLength; ) { //Копируем из таблицы первые слова
            cursor = getRandomRow(themeName);
            String nextWord = cursor.getString(0);
            if (!Contains(words, nextWord)) {
                words[i][ENG_COL] = nextWord;
                words[i][RUS_COL] = cursor.getString(1);
                i++;
            }
        }

        //arrayToLoverCase(words, arrLength);

        if (cursor != null) {
            cursor.close();
        }
        return words;
    }

    public Cursor getRandomRow(String themeName) {
        String tableName = themesDao.getTableIdByThemeName(themeName);
        Cursor c = db.query(true, tableName, ALL_COLS,
                null, null, null, null, "RANDOM()", "1");

        if (c != null) {
            c.moveToFirst();
        }
        return c;

    }

    private boolean Contains(String[][] arr, String word) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            if (arr[i][ENG_COL].equals(word)) {
                return true;
            }
        }
        return false;
    }

    private void fillArrayWithEmptyStrings(String[][] arr, int length) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < 2; j++) {
                arr[i][j] = "";
            }
        }
    }

    //Сохраняет пару слово-перевод в таблицу
    public void SavePair(PairOfWords pair, String table_name){
        String st = "INSERT INTO '" + table_name + "' VALUES ('" + pair.ENG + "', '" + pair.RUS + "');";
        db.execSQL(st);
    }

    public void AddTable(String tableName) {
        String st = "CREATE TABLE IF NOT EXISTS '" + tableName + "' (eng TEXT, rus TEXT)";
        db.execSQL(st);

        if(!tableNames.contains(tableName)){
            tableNames.add(tableName);
        }
    }


    public void deleteTable(String tableName){
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    private void arrayToLoverCase(String[][] arr, int length) {
        for (int i = 0; i < length; i++) {
            arr[i][RUS_COL] = arr[i][RUS_COL].toLowerCase();
            arr[i][ENG_COL] = arr[i][ENG_COL].toLowerCase();
        }
    }

    public int getNumberOfWordsToLearn() {
        return WORDS_TO_LEARN;
    }

    void deleteAllFromTable(String tableName) {
        db.execSQL("delete from '" + tableName + "'");
    }

    public PairOfWords[] getAllWordsInTheme(String themeName){

        String tableName = themesDao.getTableIdByThemeName(themeName);
        int table_length = (int)DatabaseUtils.queryNumEntries(db, tableName); //Получаем количестов строк в таблице

        PairOfWords []words = new PairOfWords[table_length];

        //fillArrayWithEmptyStrings(words, arrLength);

        Cursor cursor = db.query(tableName, ALL_COLS, null, null, null, null, "RANDOM()");
        cursor.moveToFirst();
        for (int i = 0; i < table_length; i++) { //Копируем из таблицы первые слова
            words[i] = new PairOfWords();
            words[i].ENG = cursor.getString(ENG_COL);
            words[i].RUS = cursor.getString(RUS_COL);
            cursor.moveToNext();
        }

        if (cursor != null) {
            cursor.close();
        }
        return words;
    }

}
