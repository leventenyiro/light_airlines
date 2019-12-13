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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg3Activity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView btnBack, btnHome;
    private TextView btnLogin, textLeiras;
    private Button btnReg;
    private EditText inputPassword, inputPasswordAgain;
    private DatabaseUser db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg3);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();


        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                inputPassword.setBackground(getResources().getDrawable(R.drawable.input));
                inputPassword.setPaddingRelative(70, 40, 40, 40);
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
                inputPasswordAgain.setBackground(getResources().getDrawable(R.drawable.input));
                inputPasswordAgain.setPaddingRelative(70, 40, 40, 40);
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
        db = new DatabaseUser(this);
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
            case R.id.btnReg:
                ellenorzes();
                if (!jelszoEllenorzes(inputPassword.getText().toString()))
                {
                    Toast.makeText(this, "Gyenge jelszó!", Toast.LENGTH_SHORT).show();
                }
                else if (inputPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Nincs megadva jelszó!", Toast.LENGTH_SHORT).show();
                }
                else if (inputPasswordAgain.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Ismételd meg a jelszót!", Toast.LENGTH_SHORT).show();
                }
                else if (!inputPassword.getText().toString().equals(inputPasswordAgain.getText().toString()))
                {
                    Toast.makeText(this, "A két jelszó nem egyezik!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password", inputPassword.getText().toString());
                    editor.apply();

                    adatbazisInsert();

                    editor.clear();
                    editor.apply();

                    finishAffinity();
                }
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
        if (!inputPassword.getText().toString().isEmpty() && jelszoEllenorzes(inputPassword.getText().toString()))
        {
            inputPassword.setBackground(getResources().getDrawable(R.drawable.inputgreen));
            inputPassword.setPaddingRelative(70, 40, 40, 40);
        }
        if (!inputPasswordAgain.getText().toString().isEmpty() && inputPassword.getText().toString().equals(inputPasswordAgain.getText().toString()) && jelszoEllenorzes(inputPassword.getText().toString()))
        {
            inputPasswordAgain.setBackground(getResources().getDrawable(R.drawable.inputgreen));
            inputPasswordAgain.setPaddingRelative(70, 40, 40, 40);
        }
        if (inputPassword.getText().toString().isEmpty() || !jelszoEllenorzes(inputPassword.getText().toString()))
        {
            inputPassword.setBackground(getResources().getDrawable(R.drawable.inputred));
            inputPassword.setPaddingRelative(70, 40, 40, 40);
        }
        if (inputPasswordAgain.getText().toString().isEmpty() || !inputPassword.getText().toString().equals(inputPasswordAgain.getText().toString()))
        {
            inputPasswordAgain.setBackground(getResources().getDrawable(R.drawable.inputred));
            inputPasswordAgain.setPaddingRelative(70, 40, 40, 40);
        }
    }

    public void adatbazisInsert()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");
        String firstname = sharedPreferences.getString("firstname", "");
        String lastname = sharedPreferences.getString("lastname", "");
        String birthdate = sharedPreferences.getString("birthdate", "");

        String salt = PasswordUtils.getSalt(30);
        String titkositottPassword = PasswordUtils.generateSecurePassword(sharedPreferences.getString("password", ""), salt);
        String password = titkositottPassword + ";" + salt;

        Boolean eredmeny = db.insert(username, email, firstname, lastname, birthdate, password);
        if (eredmeny)
            Toast.makeText(this, "Sikeres regisztráció!", Toast.LENGTH_LONG);
        else
            Toast.makeText(this, "Szerverhiba! Sikertelen regisztráció!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finishAffinity() {
        SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(Reg3Activity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.finishAffinity();
    }
}
