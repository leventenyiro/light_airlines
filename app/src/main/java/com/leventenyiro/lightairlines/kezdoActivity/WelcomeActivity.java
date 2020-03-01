package com.leventenyiro.lightairlines.kezdoActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.Database;
import com.leventenyiro.lightairlines.userActivity.InnerActivity;

public class WelcomeActivity extends AppCompatActivity {

    Database db;
    TextView textWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();
        textWelcome.setText(getString(R.string.welcome) + " " + getFirstname() + "!");
        startAnimation();
        Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(1000);  //Delay of 1 seconds = 1000 millis
                } catch (Exception e) {
                } finally {
                    Intent intent = new Intent(WelcomeActivity.this, InnerActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        };
        welcomeThread.start();
    }

    public void init() {
        textWelcome = findViewById(R.id.textWelcome);
        db = new Database(this);
    }

    private void startAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.text_appear);
        textWelcome.startAnimation(a);
    }

    public String getFirstname() {
        Cursor e = db.selectUser(getSharedPreferences("variables", Context.MODE_PRIVATE).getString("userId", ""));
        if (e != null && e.getCount() > 0) {
            while (e.moveToNext()) {
                return e.getString(2);
            }
        }
        return "";
    }
}
