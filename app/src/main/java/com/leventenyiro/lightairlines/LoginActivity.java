package com.leventenyiro.lightairlines;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leventenyiro.lightairlines.segedOsztalyok.Database;
import com.leventenyiro.lightairlines.segedOsztalyok.PasswordUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private Button btnLogin;
    private Database db;
    private EditText inputUsernameEmail, inputPassword;
    private TextView btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        inputUsernameEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputUsernameEmail.setBackground(getResources().getDrawable(R.drawable.inputlogin));
                inputUsernameEmail.setPaddingRelative(70, 40, 40, 40);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputPassword.setBackground(getResources().getDrawable(R.drawable.inputlogin));
                inputPassword.setPaddingRelative(70, 40, 40, 40);
            }
            @Override
            public void afterTextChanged(Editable s) { }
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

        alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setTitle("Kilépés");
        alertDialogBuilder.setMessage("Biztos elhagyod az alkalmazást?");
        alertDialogBuilder.setPositiveButton("Nem", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setNegativeButton("Igen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
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
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                break;
            case R.id.btnLogin:
                ellenorzes();
                if (inputUsernameEmail.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Nincs megadva a felhasználónév vagy e-mail!", Toast.LENGTH_SHORT).show();
                    inputPassword.setText("");
                }
                else if (inputPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Nincs megadva a jelszó!", Toast.LENGTH_SHORT).show();
                }
                else if (!login()) {
                    Toast.makeText(LoginActivity.this, "Helytelen bejelentkezési adatok!", Toast.LENGTH_SHORT).show();
                    inputPassword.setText("");
                }
                else {
                    intent = new Intent(LoginActivity.this, InnerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finishAffinity();
                }
                break;
        }
    }

    public void ellenorzes() {
        if (inputUsernameEmail.getText().toString().isEmpty()) {
            inputUsernameEmail.setBackground(getResources().getDrawable(R.drawable.inputloginred));
            inputUsernameEmail.setPaddingRelative(70, 40, 40, 40);
        }
        if (inputPassword.getText().toString().isEmpty()) {
            inputPassword.setBackground(getResources().getDrawable(R.drawable.inputloginred));
            inputPassword.setPaddingRelative(70, 40, 40, 40);
        }
        else if (!login()) {
            inputUsernameEmail.setBackground(getResources().getDrawable(R.drawable.inputloginred));
            inputUsernameEmail.setPaddingRelative(70, 40, 40, 40);
            inputPassword.setBackground(getResources().getDrawable(R.drawable.inputloginred));
            inputPassword.setPaddingRelative(70, 40, 40, 40);
        }
        else if (login()) {
            inputUsernameEmail.setBackground(getResources().getDrawable(R.drawable.inputlogin));
            inputUsernameEmail.setPaddingRelative(70, 40, 40, 40);
            inputPassword.setBackground(getResources().getDrawable(R.drawable.inputlogin));
            inputPassword.setPaddingRelative(70, 40, 40, 40);
        }
    }

    public boolean login() {
        Cursor eredmeny = db.selectLogin(inputUsernameEmail.getText().toString());
        if (eredmeny.getCount() == 1) {
            if (jelszoEllenorzes()) {
                SharedPreferences sharedPreferences = getSharedPreferences("variables", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                while (eredmeny.moveToNext()) {
                    editor.putString("userId", eredmeny.getString(0));
                }
                editor.apply();
                return true;
            }
        }
        return false;
    }

    public boolean jelszoEllenorzes() {
        Cursor eredmeny = db.selectPasswordByUsernameEmail(inputUsernameEmail.getText().toString());

        String password = null;
        String salt = null;

        if (eredmeny != null && eredmeny.getCount() > 0) {
            while (eredmeny.moveToNext()) {
                String[] adatok = eredmeny.getString(0).split(";");
                password = adatok[0];
                salt = adatok[1];
            }
        }
        return PasswordUtils.verifyUserPassword(inputPassword.getText().toString(), password, salt);
    }

    @Override
    public void onBackPressed() {
        alertDialog.show();
    }
}
