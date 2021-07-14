package com.slvk.words20.activities.tasks;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.slvk.words20.DisplayHelper;
import com.slvk.words20.R;
import com.slvk.words20.activities.Result;

import java.util.Random;

public class BuildTheWord extends BaseTaskActivity {
    int ROWS = 4;
    int COLS = 5;
    int currentLetterPosition = 0;
    char[] word_ch;
    char[] word_ch_shook;
    boolean wordBuiltCorrectly = true;
    Button goNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_the_word);
        goNextButton = (Button) findViewById(R.id.next_button);

        setToolBar();
        setProgressBar();

        showWordToTranslate();
        showLetters();

        hideGoNextButton();
    }


    private void showLetters() {
        word_ch = GetNextWord();
        word_ch_shook = ShakeWord(word_ch);
        showLetters(word_ch_shook);
    }

    private char[] GetCharArray(String word_s) {
        word_s = word_s.toLowerCase();
        int numberOfSpaces = 0;

        //Считаем количество пробелов в фразе
        for (int i = 0; i < word_s.length(); i++) {
            if (word_s.charAt(i) == ' ') {
                numberOfSpaces++;
            }
        }

        //Создаем массив для символов без пробелов
        char[] word_c = new char[word_s.length() - numberOfSpaces];

        //Копируем в массив все символы кроме пробелов
        int j = 0;
        for (int i = 0; i < word_s.length(); i++) {
            if (word_s.charAt(i) != ' ') {
                word_c[j] = word_s.charAt(i);
                j++;
            }
        }

        return word_c;
    }

    private boolean isCorrect(char ch) {
        if (word_ch[currentLetterPosition] == ch) {
            currentLetterPosition++;
            return true;
        } else {
            return false;
        }
    }


    //Перемешивает буквы в массиве
    private char[] ShakeWord(char[] not_shaken) {
        int length = not_shaken.length;
        char[] shaken = new char[length];

        System.arraycopy(not_shaken, 0, shaken, 0, length);

        Random rnd = new Random();
        int randomPosition;
        for (int i = 0; i < length; i++) {
            randomPosition = rnd.nextInt(length);
            Swap(shaken, i, randomPosition);
        }

        return shaken;
    }

    //Меняет две буквы местами
    private void Swap(char[] ch, int elem1, int elem2) {
        char tmp = ch[elem1];
        ch[elem1] = ch[elem2];
        ch[elem2] = tmp;
    }

    //Возвращает следующее слово
    private char[] GetNextWord() {
        String word = wordsToLearn[currentWordPosition][ENG];
        return GetCharArray(word);
    }

    //Добавляет букву в построенное слово
    int posInString = 0;

    void addLetter(char letter) {
        TextView builtWord = (TextView) findViewById(R.id.built_word);
        char ch = wordsToLearn[currentWordPosition][ENG].charAt(posInString);
        if (ch == ' ') {
            posInString++;
            builtWord.append(" ");
        }
        builtWord.append(Character.toString(letter));
    }

    void showGoNextButton() {
        goNextButton.setEnabled(true);
        goNextButton.setVisibility(View.VISIBLE);
        startFateInAnimation(goNextButton);
    }

    void hideGoNextButton() {
        goNextButton.setEnabled(false);
        goNextButton.setVisibility(View.INVISIBLE);
        //startFateOutAnimation(goNextButton);
    }

    void showWordToTranslate() {
        TextView wordToTranslate = (TextView) findViewById(R.id.word_to_build);
        wordToTranslate.setText(wordsToLearn[currentWordPosition][RUS]);
    }

    void clearBuiltWord() {
        final TextView wordToBuilt = (TextView) findViewById(R.id.built_word);
        wordToBuilt.setText("");
    }

    int letterToShowPosition;

    void showLetters(char[] word) {
        letterToShowPosition = 0;
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_lt);
        tableLayout.removeAllViews();
        for (int i = 0; i < ROWS; i++) {
            TableRow tableRow = getNewTableRow();
            addAndFillColumns(tableRow, word);
            tableLayout.addView(tableRow, i);
        }
    }

    TableRow getNewTableRow() {
        TableRow tableRow = new TableRow(this);
        android.widget.TableRow.LayoutParams p = new android.widget.TableRow.LayoutParams();

        p.rightMargin = DisplayHelper.dpToPixel(10, this); // right-margin = 10dp
        p.topMargin = DisplayHelper.dpToPixel(10, this);

        tableRow.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return tableRow;
    }

    void addAndFillColumns(TableRow tableRow, char[] arr) {
        for (int j = 0; j < COLS; j++) {
            if (letterToShowPosition == word_ch.length) {
                break;
            }
            Button button = createNewLetterButton(arr[letterToShowPosition]);
            letterToShowPosition++;

            tableRow.addView(button, j);
            setParamsToLetterButton(button);
            setOnClickListenerToLetterButton(button);
        }
    }

    public void onClick_GoNext(View view) {
        currentWordPosition++;
        wordBuiltCorrectly = true;

        if (currentWordPosition == wordsToLearn.length) {
            showResult(Result.TypesOftest.BUILD_THE_WORD);
        } else {
            hideGoNextButton();
            posInString = 0;
            showWordToTranslate();
            clearBuiltWord();

            showLetters();

            progressBar.moveNext();
            currentLetterPosition = 0;
        }
    }

    Button createNewLetterButton(char buttonText) {
        Button button = new Button(getApplicationContext());
        button.setText(Character.toString(buttonText));
        //button.setBackground(getResources().getDrawable(R.drawable.rnd_rect_blue));
        return button;
    }

    void setParamsToLetterButton(Button button) {
        android.widget.TableRow.LayoutParams p = new android.widget.TableRow.LayoutParams();
        button.setBackground(getResources().getDrawable(R.drawable.rnd_rect_blue));
        button.setTextSize(DisplayHelper.dpToPixel(10, this));

        //ViewGroup.LayoutParams params = button.getLayoutParams();

        p.height = DisplayHelper.dpToPixel(55, this);
        p.width = DisplayHelper.dpToPixel(55, this);
        int margin = DisplayHelper.dpToPixel(2, this);
        p.setMargins(margin, margin, margin, margin);
        button.setGravity(Gravity.CENTER);
        button.setLayoutParams(p);
        button.requestLayout();
    }

    void setOnClickListenerToLetterButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_letter(v);
            }
        });
    }

    void onClick_letter(View v) {
        makeLetterButtonNormal(previousNotCorrectButton);
        Button btn = (Button) v;
        checkAnswer(btn);
    }

    void hideLetterButton(Button button) {
        button.setVisibility(View.INVISIBLE);
    }

    void checkAnswer(Button btn) {
        char ch = btn.getText().charAt(0);
        if (isCorrect(ch)) {
            correct(ch, btn);
        } else {
            notCorrect(btn);
        }
    }

    void correct(char ch, Button btn) {
        addLetter(ch);
        posInString++;
        hideLetterButton(btn);

        if (letterIsLast()) {
            showGoNextButton();
            addPoints();
        }
    }

    Button previousNotCorrectButton;

    void notCorrect(Button btn) {
        makeLetterButtonRed(btn);
        previousNotCorrectButton = btn;
        wordBuiltCorrectly = false;
    }

    void addPoints() {
        if (wordBuiltCorrectly) {
            numberOfCorrectAnswers++;
            progressBar.setCurrentColor(progressBar.GREEN);
        } else {
            numberOfWrongAnswers++;
            progressBar.setCurrentColor(progressBar.RED);
        }
    }

    void makeLetterButtonRed(Button btn) {
        if (btn != null) {
            btn.setBackground(getResources().getDrawable(R.drawable.rnd_rect_red));
        }
    }

    void makeLetterButtonNormal(Button btn) {
        if (btn != null) {
            btn.setBackground(getResources().getDrawable(R.drawable.rnd_rect_blue));
        }
    }

    boolean letterIsLast() {
        return posInString == wordsToLearn[currentWordPosition][ENG].length();
    }

}
