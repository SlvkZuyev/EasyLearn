package com.slvk.words20.activities.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.slvk.words20.App;
import com.slvk.words20.R;
import com.slvk.words20.activities.Result;
import com.slvk.words20.activities.items.ProgressBar;
import com.slvk.words20.database.helpers.SQLHelper;

public abstract class BaseTaskActivity extends AppCompatActivity {

    public SQLHelper dbHelper;
    public ProgressBar progressBar;
    public String[][] wordsToLearn; //Array with pair of words
    public String themeName;
    public final int ENG = 0;
    public final int RUS = 1;

    public int numberOfWordsToLearn; //Whole number of words to learn in group
    public int currentWordPosition;  //Position of word that is displayed now
    public int numberOfCorrectAnswers;
    public int numberOfWrongAnswers;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentWordPosition = 0;
        numberOfCorrectAnswers = 0;
        numberOfWrongAnswers = 0;

        themeName = (String) getIntent().getSerializableExtra("THEME");

        dbHelper = App.getSQLHelper();
        wordsToLearn = dbHelper.GetArrayOfWords(themeName);
        numberOfWordsToLearn = wordsToLearn.length;

    }

    protected void setProgressBar(int numberOfItems) {
        progressBar = new ProgressBar(this, numberOfItems);
        LinearLayout linearLayout = findViewById(R.id.progress_bar_container);
        if(linearLayout != null) {
            progressBar.drawRectangles(linearLayout);
        } else {
            throw new NullPointerException("Linear layout should have id: progress_bar_container");
        }
    }

    protected void setProgressBar(){
        setProgressBar(numberOfWordsToLearn);
    }



    protected void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(themeName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.close_cross) {
            finish();
        }
        return true;
    }

    public void OnClick_GoNext(View view) {
        progressBar.moveNext();
    }

    protected void showResult(Result.TypesOftest typeOfCurrentTest) {
        Intent intent = new Intent(this, Result.class);
        intent.putExtra("THEME", themeName);
        intent.putExtra("CORRECT", numberOfCorrectAnswers);
        intent.putExtra("NOT_CORRECT", numberOfWrongAnswers);
        intent.putExtra("NUMBER_OF_QUESTIONS", numberOfWordsToLearn);
        intent.putExtra("TYPE", typeOfCurrentTest);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void startFateInAnimation(View view){
        Animation animFateIn = AnimationUtils.loadAnimation(this, R.anim.fate_in);
        //animFateIn.setFillAfter(true);
        view.startAnimation(animFateIn);
    }

    public void startFateOutAnimation(View view){
        Animation animFateOut = AnimationUtils.loadAnimation(this, R.anim.fate_out);
        view.startAnimation(animFateOut);
    }
}
