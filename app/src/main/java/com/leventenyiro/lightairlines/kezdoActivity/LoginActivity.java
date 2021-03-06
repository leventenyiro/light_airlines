package com.leventenyiro.lightairlines.kezdoActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.adminActivity.AdminActivity;
import com.leventenyiro.lightairlines.segedOsztaly.Database;
import com.leventenyiro.lightairlines.segedOsztaly.Metodus;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private Button btnLogin;
    private Database db;
    private EditText inputUsernameEmail, inputPassword;
    private int dp15, dp20;
    private Metodus m;
    private TextView btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        inputUsernameEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputSzin("usernameEmail");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputSzin("password");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnReg.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    public void init() {
        btnReg = findViewById(R.id.btnReg);
        btnLogin = findViewById(R.id.btnLogin);
        inputUsernameEmail = findViewById(R.id.inputUsernameEmail);
        inputPassword = findViewById(R.id.inputPassword);
        db = new Database(this);
        m = new Metodus(this);
        dp15 = m.dpToPx(15, getResources());
        dp20 = m.dpToPx(20, getResources());

        alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.exit));
        alertDialogBuilder.setMessage(getString(R.string.exitMessage));
        alertDialogBuilder.setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                System.exit(0);
            }
        });
        alertDialog = alertDialogBuilder.create();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnReg:
                intent = new Intent(LoginActivity.this, Reg1Activity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.btnLogin:
                if (inputUsernameEmail.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.noUsernameEmail), Toast.LENGTH_LONG).show();
                    inputPassword.setText("");
                    inputSzin("usernameEmailRed");
                } else if (inputPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.noPassword), Toast.LENGTH_LONG).show();
                    inputSzin("passwordRed");
                } else if (login() == 0) {
                    Toast.makeText(LoginActivity.this, getString(R.string.wrongLoginData), Toast.LENGTH_LONG).show();
                    inputPassword.setText("");
                    inputSzin("usernameEmailRed");
                    inputSzin("passwordRed");
                } else {
                    if (login() == 1) {
                        intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    } else {
                        intent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
                break;
        }
    }

    public void inputSzin(String mod) {
        switch (mod) {
            case "usernameEmail":
                inputUsernameEmail.setBackground(getResources().getDrawable(R.drawable.inputlogin));
                inputUsernameEmail.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "usernameEmailRed":
                inputUsernameEmail.setBackground(getResources().getDrawable(R.drawable.inputloginred));
                inputUsernameEmail.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "password":
                inputPassword.setBackground(getResources().getDrawable(R.drawable.inputlogin));
                inputPassword.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "passwordRed":
                inputPassword.setBackground(getResources().getDrawable(R.drawable.inputloginred));
                inputPassword.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
        }
    }

    public int login() {
        Cursor eredmeny = db.selectLogin(inputUsernameEmail.getText().toString().trim());
        if (eredmeny.getCount() == 1) {
            while (eredmeny.moveToNext()) {
                String userId = eredmeny.getString(0);
                if (m.jelszoEllenorzes(userId, inputPassword.getText().toString()) && userId.equals("1")) {
                    getSharedPreferences("variables", Context.MODE_PRIVATE).edit().putString("userId", "1").apply();
                    return 2;
                } else if (m.jelszoEllenorzes(userId, inputPassword.getText().toString())) {
                    getSharedPreferences("variables", Context.MODE_PRIVATE).edit().putString("userId", eredmeny.getString(0)).apply();
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        alertDialog.show();
    }
}
