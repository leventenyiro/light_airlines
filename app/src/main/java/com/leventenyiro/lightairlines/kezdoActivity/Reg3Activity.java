package com.leventenyiro.lightairlines.kezdoActivity;

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

import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.Database;
import com.leventenyiro.lightairlines.segedOsztaly.Metodus;
import com.leventenyiro.lightairlines.segedOsztaly.PasswordUtils;

public class Reg3Activity extends AppCompatActivity implements View.OnClickListener {
    private Button btnReg;
    private Database db;
    private EditText inputPassword, inputPasswordAgain;
    private ImageView btnBack, btnHome;
    private int dp15, dp20;
    private Metodus m;
    private SharedPreferences s;
    private TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg3);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputPassword.setBackground(getResources().getDrawable(R.drawable.input));
                inputPassword.setPaddingRelative(dp20, dp15, dp20, dp15);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputPasswordAgain.setBackground(getResources().getDrawable(R.drawable.input));
                inputPasswordAgain.setPaddingRelative(dp20, dp15, dp20, dp15);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);
    }

    public void init() {
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnLogin = findViewById(R.id.btnLogin);
        btnReg = findViewById(R.id.btnReg);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordAgain = findViewById(R.id.inputPasswordAgain);
        db = new Database(this);
        m = new Metodus(this);
        dp15 = m.dpToPx(15, getResources());
        dp20 = m.dpToPx(20, getResources());
        s = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnHome:
            case R.id.btnLogin:
                backToLogin();
                break;
            case R.id.btnReg:
                if (inputPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.noPassword), Toast.LENGTH_LONG).show();
                    inputClear();
                } else if (inputPasswordAgain.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.noPasswordAgain), Toast.LENGTH_LONG).show();
                    inputClear();
                } else if (!inputPassword.getText().toString().equals(inputPasswordAgain.getText().toString())) {
                    Toast.makeText(this, getString(R.string.noPasswordPass), Toast.LENGTH_LONG).show();
                    inputClear();
                } else if (!m.jelszoErossegEllenorzes(inputPassword.getText().toString())) {
                    Toast.makeText(this, getString(R.string.weakPassword), Toast.LENGTH_LONG).show();
                    inputClear();
                } else {
                    s.edit().putString("password", inputPassword.getText().toString()).apply();
                    insertUser();
                    backToLogin();
                }
                break;
        }
    }

    public void inputClear() {
        inputPassword.setText("");
        inputPasswordAgain.setText("");
    }

    public void insertUser() {
        String username = s.getString("username", "");
        String email = s.getString("email", "");
        String firstname = s.getString("firstname", "");
        String lastname = s.getString("lastname", "");
        String birthdate = s.getString("birthdate", "");

        String salt = PasswordUtils.getSalt(30);
        String titkositottPassword = PasswordUtils.generateSecurePassword(s.getString("password", ""), salt);
        String password = titkositottPassword + ";" + salt;

        boolean eredmeny = db.insertUser(username, email, firstname, lastname, birthdate, password);
        if (eredmeny)
            Toast.makeText(this, getString(R.string.successReg), Toast.LENGTH_LONG);
        else
            Toast.makeText(this, getString(R.string.unsuccessReg), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Reg3Activity.this, Reg2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void backToLogin() {
        s.edit().clear().apply();
        Intent intent = new Intent(Reg3Activity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
