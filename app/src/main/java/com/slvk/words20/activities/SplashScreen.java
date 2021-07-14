package com.slvk.words20.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.slvk.words20.App;
import com.slvk.words20.R;
import com.slvk.words20.activities.MainActivity;
import com.slvk.words20.activities.TasksList;
import com.slvk.words20.database.helpers.FirebaseHelper;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //this.deleteDatabase("words.db");
       // App.getInstance().getRoomDatabase().getDescriptionDao().deleteTable();
        //getBaseContext().openOrCreateDatabase("words.db", MODE_PRIVATE, null);

        FirebaseHelper fbHelper = new FirebaseHelper(App.getSQLiteDatabase(), this);
        fbHelper.setThemesListener();
        Handler hd = new Handler();
        Intent intent = new Intent(this, MainActivity.class);
        Runnable rn = new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        };

        hd.postDelayed(rn, 3000);
    }
}
