package com.example.szakdolgozat;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg3Activity extends AppCompatActivity {

    private ImageView btnBack;
    private ImageView btnHome;
    private TextView btnLogin;
    private Button btnReg;
    private EditText inputPassword, inputPasswordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg3);

        init();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reg3Activity.this, Reg2Activity.class);
                startActivity(intent);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reg3Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reg3Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (inputPassword.getText().toString().isEmpty() || inputPasswordAgain.getText().toString().isEmpty())
                {
                    Toast.makeText(Reg3Activity.this, "Hiba! Valami nincs kitöltve!", Toast.LENGTH_LONG).show();
                }
                else if (!inputPassword.getText().toString().equals(inputPasswordAgain.getText().toString()))
                {
                    Toast.makeText(Reg3Activity.this, "Hiba! A két jelszó nem egyezik egymással!", Toast.LENGTH_LONG).show();
                }
                else if (!isValidPassword(inputPassword.getText().toString().trim()))
                {
                    Toast.makeText(Reg3Activity.this, "A jelszónak 8 karakterből kell állnia, tartalmaznia kell egy számot egy nagybetűt!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password", inputPassword.getText().toString());
                    editor.apply();

                    // ADATBÁZIS HELYE - regisztrált adatok rögzítése

                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(Reg3Activity.this, LoginActivity.class);
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
        btnReg = findViewById(R.id.btnReg);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordAgain = findViewById(R.id.inputPasswordAgain);
    }

    public boolean isValidPassword(final String password)
    {
        Pattern pattern;
        Matcher matcher;

        final String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(passwordPattern);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
}
