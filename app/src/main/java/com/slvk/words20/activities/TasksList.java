package com.slvk.words20.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.slvk.words20.R;
import com.slvk.words20.activities.tasks.Blitz;
import com.slvk.words20.activities.tasks.BuildTheWord;
import com.slvk.words20.activities.tasks.TypeTheWord;
import com.slvk.words20.activities.tasks.ChooseAnswer;

public class TasksList extends AppCompatActivity {
    String tb;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);
        tb = (String)getIntent().getSerializableExtra("THEME");
    }

    public void onClick_EngToRus(View view) {
        intent = new Intent(this, ChooseAnswer.class);
        intent.putExtra("THEME", tb);
        intent.putExtra("TYPE", ChooseAnswer.Type.ENG_RUS);
        startActivity(intent);
    }

    public void onClick_RusToEng(View view){
        intent = new Intent(this, ChooseAnswer.class);
        intent.putExtra("THEME", tb);
        intent.putExtra("TYPE", ChooseAnswer.Type.RUS_ENG);
        startActivity(intent);
    }

    public void onClick_BuildTheWord(View view){
        intent = new Intent(this, BuildTheWord.class);
        intent.putExtra("THEME", tb);
        startActivity(intent);
    }

    public void onClick_TypeTheWord(View view){
        intent = new Intent(this, TypeTheWord.class);
        intent.putExtra("THEME", tb);
        startActivity(intent);
    }

    public void onClick_Blitz(View view){
        intent = new Intent(this, Blitz.class);
        intent.putExtra("THEME", tb);
        startActivity(intent);
    }


    public void onClick_Close_cross(View view) {
        finish();
    }

}
