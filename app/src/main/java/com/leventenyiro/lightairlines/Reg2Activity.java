package com.leventenyiro.lightairlines;

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
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Reg2Activity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView btnBack;
    private ImageView btnHome;
    private TextView btnLogin;
    private Button btnNext;
    private EditText inputFirstname, inputLastname;
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
        if (!sharedPreferences.getString("birthdate", "").isEmpty())
        {
            String[] datum = sharedPreferences.getString("birthdate", "").split("-");
            inputBirthdate.updateDate(Integer.parseInt(datum[0]) + 0, Integer.parseInt(datum[1]) + 1, Integer.parseInt(datum[2]));
        }

        ellenorzes();

        inputFirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                inputFirstname.setBackground(getResources().getDrawable(R.drawable.input));
                inputFirstname.setPaddingRelative(70, 40, 40, 40);
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
                inputLastname.setBackground(getResources().getDrawable(R.drawable.input));
                inputLastname.setPaddingRelative(70, 40, 40, 40);
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
        inputBirthdate.updateDate(2000,00,01);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnHome:
                finishAffinity();
                break;
            case R.id.btnLogin:
                finishAffinity();
                break;
            case R.id.btnNext:
                ellenorzes();
                if (!korEllenorzes(inputBirthdate.getYear(), inputBirthdate.getMonth(), inputBirthdate.getDayOfMonth()))
                {
                    Toast.makeText(Reg2Activity.this, "13 éven aluliak nem regisztrálhatnak!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("firstname", elsoNagybetu(inputFirstname.getText().toString()));
                    editor.putString("lastname", elsoNagybetu(inputLastname.getText().toString()));
                    editor.putString("birthdate", birthdateToString(inputBirthdate.getYear(), inputBirthdate.getMonth(), inputBirthdate.getDayOfMonth()));
                    editor.apply();

                    Intent intent = new Intent(Reg2Activity.this, Reg3Activity.class);
                    startActivity(intent);
                }
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


    public boolean korEllenorzes(int year, int month, int day)
    {
        Calendar today = GregorianCalendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        int age = todayYear - year;
        if (month > todayMonth || month == todayMonth && day > todayDay)
        {
            age--;
        }
        return age >= 13;
    }

    public String birthdateToString(int year, int month, int day)
    {
        return year + "-" + (month + 1) + "-" + day;
    }

    public void ellenorzes()
    {
        if (!inputFirstname.getText().toString().isEmpty())
        {
            inputFirstname.setBackground(getResources().getDrawable(R.drawable.inputgreen));
            inputFirstname.setPaddingRelative(70, 40, 40, 40);
        }
        if (!inputLastname.getText().toString().isEmpty())
        {
            inputLastname.setBackground(getResources().getDrawable(R.drawable.inputgreen));
            inputLastname.setPaddingRelative(70, 40, 40, 40);
        }
    }

    @Override
    public void onBackPressed() {
        super.finish();
    }

    @Override
    public void finishAffinity() {
        SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(Reg2Activity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.finishAffinity();
    }
}
