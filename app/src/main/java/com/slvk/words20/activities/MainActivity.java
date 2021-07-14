package com.slvk.words20.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.slvk.words20.App;
import com.slvk.words20.PairOfWords;
import com.slvk.words20.R;
import com.slvk.words20.adapter.ThemeAdapter;
import com.slvk.words20.database.AppDatabase;
import com.slvk.words20.database.DescriptionDao;
import com.slvk.words20.database.ThemeDescription;
import com.slvk.words20.database.helpers.FirebaseHelper;
import com.slvk.words20.database.helpers.SQLHelper;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    RecyclerView themeRecyclerView;
    ThemeAdapter themeAdapter;
    FirebaseHelper fbHelper;
    DatabaseReference myRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //App.getInstance().getRoomDatabase().getDescriptionDao().deleteTable();
        database = FirebaseDatabase.getInstance();

        db = getBaseContext().openOrCreateDatabase("words.db", MODE_PRIVATE, null);

        fbHelper = new FirebaseHelper(db, this);

        themeAdapter = new ThemeAdapter();

        showListOfThemes();
        setOnThemeClickListener();
    }

    void loadThemes(List<ThemeDescription> themes){
        themeAdapter.SetItems(themes);
    }

    private void initRecyclerView() {
        themeRecyclerView = findViewById(R.id.theme_recycler_view);
        themeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        themeRecyclerView.setAdapter(themeAdapter);
    }

    void setOnThemeClickListener(){
        Intent intent = new Intent(this, TasksList.class);
        ThemeAdapter.OnThemeClickListener themeClickListener = new ThemeAdapter.OnThemeClickListener() {
            @Override
            public void onThemeClick(ThemeDescription theme) {
                intent.putExtra("THEME", theme.getTheme_name());
                startActivity(intent);
            }
        };
        themeAdapter.setOnClickListener(themeClickListener);
    }


    void addAndFillNewGroup(String groupName){
        myRef = database.getReference(groupName);

        for(int i = 0 ; i < 10; i++){
            String ENG = groupName + " ENG " + i;
            String RUS = groupName + " RUS " + i;
            addWord(ENG, RUS, "W"+i, groupName);
        }
    }

    void addWord(String eng, String rus, String word_id, String groupName){
        PairOfWords pair = new PairOfWords(eng, rus);
        myRef = database.getReference(groupName);
        myRef.child(word_id).setValue(pair);
    }

    synchronized void showListOfThemes()  {
        AppDatabase roomDB = App.getInstance().getRoomDatabase();
        DescriptionDao themesDao = roomDB.getDescriptionDao();

        List<ThemeDescription> themes = themesDao.getAllThemes();
        loadThemes(themes);
        initRecyclerView();
    }


}







