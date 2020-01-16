package com.leventenyiro.lightairlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.leventenyiro.lightairlines.segedOsztalyok.Database;
import com.leventenyiro.lightairlines.segedOsztalyok.PasswordUtils;

public class Megerosites extends AppCompatActivity implements View.OnClickListener {

    private Button btnBack, btnCancel, btnVerify;
    private Database db;
    private EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_megerosites);

        init();

        btnBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        btnCancel = findViewById(R.id.btnCancel);
        btnVerify = findViewById(R.id.btnVerify);
        inputPassword = findViewById(R.id.inputPassword);
        db = new Database(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack: onBackPressed(); break;
            case R.id.btnCancel: onBackPressed(); break;
            case btnVerify:
                if (jelszoEllenorzes()) {

                }
        }
    }

    public boolean jelszoEllenorzes() {
        Cursor eredmeny = db.selectPasswordById(getSharedPreferences("variables", Context.MODE_PRIVATE).getString("userId", ""));

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
        finish();
    }
}
