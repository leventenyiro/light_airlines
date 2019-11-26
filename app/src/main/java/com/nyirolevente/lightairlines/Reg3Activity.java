package com.nyirolevente.lightairlines;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg3Activity extends AppCompatActivity implements View.OnClickListener
{

    private ImageView btnBack;
    private ImageView btnHome;
    private TextView btnLogin, textLeiras;
    private Button btnReg;
    private EditText inputPassword, inputPasswordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg3);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        ellenorzes();

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                ellenorzes();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                ellenorzes();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);
    }

    public void init()
    {
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnLogin = findViewById(R.id.btnLogin);
        btnReg = findViewById(R.id.btnReg);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordAgain = findViewById(R.id.inputPasswordAgain);
        textLeiras = findViewById(R.id.textLeiras);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnBack:
                SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.apply();
                Intent intent = new Intent(Reg3Activity.this, Reg2Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnHome:
                sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                intent = new Intent(Reg3Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnLogin:
                sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                intent = new Intent(Reg3Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnReg:
                sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("password", inputPassword.getText().toString());
                editor.apply();

                // ADATBÁZIS HELYE - regisztrált adatok rögzítése

                editor.clear();
                editor.apply();

                intent = new Intent(Reg3Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public boolean jelszoEllenorzes(String password)
    {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public void ellenorzes()
    {
        if (!inputPassword.getText().toString().isEmpty() && inputPassword.getText().toString().equals(inputPasswordAgain.getText().toString()) && jelszoEllenorzes(inputPassword.getText().toString()))
        {
            btnReg.setEnabled(true);
            btnReg.setBackground(getResources().getDrawable(R.drawable.button));
            textLeiras.setVisibility(View.INVISIBLE);
        }
        else
        {
            btnReg.setEnabled(false);
            btnReg.setBackground(getResources().getDrawable(R.drawable.buttondisabled));
            textLeiras.setVisibility(View.VISIBLE);
        }
    }
}
