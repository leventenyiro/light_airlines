package com.leventenyiro.lightairlines.kezdoActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.*;
import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.*;

public class Reg1Activity extends AppCompatActivity implements View.OnClickListener {

    private Button btnNext;
    private EditText inputUsername, inputEmail;
    private ImageView btnBack, btnHome;
    private int dp15, dp20;
    private Metodus m;
    private SharedPreferences s;
    private SharedPreferences.Editor se;
    private TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg1);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        inputUsername.setText(s.getString("username", ""));
        inputEmail.setText(s.getString("email", ""));

        inputUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputSzin("username");
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputSzin("email");
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    public void init() {
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnLogin = findViewById(R.id.btnLogin);
        btnNext = findViewById(R.id.btnNext);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        m = new Metodus(this);
        dp15 = m.dpToPx(15, getResources());
        dp20 = m.dpToPx(20, getResources());
        s = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        se = s.edit();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
            case R.id.btnHome:
            case R.id.btnLogin: onBackPressed(); break;
            case R.id.btnNext:
                if (!m.usernameWhiteSpaceEllenorzes(inputUsername.getText().toString())) {
                    Toast.makeText(this, getString(R.string.usernameWhiteSpace), Toast.LENGTH_LONG).show();
                    inputSzin("usernameRed");
                }
                else if (!m.usernameHosszEllenorzes(inputUsername.getText().toString())) {
                    Toast.makeText(this, getString(R.string.username5char), Toast.LENGTH_LONG).show();
                    inputSzin("usernameRed");
                }
                else if (inputUsername.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.noUsername), Toast.LENGTH_LONG).show();
                    inputSzin("usernameRed");
                }
                else if (!m.emailEllenorzes(inputEmail.getText().toString())) {
                    Toast.makeText(this, getString(R.string.wrongEmail), Toast.LENGTH_LONG).show();
                    inputSzin("usernameGreen");
                    inputSzin("emailRed");
                }
                else if (inputEmail.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.noEmail), Toast.LENGTH_LONG).show();
                    inputSzin("usernameGreen");
                    inputSzin("emailRed");
                }
                else {
                    m.loading(findViewById(R.id.loading));
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
                    ref.orderByChild("username").equalTo(inputUsername.getText().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() == 1) {
                                        Toast.makeText(Reg1Activity.this, getString(R.string.usernameExists), Toast.LENGTH_LONG).show();
                                        inputSzin("usernameRed");
                                        m.loading(findViewById(R.id.loading));
                                    } else {
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
                                        ref.orderByChild("email").equalTo(inputEmail.getText().toString())
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.getChildrenCount() == 1) {
                                                            Toast.makeText(Reg1Activity.this, getString(R.string.emailExists), Toast.LENGTH_LONG).show();
                                                            inputSzin("usernameGreen");
                                                            inputSzin("emailRed");
                                                            m.loading(findViewById(R.id.loading));
                                                        } else {
                                                            inputSzin("usernameGreen");
                                                            inputSzin("emailGreen");
                                                            se.putString("username", inputUsername.getText().toString());
                                                            se.putString("email", inputEmail.getText().toString());
                                                            se.apply();
                                                            Intent intent = new Intent(Reg1Activity.this, Reg2Activity.class);
                                                            startActivity(intent);
                                                            finish();
                                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                            m.loading(findViewById(R.id.loading));

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    }
                                                });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                }
                break;
        }
    }

    public void inputSzin(String mod) {
        switch (mod) {
            case "username":
                inputUsername.setBackground(getResources().getDrawable(R.drawable.input));
                inputUsername.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "usernameGreen":
                inputUsername.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputUsername.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "usernameRed":
                inputUsername.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputUsername.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "email":
                inputEmail.setBackground(getResources().getDrawable(R.drawable.input));
                inputEmail.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "emailGreen":
                inputEmail.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputEmail.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "emailRed":
                inputEmail.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputEmail.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        se.clear().apply();
        Intent intent = new Intent(Reg1Activity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
