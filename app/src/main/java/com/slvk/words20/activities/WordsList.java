package com.slvk.words20.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.slvk.words20.App;
import com.slvk.words20.PairOfWords;
import com.slvk.words20.R;
import com.slvk.words20.adapter.WordsAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class WordsList extends AppCompatActivity {
private String themeName;
private RecyclerView wordRecyclerView;
private WordsAdapter wordsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list);

        themeName = (String)getIntent().getSerializableExtra("THEME");
        wordsAdapter = new WordsAdapter();
        setToolBar();
        showWords();
    }

    private void showWords(){
        ArrayList<PairOfWords> words = new ArrayList<>();
        Collections.addAll(words, App.getSQLHelper().getAllWordsInTheme(themeName));

        wordsAdapter.setItems(words);
        initRecyclerView();
    }

    private void initRecyclerView() {
        wordRecyclerView = findViewById(R.id.words_list);
        wordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        wordRecyclerView.setAdapter(wordsAdapter);
    }

    protected void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(themeName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.word_actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.white_close_cross) {
            finish();
        }
        return true;
    }

    public void onClick_ToTasks(View view) {
        Intent intent = new Intent(this, TasksList.class);
        intent.putExtra("THEME", themeName);
        startActivity(intent);
    }
}
