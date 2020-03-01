package com.leventenyiro.lightairlines.globalActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.adminActivity.AdminActivity;
import com.leventenyiro.lightairlines.segedOsztaly.Database;
import com.leventenyiro.lightairlines.segedOsztaly.Metodus;
import com.leventenyiro.lightairlines.segedOsztaly.PasswordUtils;
import com.leventenyiro.lightairlines.userActivity.InnerActivity;

public class PasswordUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnCancel, btnUpdate;
    private Database db;
    private EditText inputOldPassword, inputPassword, inputPasswordAgain;
    private ImageView btnBack;
    private int dp15, dp20;
    private Metodus m;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordupdate);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        init();
        inputOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputOldPassword.setBackground(getResources().getDrawable(R.drawable.input));
                inputOldPassword.setPaddingRelative(dp20, dp15, dp20, dp15);
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
                inputPassword.setBackground(getResources().getDrawable(R.drawable.input));
                inputPassword.setPaddingRelative(dp20, dp15, dp20, dp15);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        inputPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputPasswordAgain.setBackground(getResources().getDrawable(R.drawable.input));
                inputPasswordAgain.setPaddingRelative(dp20, dp15, dp20, dp15);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    private void init() {
        inputOldPassword = findViewById(R.id.inputOldPassword);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordAgain = findViewById(R.id.inputPasswordAgain);
        btnBack = findViewById(R.id.btnBack);
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);
        db = new Database(this);
        m = new Metodus(this);
        dp15 = m.dpToPx(15, getResources());
        dp20 = m.dpToPx(20, getResources());
        userId = getSharedPreferences("variables", Context.MODE_PRIVATE).getString("userId", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
            case R.id.btnCancel:
                onBackPressed();
                break;
            case R.id.btnUpdate:
                if (inputOldPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.noOldPassword), Toast.LENGTH_LONG).show();
                    inputClear();
                } else if (!m.jelszoEllenorzes(getSharedPreferences("variables", Context.MODE_PRIVATE).getString("userId", ""), inputOldPassword.getText().toString())) {
                    Toast.makeText(this, getString(R.string.wrongOldPassword), Toast.LENGTH_LONG).show();
                    inputClear();
                } else if (!m.jelszoErossegEllenorzes(inputPassword.getText().toString())) {
                    Toast.makeText(this, getString(R.string.weakPassword), Toast.LENGTH_LONG).show();
                    inputClear();
                } else if (inputPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.noPassword), Toast.LENGTH_LONG).show();
                    inputClear();
                } else if (inputPasswordAgain.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.noPasswordAgain), Toast.LENGTH_LONG).show();
                    inputClear();
                } else if (!inputPassword.getText().toString().equals(inputPasswordAgain.getText().toString())) {
                    Toast.makeText(this, getString(R.string.noPasswordPass), Toast.LENGTH_LONG).show();
                    inputClear();
                } else {
                    update();
                    onBackPressed();
                }
                break;
        }
    }

    public void inputClear() {
        inputOldPassword.setText("");
        inputPassword.setText("");
        inputPasswordAgain.setText("");
    }

    private void update() {
        String salt = PasswordUtils.getSalt(30);
        String titkositottPassword = PasswordUtils.generateSecurePassword(inputPassword.getText().toString(), salt);
        String password = titkositottPassword + ";" + salt;
        if (db.updatePassword(userId, password))
            Toast.makeText(this, getString(R.string.successPasswordUpdate), Toast.LENGTH_LONG);
        else
            Toast.makeText(this, getString(R.string.unsuccessPasswordUpdate), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (userId.equals("1")) {
            Intent intent = new Intent(PasswordUpdateActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            Intent intent = new Intent(PasswordUpdateActivity.this, InnerActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}
