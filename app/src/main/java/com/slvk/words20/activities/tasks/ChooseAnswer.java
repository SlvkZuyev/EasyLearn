package com.slvk.words20.activities.tasks;

import androidx.cardview.widget.CardView;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slvk.words20.DisplayHelper;
import com.slvk.words20.R;
import com.slvk.words20.activities.Result;

import java.util.Random;

public class ChooseAnswer extends BaseTaskActivity {
    final int NUMBER_OF_ANSWERS = 5; //Количество ваирантов ответов
    int QUESTION_COL;
    int ANSWER_COL;
    //ImageView[] correctCircles = new ImageView[NUMBER_OF_ANSWERS];
    //ImageView[] notCorrectCircles = new ImageView[NUMBER_OF_ANSWERS];

    //String themeName;
    //String[][] wordsToLearn;
    //SQLiteDatabase db;
    int answerPosition = 0;
    int currentWordPos = 0;
    int pressedPosition = 0;
    //int numberOfCorrectAnswers = 0;
    //int numberOfWrongAnswers = 0;
    //SQLHelper dbHelper;
    final int ENG_COL  = 0;
    final int RUS_COL = 1;
    Button[] Buttons = new Button[5];

    //ProgressLine progressLine;
    Button goNextButton;
    TextView currentWord;

    public enum Type{
        ENG_RUS,
        RUS_ENG;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_answer);

        //themeName = (String)getIntent().getSerializableExtra("THEME");
        //dbHelper = App.getSQLHelper();

        currentWord = (TextView)findViewById(R.id.word);
        goNextButton = (Button)findViewById(R.id.next_button);
        hideGoNextButton();

        Type type = (Type)getIntent().getSerializableExtra("TYPE");
        setQuestionAndAnswerColumns(type);

        //wordsToLearn = dbHelper.GetArrayOfWords(themeName); //Получаем группу слов для изучения
        setToolBar();
        initButtons();
        setProgressBar();
        setQuestion();
        setAnswers();
    }



    private void setQuestionAndAnswerColumns(Type type){
        if(type == Type.ENG_RUS){
            QUESTION_COL = 0;
            ANSWER_COL = 1;
        }
        else{
            QUESTION_COL = 1;
            ANSWER_COL = 0;
        }
    }

    private void initButtons(){
        Buttons[0] = (Button)findViewById(R.id.answer0);
        Buttons[1] = (Button)findViewById(R.id.answer1);
        Buttons[2] = (Button)findViewById(R.id.answer2);
        Buttons[3] = (Button)findViewById(R.id.answer3);
        Buttons[4] = (Button)findViewById(R.id.answer4);
    }

    private boolean Contains(String[] arr, String word){
        int length = arr.length;
        for (int i = 0; i < length; i++) {
                if (arr[i].equals(word)) {
                    return true;
            }
        }
        return false;
    }

    public void OnClick_GoNext(View view){
        currentWordPos++;

        if(currentWordPos == wordsToLearn.length){ //Если это последнее слово в списке
            showResult(Result.TypesOftest.BUILD_THE_WORD);
            return;
        }

        setButtonsEnabled(true);
        setButtonsDefaultColor();

        setQuestion();
        setAnswers();

        progressBar.moveNext();
    }

    private void showGoNextButton(){
        goNextButton.setEnabled(true);
        goNextButton.setVisibility(View.VISIBLE);
        startFateInAnimation(goNextButton);
    }

    private void hideGoNextButton(){
        goNextButton.setEnabled(false);
        goNextButton.setVisibility(View.INVISIBLE);
        startFateOutAnimation(goNextButton);
    }

    private void setAnswers(){
        Cursor c;
        String answer;

        String[] answers = new String[NUMBER_OF_ANSWERS];
        for(int i = 0; i < NUMBER_OF_ANSWERS; i++){
            answers[i] = "";
        }

        for(int i = 0; i < NUMBER_OF_ANSWERS;){

            c = dbHelper.getRandomRow(themeName);
            answer = c.getString(ANSWER_COL);

            if(!answer.equals(wordsToLearn[currentWordPos][ANSWER_COL])) { //Если слово не является ответом
                if(!Contains(answers, answer)) { //Если такого варианта ответа еще нет
                    Buttons[i].setText(answer);
                    answers[i] = answer;
                    i++;
                }
            }
        }

        Random rand = new Random();
        answerPosition = rand.nextInt(5);
        Buttons[answerPosition].setText(wordsToLearn[currentWordPos][ANSWER_COL]);
        hideGoNextButton();
    }

    private void setQuestion(){
        currentWord.setText(wordsToLearn[currentWordPos][QUESTION_COL]);
    }

    private void setButtonsDefaultColor(){
        Buttons[pressedPosition].setBackgroundResource(R.drawable.rnd_rect_blue);
        Buttons[answerPosition].setBackgroundResource(R.drawable.rnd_rect_blue);
    }

    //Переход к следующему слову
    public void GoToNextWord(View view) {

        setButtonsEnabled(true);

        Buttons[pressedPosition].setBackgroundResource(R.drawable.rnd_rect_blue);
        Buttons[answerPosition].setBackgroundResource(R.drawable.rnd_rect_blue);

        if(currentWordPos == wordsToLearn.length){ //Если это последнее слово в списке
            showResult(Result.TypesOftest.BUILD_THE_WORD);
            return;
        }

        Cursor c;
        String answer;
        currentWord.setText(wordsToLearn[currentWordPos][QUESTION_COL]);

        String[] answers = new String[NUMBER_OF_ANSWERS];
        for(int i = 0; i < NUMBER_OF_ANSWERS; i++){
            answers[i] = "";
        }

        for(int i = 0; i < NUMBER_OF_ANSWERS;){

            c = dbHelper.getRandomRow(themeName);
            answer = c.getString(ANSWER_COL);
            if(!answer.equals(wordsToLearn[currentWordPos][ANSWER_COL])) { //Если слово не является ответом
               if(!Contains(answers, answer)) { //Если такого варианта ответа еще нет
                    Buttons[i].setText(answer);
                    answers[i] = answer;
                    i++;
               }
           }
        }

        Random rand = new Random();
        answerPosition = rand.nextInt(5);
        Buttons[answerPosition].setText(wordsToLearn[currentWordPos][ANSWER_COL]);
        hideGoNextButton();

        currentWordPos++;

    }

    //При выборе ответа
    public void buttonPress(View v) {

        pressedPosition = getButtonPosition(v); //Получаем номер нажатой кнопки
        showGoNextButton();

        Buttons[answerPosition].setBackgroundResource(R.drawable.rnd_rect_green);
        if(pressedPosition == answerPosition){ //Если ответ верный
            numberOfCorrectAnswers++;
            progressBar.setCurrentColor(progressBar.GREEN);
        }
        else { //Ели ответ не верный
            numberOfWrongAnswers++;
            progressBar.setCurrentColor(progressBar.RED);
            Buttons[pressedPosition].setBackgroundResource(R.drawable.rnd_rect_red);
        }
        setButtonsEnabled(false); //Делаем все кнопки нерабочими
        Buttons[pressedPosition].setEnabled(true); //Кроме той которую нажали
    }

