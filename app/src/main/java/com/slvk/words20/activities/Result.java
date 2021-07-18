package com.slvk.words20.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slvk.words20.R;
import com.slvk.words20.activities.tasks.Blitz;
import com.slvk.words20.activities.tasks.BuildTheWord;
import com.slvk.words20.activities.tasks.ChooseAnswer;
import com.slvk.words20.activities.tasks.TypeTheWord;

public class Result extends AppCompatActivity {
    int correctAnswersNum;
    int notCorrectAnswersNum;
    int numberOfWordsToLearn;
    String themeName;
    TypesOftest type;

    public enum TypesOftest {
        ENG_RUS,
        RUS_ENG,
        BUILD_THE_WORD,
        TYPE_THE_WORD,
        BLITZ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        correctAnswersNum = (int) getIntent().getSerializableExtra("CORRECT");
        notCorrectAnswersNum = (int) getIntent().getSerializableExtra("NOT_CORRECT");
        numberOfWordsToLearn = (int) getIntent().getSerializableExtra("NUMBER_OF_QUESTIONS");
        themeName = (String) getIntent().getSerializableExtra("THEME");
        type = (TypesOftest) getIntent().getSerializableExtra("TYPE");

        //answersNum = correctAnswersNum + notCorrectAnswersNum;

        setStatisticPhrase(correctAnswersNum, numberOfWordsToLearn);
        SetTopPhrase(correctAnswersNum, notCorrectAnswersNum);
        setPercent(correctAnswersNum, numberOfWordsToLearn);
    }

    void setStatisticPhrase(int correctAnswersNum, int wholeAnswersNum) {
        TextView statPhrase = (TextView) findViewById(R.id.correct_and_incorrect);
        String stat_str = getResources().getString(R.string.statistic_string, correctAnswersNum, wholeAnswersNum);
        statPhrase.setText(stat_str);
    }

    public void SetTopPhrase(int corr, int notCorr) {
        TextView top_Phrase = (TextView) findViewById(R.id.motivating_text);
        if (notCorr == 0) {
            top_Phrase.setText("Идеально");
            return;
        }

        if (corr == 0) {
            top_Phrase.setText("Очень плохо");
            return;
        }

        if (corr > notCorr) {
            top_Phrase.setText("Неплохо");
            return;
        }

        if (corr < notCorr) {
            top_Phrase.setText("Ты можешь лучше");
            return;
        }

        if (corr == notCorr) {
            top_Phrase.setText("Средненько");
            return;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setPercent(int correctAnswersNum, int answersNum) {
        int percent = (correctAnswersNum * 100 / answersNum);
        TextView percentText = findViewById(R.id.percent_text);
        percentText.setText(percent + "%");
        setColorOfPercentCircle(percent);
    }

    private void setColorOfPercentCircle(int percent) {
        ImageView result_circle = (ImageView) findViewById(R.id.percent_circle);
        Drawable circle;
        if (percent < 30) {
            circle = getResources().getDrawable(R.drawable.red_result_circle);
        } else if (percent < 70) {
            circle = getResources().getDrawable(R.drawable.yellow_result_circle);
        } else {
            circle = getResources().getDrawable(R.drawable.green_result_circle);
        }

        result_circle.setImageDrawable(circle);
    }


    public void OnClick_TryAgain(View view) {
        Intent intent;
        switch (type) {
            case ENG_RUS:
                intent = new Intent(this, ChooseAnswer.class);
                intent.putExtra("THEME", themeName);
                startActivity(intent);
                break;
            case RUS_ENG:
                intent = new Intent(this, ChooseAnswer.class);
                intent.putExtra("THEME", themeName);
                intent.putExtra("TYPE", ChooseAnswer.Type.RUS_ENG);
                startActivity(intent);
            case TYPE_THE_WORD:
                intent = new Intent(this, TypeTheWord.class);
                intent.putExtra("THEME", themeName);
                startActivity(intent);
                break;
            case BUILD_THE_WORD:
                intent = new Intent(this, BuildTheWord.class);
                intent.putExtra("THEME", themeName);
                startActivity(intent);
                break;
            case BLITZ:
                intent = new Intent(this, Blitz.class);
                intent.putExtra("THEME", themeName);
                startActivity(intent);
                break;
        }
    }

    public void OnClick_GoBack(View view) {
        Intent intent = new Intent(this, ThemesList.class);
        startActivity(intent);
    }
}
