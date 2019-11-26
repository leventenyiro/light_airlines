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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg2Activity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView btnBack;
    private ImageView btnHome;
    private TextView btnLogin;
    private Button btnNext;
    private EditText inputFirstname, inputLastname; /*Birthdatehez kéne egy maszk */
    private DatePicker inputBirthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg2);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        inputFirstname.setText(sharedPreferences.getString("firstname", ""));
        inputLastname.setText(sharedPreferences.getString("lastname", ""));
        //inputBirthdate.setText(sharedPreferences.getString("birthdate", ""));

        ellenorzes();

        inputFirstname.addTextChangedListener(new TextWatcher() {
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
        inputLastname.addTextChangedListener(new TextWatcher() {
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
        inputFirstname = findViewById(R.id.inputFirstname);
        inputLastname = findViewById(R.id.inputLastname);
        inputBirthdate = findViewById(R.id.inputBirthdate);
        inputBirthdate.setMaxDate(System.currentTimeMillis());
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
                Intent intent = new Intent(Reg2Activity.this, Reg1Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnHome:
                sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                intent = new Intent(Reg2Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnLogin:
                sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                intent = new Intent(Reg2Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnNext:
                sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("firstname", elsoNagybetu(inputFirstname.getText().toString()));
                editor.putString("lastname", elsoNagybetu(inputLastname.getText().toString()));
                //editor.putString("birthdate", inputBirthdate.getText().toString());
                editor.apply();

                intent = new Intent(Reg2Activity.this, Reg3Activity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public String elsoNagybetu(String nev)
    {
        String[] nevek = nev.split(" ");
        String ujNev = "";
        for (String s : nevek) {
            ujNev += s.toUpperCase().charAt(0) + s.toLowerCase().substring(1, s.length()) + " ";
        }
        return ujNev;
    }

    public boolean datumEllenorzes(DatePicker datePicker)
    {
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();

        Date date = new Date(year, month, day);
        return true;
    }

    public void ellenorzes()
    {
        if (!inputFirstname.getText().toString().isEmpty() && !inputLastname.getText().toString().isEmpty()/* && !inputBirthdate.getText().toString().isEmpty() && datumEllenorzes(inputBirthdate.getText().toString())*/)
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
