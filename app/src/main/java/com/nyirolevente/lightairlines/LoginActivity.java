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
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView btnBack, btnHome;
    private TextView btnReg;
    private Button btnLogin;
    private EditText inputUsernameEmail, inputPassword;
    private DatabaseUser db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        ellenorzes();

        inputUsernameEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ellenorzes();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ellenorzes();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    public void init()
    {
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnReg = findViewById(R.id.btnReg);
        btnLogin = findViewById(R.id.btnLogin);
        inputUsernameEmail = findViewById(R.id.inputUsernameEmail);
        inputPassword = findViewById(R.id.inputPassword);
        db = new DatabaseUser(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnHome:
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.btnReg:
                intent = new Intent(LoginActivity.this, Reg1Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.btnLogin:
                if (login() && passwordEllenorzes())
                {
                    intent = new Intent(LoginActivity.this, MainInnerActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (!login())
                {
                    Toast.makeText(LoginActivity.this, "Nincs ilyen felhasználó!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Helytelen jelszó!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void ellenorzes()
    {
        if (!inputUsernameEmail.getText().toString().isEmpty() && !inputPassword.getText().toString().isEmpty())
        {
            btnLogin.setEnabled(true);
            btnLogin.setBackground(getResources().getDrawable(R.drawable.button));
        }
        else
        {
            btnLogin.setEnabled(false);
            btnLogin.setBackground(getResources().getDrawable(R.drawable.buttondisabled));
        }
    }

    public boolean login()
    {
        Cursor eredmeny = db.selectLogin(inputUsernameEmail.getText().toString());
        StringBuffer stringBuffer = new StringBuffer();
        return eredmeny.getCount() == 1;
    }

    public boolean passwordEllenorzes()
    {
        Cursor eredmeny = db.selectPassword(inputUsernameEmail.getText().toString());
        StringBuffer stringBuffer = new StringBuffer();

        String password = null;
        String salt = null;

        if (eredmeny != null && eredmeny.getCount() > 0)
        {
            while (eredmeny.moveToNext())
            {
                String[] adatok = eredmeny.getString(0).split(";");
                password = adatok[0];
                salt = adatok[1];
            }
        }
        return PasswordUtils.verifyUserPassword(inputPassword.getText().toString(), password, salt);
    }

    @Override
    public void finish()
    {
        super.finish();
    }
}
