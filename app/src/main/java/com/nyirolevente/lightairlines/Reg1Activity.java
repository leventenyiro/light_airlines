package com.nyirolevente.lightairlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reg1Activity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView btnBack;
    private ImageView btnHome;
    private TextView btnLogin;
    private Button btnNext;
    private EditText inputUsername, inputEmail;
    DatabaseUser db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg1);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        inputUsername.setText(sharedPreferences.getString("username", ""));
        inputEmail.setText(sharedPreferences.getString("email", ""));

        inputUsernameEllenorzes();
        inputEmailEllenorzes();

        inputUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                inputUsernameEllenorzes();
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
                inputEmailEllenorzes();
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
        db = new DatabaseUser(this);
    }

    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnHome:
                finish();
                break;
            case R.id.btnLogin:
                finish();
                break;
            case R.id.btnNext:
                ellenorzes();
                if (vanEUsername())
                {
                    Toast.makeText(this, "A felhasználónév foglalt!", Toast.LENGTH_LONG).show();
                }
                else if (inputUsername.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Nincs megadva felhasználónév!", Toast.LENGTH_LONG).show();
                }
                else if (vanEEmail())
                {
                    Toast.makeText(this, "Az e-mail cím foglalt!", Toast.LENGTH_LONG).show();
                }
                else if (inputEmail.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Nincs megadva e-mail cím!", Toast.LENGTH_LONG).show();
                }
                else if (!emailEllenorzes(inputEmail.getText().toString()))
                {
                    Toast.makeText(this, "Helytelen e-mail cím!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", inputUsername.getText().toString());
                    editor.putString("email", inputEmail.getText().toString());
                    editor.apply();

                    intent = new Intent(Reg1Activity.this, Reg2Activity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    public void inputUsernameEllenorzes()
    {
        if (!inputUsername.getText().toString().isEmpty())
        {
            inputUsername.setBackground(getResources().getDrawable(R.drawable.inputgreen));
            inputUsername.setPaddingRelative(70, 40, 40, 40);
        }
        else
        {
            inputUsername.setBackground(getResources().getDrawable(R.drawable.input));
            inputUsername.setPaddingRelative(70, 40, 40, 40);
        }
    }

    public void inputEmailEllenorzes()
    {

        if (!inputEmail.getText().toString().isEmpty() && emailEllenorzes(inputEmail.getText().toString()))
        {
            inputEmail.setBackground(getResources().getDrawable(R.drawable.inputgreen));
            inputEmail.setPaddingRelative(70, 40, 40, 40);
        }

        else
        {
            inputEmail.setBackground(getResources().getDrawable(R.drawable.input));
            inputEmail.setPaddingRelative(70, 40, 40, 40);
        }
    }

    public void ellenorzes()
    {
        if (vanEUsername() || inputUsername.getText().toString().isEmpty())
        {
            inputUsername.setBackground(getResources().getDrawable(R.drawable.inputred));
            inputUsername.setPaddingRelative(70, 40, 40, 40);
        }
        if (vanEEmail() || inputEmail.getText().toString().isEmpty() || !emailEllenorzes(inputEmail.getText().toString()))
        {
            inputEmail.setBackground(getResources().getDrawable(R.drawable.inputred));
            inputEmail.setPaddingRelative(70, 40, 40, 40);
        }
    }

    public boolean emailEllenorzes(String email)
    {
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public boolean vanEUsername()
    {
        Cursor eredmeny = db.selectUsername(inputUsername.getText().toString());
        StringBuffer stringBuffer = new StringBuffer();
        return eredmeny.getCount() == 1;
    }

    public boolean vanEEmail()
    {
        Cursor eredmeny = db.selectEmail(inputEmail.getText().toString());
        StringBuffer stringBuffer = new StringBuffer();
        return eredmeny.getCount() == 1;
    }

    @Override
    public void finish()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(Reg1Activity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.finishAffinity();
    }
}
