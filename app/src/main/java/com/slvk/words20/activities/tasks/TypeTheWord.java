package com.slvk.words20.activities.tasks;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.slvk.words20.R;
import com.slvk.words20.activities.Result;

import javax.crypto.Cipher;

public class TypeTheWord extends BaseTaskActivity {

    String typedWord = "";
    TextView correctTranslate;
    TextView wordToTranslate;
    EditText typedWord_EditText;
    Button checkButton;
    Button goNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_the_word);

        setToolBar();
        setProgressBar();

        typedWord_EditText = (EditText) findViewById(R.id.typed_word);
        correctTranslate = (TextView) findViewById(R.id.correct_translate);
        wordToTranslate = (TextView) findViewById(R.id.word_to_translate);
        checkButton = (Button) findViewById(R.id.check_button);
        goNextButton = (Button) findViewById(R.id.next_button);
        setEditTextListener();
        wordToTranslate.setText(wordsToLearn[currentWordPosition][RUS]);
    }

    void setEditTextListener() {
        typedWord_EditText.setOnKeyListener(
                new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            onClick_Check(v);
                            return true;
                        }
                        return false;
                    }
                }
        );

    }

    @Override
    public void OnClick_GoNext(View view) {
        //super.OnClick_GoNext(view);
        if (currentWordPosition == numberOfWordsToLearn - 1) {
            showResult(Result.TypesOftest.TYPE_THE_WORD);
        }
        showCheckButton();
        progressBar.moveNext();
        showNextWord();
    }

    public void onClick_Check(View view) {
        showCorrectTranslate();
        typedWord = getTypedWord();
        checkWord(typedWord);
    }

    void showNextWord() {
        currentWordPosition++;
        hideCorrectTranslate();
        clearTypedWord();
        setColorToTypedText(R.color.clearBlack);
        typedWord = "";
        wordToTranslate.setText(wordsToLearn[currentWordPosition][RUS]);
    }

    void correct() {
        setColorToTypedText(R.color.correctGreen);
        numberOfCorrectAnswers++;
        progressBar.setCurrentColor(progressBar.GREEN);
        hideCheckButton();
    }

    void notCorrect() {
        setColorToTypedText(R.color.notCorrectRed);
        numberOfWrongAnswers++;
        progressBar.setCurrentColor(progressBar.RED);
    }

    private void hideCheckButton() {
        checkButton.setVisibility(View.INVISIBLE);
        startFateOutAnimation(checkButton);
        showGoNextButton();
    }

    private void showCheckButton() {
        hideGoNextButton();
        checkButton.setVisibility(View.VISIBLE);
    }

    private void hideGoNextButton(){
        goNextButton.setVisibility(View.INVISIBLE);
    }

    private void showGoNextButton(){
        goNextButton.setVisibility(View.VISIBLE);
        startFateInAnimation(goNextButton);
    }

    String getTypedWord() {
        String word;
        word = typedWord_EditText.getText().toString();
        word = word.toLowerCase();
        return word;
    }

    void checkWord(String userWord) {
        userWord = userWord.toLowerCase();
        userWord = userWord.replaceAll("\\s", "");
        String correctWord = wordsToLearn[currentWordPosition][ENG].toLowerCase();
        correctWord = correctWord.replaceAll("\\s", "");
        if (userWord.equals(correctWord)) {
            correct();
        } else {
            notCorrect();
        }
    }

    void setColorToTypedText(int colorId) {
        typedWord_EditText.setTextColor(getResources().getColor(colorId));
    }

    void showCorrectTranslate() {
        correctTranslate.setText(wordsToLearn[currentWordPosition][ENG]);
    }

    void hideCorrectTranslate() {
        correctTranslate.setText("");
    }

    boolean wordNotChecked() {
        return typedWord.equals("");
    }

    void clearTypedWord() {
        typedWord_EditText.setText("");
    }
}
