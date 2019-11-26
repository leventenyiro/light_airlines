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

public class Reg1Activity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView btnBack;
    private ImageView btnHome;
    private TextView btnLogin;
    private Button btnNext;
    private EditText inputUsername, inputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg1);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        inputUsername.setText(sharedPreferences.getString("username", ""));
        inputEmail.setText(sharedPreferences.getString("email", ""));

        ellenorzes();

        inputUsername.addTextChangedListener(new TextWatcher() {
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
        inputEmail.addTextChangedListener(new TextWatcher() {
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
        btnNext.setOnClickListener(this);
    }

    public void init()
    {
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnLogin = findViewById(R.id.btnLogin);
        btnNext = findViewById(R.id.btnNext);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnBack:
                SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(Reg1Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnHome:
                sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                intent = new Intent(Reg1Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnLogin:
                sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                intent = new Intent(Reg1Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnNext:
                sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("username", inputUsername.getText().toString());
                editor.putString("email", inputEmail.getText().toString());
                editor.apply();

                intent = new Intent(Reg1Activity.this, Reg2Activity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

   /* public boolean emailEllenorzes(String email)
    {
        int dbBetu = 0;
        int dbPont = 0;
        String[] betuk = email.split("");
        for (int i = 0; i < betuk.length; i++)
        {
            if (betuk[i].equals("@"))
            {
                dbBetu++;
            }
            if (betuk[i].equals("."))
            {
                dbPont++;
            }
        }
        if (dbBetu == 1 && dbPont >= 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }*/
    public boolean emailEllenorzes(String email)
    {
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public void ellenorzes()
    {
        if (!inputUsername.getText().toString().isEmpty() && !inputEmail.getText().toString().isEmpty() && emailEllenorzes(inputEmail.getText().toString()) == true)
        {
            btnNext.setEnabled(true);
            btnNext.setBackground(getResources().getDrawable(R.drawable.button));
        }
        else
        {
            btnNext.setEnabled(false);
            btnNext.setBackground(getResources().getDrawable(R.drawable.buttondisabled));
        }
    }
}
