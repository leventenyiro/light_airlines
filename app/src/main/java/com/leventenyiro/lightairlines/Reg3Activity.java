package com.leventenyiro.lightairlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.leventenyiro.lightairlines.segedOsztalyok.PasswordUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg3Activity extends AppCompatActivity implements View.OnClickListener {
    private Button btnReg;
    private Database db;
    private EditText inputPassword, inputPasswordAgain;
    private ImageView btnBack, btnHome;
    private SharedPreferences s;
    private SharedPreferences.Editor se;
    private TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg3);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputPassword.setBackground(getResources().getDrawable(R.drawable.input));
                inputPassword.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputPasswordAgain.setBackground(getResources().getDrawable(R.drawable.input));
                inputPasswordAgain.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
            }
            @Override
            public void afterTextChanged(Editable s) { }
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
        s = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        se = s.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnHome:
            case R.id.btnLogin:
                finishAffinity();
                break;
            case R.id.btnReg:
                if (inputPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Nincs megadva jelszó!", Toast.LENGTH_SHORT).show();
                    inputClear();
                }
                else if (inputPasswordAgain.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Ismételd meg a jelszót!", Toast.LENGTH_SHORT).show();
                    inputClear();
                }
                else if (!inputPassword.getText().toString().equals(inputPasswordAgain.getText().toString())) {
                    Toast.makeText(this, "A két jelszó nem egyezik!", Toast.LENGTH_SHORT).show();
                    inputClear();
                }
                else if (!jelszoErossegEllenorzes(inputPassword.getText().toString())) {
                    Toast.makeText(this, "Gyenge jelszó!", Toast.LENGTH_SHORT).show();
                    inputClear();
                }
                else {
                    se.putString("password", inputPassword.getText().toString()).apply();
                    insertUser();
                    finishAffinity();
                }
                break;
        }
    }

    public boolean jelszoErossegEllenorzes(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
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

        Boolean eredmeny = db.insertUser(username, email, firstname, lastname, birthdate, password);
        if (eredmeny)
            Toast.makeText(this, "Sikeres regisztráció!", Toast.LENGTH_LONG);
        else
            Toast.makeText(this, "Szerverhiba! Sikertelen regisztráció!", Toast.LENGTH_SHORT).show();
    }

    public int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finishAffinity() {
        se.clear().apply();
        Intent intent = new Intent(Reg3Activity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.finishAffinity();
    }


}
