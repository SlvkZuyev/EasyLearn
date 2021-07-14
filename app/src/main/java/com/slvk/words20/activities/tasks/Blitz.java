package com.slvk.words20.activities.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prush.bndrsntchtimer.BndrsntchTimer;
import com.slvk.words20.PairOfWords;
import com.slvk.words20.R;
import com.slvk.words20.activities.Result;
import com.slvk.words20.activities.items.OnTimerElapsedListener;
import com.slvk.words20.activities.items.ProgressCircles;
import com.slvk.words20.activities.items.ShrinkingTimerLine;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Blitz extends BaseTaskActivity {
PairOfWords []words;
TextView wordToTranslate;
TextView wordTranslation;
Boolean translationIsCorrect;
ShrinkingTimerLine timerLine;
ProgressCircles progressCircles;
final int TIME_FOR_ANSWER = 4000;


@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blitz);

        LinearLayout timerLineContainer = (LinearLayout) findViewById(R.id.timer_line_container);
        timerLine = new ShrinkingTimerLine(this, timerLineContainer);

        words = dbHelper.getAllWordsInTheme(themeName);
        numberOfWordsToLearn = words.length;

        wordToTranslate = (TextView)findViewById(R.id.word_to_translate);
        wordTranslation = (TextView)findViewById(R.id.translation);

        timerLine.drawLine();
        timerLine.startAnimation(TIME_FOR_ANSWER);

        initProgressCircles();
        progressCircles.drawEmptyCircles();

        setToolBar();
        setProgressBar();
        setListener();
        showWordAndTranslation();

    }

    void setListener(){
        timerLine.setOnTimeElapsedListener(new OnTimerElapsedListener() {
            @Override
            public void onTimerElapsed() {
                showResult(Result.TypesOftest.BLITZ);
            }
        });
    }

    private void initProgressCircles(){
        ImageView firstCircle = (ImageView)findViewById(R.id.progress_circle_1);
        ImageView secondCircle = (ImageView)findViewById(R.id.progress_circle_2);
        ImageView thirdCircle = (ImageView)findViewById(R.id.progress_circle_3);

        progressCircles = new ProgressCircles(firstCircle, secondCircle, thirdCircle);
    }

    void showWordAndTranslation(){
        wordToTranslate.setText(words[currentWordPosition].ENG);
        translationIsCorrect = getRandomBoolean();
        if(translationIsCorrect){
            wordTranslation.setText(words[currentWordPosition].RUS);
        } else {
            wordTranslation.setText(words[getRandomWordPosition()].RUS);
        }
    }

    private boolean getRandomBoolean(){
        return Math.random() < 0.5;
    }

    private int getRandomWordPosition(){
        Random rand = new Random();
        int randomPosition = 0;
        do{
            randomPosition = rand.nextInt(words.length);
        } while (randomPosition == currentWordPosition);

        return randomPosition;
    }

    public void onClick_TrueOrFalse(View view) {
        currentWordPosition++;
        increasePoints(view);

        if (currentWordPosition < words.length) {
            progressBar.moveNext();
            timerLine.clearAnimation();
            showWordAndTranslation();
            timerLine.startAnimation(TIME_FOR_ANSWER);
        }
        else{
            showResult(Result.TypesOftest.BLITZ);
        }
    }

void increasePoints(View view){
    switch (view.getId()) {
        case R.id.true_button:
            if (translationIsCorrect) {
                answerIsCorrect();
            } else {
                answerIsNotCorrect();
            }
            break;
        case R.id.false_button:
            if (translationIsCorrect) {
                answerIsNotCorrect();
            } else {
                answerIsCorrect();
            }
            break;
    }
}

    public void answerIsCorrect(){
        numberOfCorrectAnswers++;
        progressCircles.setNextCorrect();
        progressBar.setCurrentColor(progressBar.GREEN);
    }

    public void answerIsNotCorrect(){
        numberOfWrongAnswers++;
        progressCircles.setNextNotCorrect();
        progressBar.setCurrentColor(progressBar.RED);
    }
}
