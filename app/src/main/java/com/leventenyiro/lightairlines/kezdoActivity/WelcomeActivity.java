package com.leventenyiro.lightairlines.kezdoActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.userActivity.InnerActivity;
import com.leventenyiro.lightairlines.segedOsztaly.Database;

public class WelcomeActivity extends AppCompatActivity {

    TextView textWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setStatusBarColor(getResources().getColor(R.color.welcomeGray));

        init();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
        ref.orderByKey().equalTo(getSharedPreferences("variables", Context.MODE_PRIVATE).getString("userId", ""))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            textWelcome.setText(getString(R.string.welcome) + " " + snapshot.child("firstname").getValue() + "!");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

        startAnimation();
        Thread welcomeThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(1000);  //Delay of 1 seconds = 1000 millis
                }
                catch (Exception e) { }
                finally {
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
    }

    private void startAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.text_appear);
        textWelcome.startAnimation(a);
    }
}
