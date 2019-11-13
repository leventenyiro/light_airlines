package com.nyirolevente.lightairlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Reg2Activity extends AppCompatActivity {

    private ImageView btnBack;
    private ImageView btnHome;
    private TextView btnLogin;
    private Button btnNext;
    private EditText inputFirstname, inputLastname, inputBirthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg2);

        init();

        final SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        inputFirstname.setText(sharedPreferences.getString("firstname", ""));
        inputLastname.setText(sharedPreferences.getString("lastname", ""));
        inputBirthdate.setText(sharedPreferences.getString("birthdate", ""));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reg2Activity.this, Reg1Activity.class);
                startActivity(intent);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(Reg2Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(Reg2Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (inputFirstname.getText().toString().isEmpty() || inputLastname.getText().toString().isEmpty() || inputBirthdate.getText().toString().isEmpty())
                {
                    Toast.makeText(Reg2Activity.this, "Hiba! Valami nincs kitöltve!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("firstname", elsoNagybetu(inputFirstname.getText().toString()));
                    editor.putString("lastname", elsoNagybetu(inputLastname.getText().toString()));

                    editor.putString("birthdate", inputBirthdate.getText().toString());
                    editor.apply();

                    Intent intent = new Intent(Reg2Activity.this, Reg3Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
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
}
