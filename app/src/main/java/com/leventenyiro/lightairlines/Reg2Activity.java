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

import com.leventenyiro.lightairlines.segedOsztalyok.Metodus;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Reg2Activity extends AppCompatActivity implements View.OnClickListener {
    private Button btnNext;
    private DatePicker inputBirthdate;
    private EditText inputFirstname, inputLastname;
    private ImageView btnBack, btnHome;
    private int dp15, dp20;
    private Metodus m;
    private SharedPreferences s;
    private SharedPreferences.Editor se;
    private TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg2);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        inputFirstname.setText(s.getString("firstname", ""));
        inputLastname.setText(s.getString("lastname", ""));
        if (!s.getString("birthdate", "").isEmpty()) {
            String[] datum = s.getString("birthdate", "").split("-");
            inputBirthdate.updateDate(Integer.parseInt(datum[0]) + 0, Integer.parseInt(datum[1]) + 1, Integer.parseInt(datum[2]));
        }

        inputFirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputSzin("firstname");
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputLastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputSzin("lastname");
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
        inputFirstname = findViewById(R.id.inputFirstname);
        inputLastname = findViewById(R.id.inputLastname);
        inputBirthdate = findViewById(R.id.inputBirthdate);
        inputBirthdate.setMaxDate(System.currentTimeMillis());
        inputBirthdate.updateDate(2000,00,01);
        m = new Metodus(this);
        dp15 = m.dpToPx(15, getResources());
        dp20 = m.dpToPx(20, getResources());
        s = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        se = s.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnHome:
            case R.id.btnLogin:
                finishAffinity();
                break;
            case R.id.btnNext:
                if (inputFirstname.getText().toString().isEmpty()) {
                    inputSzin("firstnameRed");
                    Toast.makeText(this, "Nincs megadva keresztnév!", Toast.LENGTH_SHORT).show();
                }
                else if (inputLastname.getText().toString().isEmpty()) {
                    inputSzin("firstnameGreen");
                    inputSzin("lastnameRed");
                    Toast.makeText(this, "Nincs megadva vezetéknév!", Toast.LENGTH_SHORT).show();
                }
                else if (!korEllenorzes(inputBirthdate.getYear(), inputBirthdate.getMonth(), inputBirthdate.getDayOfMonth())) {
                    Toast.makeText(Reg2Activity.this, "13 éven aluliak nem regisztrálhatnak!", Toast.LENGTH_LONG).show();
                }
                else {
                    inputSzin("firstnameGreen");
                    inputSzin("lastnameGreen");
                    se.putString("firstname", m.elsoNagybetu(inputFirstname.getText().toString()).trim());
                    se.putString("lastname", m.elsoNagybetu(inputLastname.getText().toString()).trim());
                    se.putString("birthdate", birthdateToString(inputBirthdate.getYear(), inputBirthdate.getMonth(), inputBirthdate.getDayOfMonth()));
                    se.apply();
                    Intent intent = new Intent(Reg2Activity.this, Reg3Activity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    public void inputSzin(String mod) {
        switch (mod) {
            case "firstname":
                inputFirstname.setBackground(getResources().getDrawable(R.drawable.input));
                inputFirstname.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "firstnameGreen":
                inputFirstname.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputFirstname.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "firstnameRed":
                inputFirstname.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputFirstname.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "lastname":
                inputLastname.setBackground(getResources().getDrawable(R.drawable.input));
                inputLastname.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "lastnameGreen":
                inputLastname.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputLastname.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "lastnameRed":
                inputLastname.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputLastname.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
        }
    }

    public boolean korEllenorzes(int year, int month, int day) {
        Calendar today = GregorianCalendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        int age = todayYear - year;
        if (month > todayMonth || month == todayMonth && day > todayDay) {
            age--;
        }
        return age >= 13;
    }

    public String birthdateToString(int year, int month, int day) {
        return year + "-" + (month + 1) + "-" + day;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finishAffinity() {
        se.clear().apply();
        Intent intent = new Intent(Reg2Activity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.finishAffinity();
    }
}