/*
    private void ShowResult(){
        Intent intent = new Intent(ChooseAnswer.this, Result.class);
        intent.putExtra("TABLE", themeName);
        intent.putExtra("CORRECT", numberOfCorrectAnswers);
        intent.putExtra("NOT_CORRECT", numberOfWrongAnswers);
        intent.putExtra("TYPE", Result.TypesOftest.ENG_RUS);
        startActivity(intent);
    }

 */

    //Включает или выклюлчает все кнопки
   private void setButtonsEnabled (boolean state){
        for(int i = 0; i < NUMBER_OF_ANSWERS; i++){
            Buttons[i].setEnabled(state);
        }
    }

    //Возвращает номер нажатой кнопки
    private int getButtonPosition(View v){
        switch (v.getId()) {
            case R.id.answer0:
                return 0;
            case R.id.answer1:
                return 1;
            case R.id.answer2:
                return 2;
            case R.id.answer3:
                return 3;
            case R.id.answer4:
                return 4;
            default:
                return -1;
        }
    }

    CardView cardView;
    LinearLayout.LayoutParams layoutParams;
    LinearLayout linearLayout;
    TextView textView;

    public void CreateCardViewProgrammatically(Context context) {

        ShapeDrawable rect = new ShapeDrawable(new RectShape());
        rect.setIntrinsicHeight(2);
        rect.setIntrinsicWidth(100);
        rect.getPaint().setColor(Color.MAGENTA);

        ImageView iView = new ImageView(context);
        iView.setImageDrawable(rect);

        linearLayout.addView(iView);
    }

    int getWidthOfRectangle(int num_of_rectangles){
        int screen_with = getResources().getDisplayMetrics().widthPixels;

        int rectangle_with = screen_with / num_of_rectangles;
        rectangle_with -= DisplayHelper.dpToPixel(4, this);
        return rectangle_with;
    }

    void drawRectangles(int numOfRectangles, Context context){
        for(int i = 0; i < numOfRectangles; i++) {
            ShapeDrawable rect = new ShapeDrawable(new RectShape());
            int with = getWidthOfRectangle(numOfRectangles);
            rect.setIntrinsicHeight(5);
            rect.setIntrinsicWidth(with);
            rect.getPaint().setColor(Color.GRAY);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(5, 0, 5, 0);

            ImageView iView = new ImageView(context);
            iView.setLayoutParams(params);
            iView.setImageDrawable(rect);

            linearLayout.addView(iView);
        }
    }

}
