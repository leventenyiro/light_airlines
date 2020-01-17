package com.leventenyiro.lightairlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leventenyiro.lightairlines.segedOsztalyok.Database;

import java.util.regex.Pattern;

public class Reg1Activity extends AppCompatActivity implements View.OnClickListener {
    private Button btnNext;
    private Database db;
    private EditText inputUsername, inputEmail;
    private ImageView btnBack, btnHome;
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
        db = new Database(this);
        s = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        se = s.edit();
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnBack:
            case R.id.btnHome:
            case R.id.btnLogin:
                onBackPressed();
                break;
            case R.id.btnNext:
                if (vanEUsername()) {
                    Toast.makeText(this, "A felhasználónév foglalt!", Toast.LENGTH_LONG).show();
                    inputSzin("usernameRed");
                }
                else if (!usernameEllenorzes(inputUsername.getText().toString())) {
                    Toast.makeText(this, "A felhasználónév túl rövid!", Toast.LENGTH_LONG).show();
                    inputSzin("usernameRed");
                }
                else if (inputUsername.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Nincs megadva felhasználónév!", Toast.LENGTH_LONG).show();
                    inputSzin("usernameRed");
                }
                else if (vanEEmail()) {
                    Toast.makeText(this, "Az e-mail cím foglalt!", Toast.LENGTH_LONG).show();
                    inputSzin("usernameGreen");
                    inputSzin("emailRed");
                }
                else if (inputEmail.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Nincs megadva e-mail cím!", Toast.LENGTH_LONG).show();
                    inputSzin("usernameGreen");
                    inputSzin("emailRed");
                }
                else if (!emailEllenorzes(inputEmail.getText().toString())) {
                    Toast.makeText(this, "Helytelen e-mail cím!", Toast.LENGTH_LONG).show();
                    inputSzin("usernameGreen");
                    inputSzin("emailRed");
                }
                else {
                    inputSzin("usernameGreen");
                    inputSzin("emailGreen");
                    se.putString("username", inputUsername.getText().toString());
                    se.putString("email", inputEmail.getText().toString());
                    se.apply();

                    intent = new Intent(Reg1Activity.this, Reg2Activity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    public void inputSzin(String mod) {
        switch (mod) {
            case "username":
                inputUsername.setBackground(getResources().getDrawable(R.drawable.input));
                inputUsername.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "usernameGreen":
                inputUsername.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputUsername.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "usernameRed":
                inputUsername.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputUsername.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "email":
                inputEmail.setBackground(getResources().getDrawable(R.drawable.input));
                inputEmail.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "emailGreen":
                inputEmail.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputEmail.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "emailRed":
                inputEmail.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputEmail.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
        }
    }

    public boolean usernameEllenorzes(String username) {
        return username.length() >= 5;
    }

    public boolean emailEllenorzes(String email) {
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailPattern);
        return pattern.matcher(email).matches();
    }

    public boolean vanEUsername() {
        Cursor eredmeny = db.selectUsername(inputUsername.getText().toString());
        return eredmeny.getCount() == 1;
    }

    public boolean vanEEmail() {
        Cursor eredmeny = db.selectEmail(inputEmail.getText().toString());
        return eredmeny.getCount() == 1;
    }

    public int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {
        se.clear().apply();
        Intent intent = new Intent(Reg1Activity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finishAffinity();
    }
}
