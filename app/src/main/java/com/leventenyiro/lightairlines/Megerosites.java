package com.leventenyiro.lightairlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.leventenyiro.lightairlines.segedOsztalyok.Database;
import com.leventenyiro.lightairlines.segedOsztalyok.Metodus;

public class Megerosites extends AppCompatActivity implements View.OnClickListener {

    private Button btnCancel, btnVerify;
    private Database db;
    private EditText inputPassword;
    private ImageView btnBack;
    private Metodus m;
    private SharedPreferences s;

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
        m = new Metodus(this);
        s = getSharedPreferences("variables", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
            case R.id.btnCancel: onBackPressed(); break;
            case R.id.btnVerify:
                if (inputPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Nincs megadva jelszó!", Toast.LENGTH_SHORT).show();
                }
                else if (!m.jelszoEllenorzes(s.getString("userId", ""), inputPassword.getText().toString())) {
                    Toast.makeText(this, "Sikertelen jóváhagyás!", Toast.LENGTH_SHORT).show();
                    inputPassword.setText("");
                }
                else {
                    deleteJegy();
                    s.edit().remove("foglalasId").apply();
                    Intent intent = new Intent(Megerosites.this, InnerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finishAffinity();
                }
                break;
        }
    }

    public void deleteJegy() {
        if (db.deleteJegy(s.getString("foglalasId", "")))
            Toast.makeText(this, "Sikeres jegytörlés!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Szerverhiba! Sikertelen jegytörlés!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
