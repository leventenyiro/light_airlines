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

public class Reg1Activity extends AppCompatActivity
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

        final SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        inputUsername.setText(sharedPreferences.getString("username", ""));
        inputEmail.setText(sharedPreferences.getString("email", ""));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(Reg1Activity.this, LoginActivity.class);
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
                Intent intent = new Intent(Reg1Activity.this, MainActivity.class);
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
                Intent intent = new Intent(Reg1Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (inputUsername.getText().toString().isEmpty() || inputEmail.getText().toString().isEmpty())
                {
                    Toast.makeText(Reg1Activity.this, "Hiba! Valami nincs kitöltve!", Toast.LENGTH_LONG).show();
                }
                else if (!emailEllenorzes(inputEmail.getText().toString()))
                {
                    Toast.makeText(Reg1Activity.this, "Helytelen E-mail cím!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", inputUsername.getText().toString());
                    editor.putString("email", inputEmail.getText().toString());
                    editor.apply();

                    Intent intent = new Intent(Reg1Activity.this, Reg2Activity.class);
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
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
    }

    public boolean emailEllenorzes(String email)
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
    }
}
