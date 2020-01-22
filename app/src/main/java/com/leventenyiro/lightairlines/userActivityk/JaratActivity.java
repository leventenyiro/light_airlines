package com.leventenyiro.lightairlines.userActivityk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztalyok.Database;
import com.leventenyiro.lightairlines.segedOsztalyok.Metodus;

public class JaratActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnHelyFoglalas, btnMegse;
    private Database db;
    private ImageView btnBack;
    private Metodus m;
    private TextView textRovidites, textNev, textIdopont, textIdotartam, textHelyekSzama;
    private SharedPreferences s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jarat);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        init();
        select();
        btnBack.setOnClickListener(this);
        btnHelyFoglalas.setOnClickListener(this);
        btnMegse.setOnClickListener(this);
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        textRovidites = findViewById(R.id.textRovidites);
        textNev = findViewById(R.id.textNev);
        textIdopont = findViewById(R.id.textIdopont);
        textIdotartam = findViewById(R.id.textIdotartam);
        textHelyekSzama = findViewById(R.id.textHelyekSzama);
        btnHelyFoglalas = findViewById(R.id.btnHelyFoglalas);
        btnMegse = findViewById(R.id.btnMegse);
        db = new Database(this);
        m = new Metodus(this);
        s = getSharedPreferences("variables", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
            case R.id.btnMegse: onBackPressed(); break;
            case R.id.btnHelyFoglalas:
                Intent intent = new Intent(JaratActivity.this, FoglalasActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void select() {
        Cursor e = db.selectJarat(s.getString("jaratId", ""));
        if (e != null && e.getCount() > 0) {
            while (e.moveToNext()) {
                String roviditesInfo = e.getString(3) + " - " + e.getString(5);
                textRovidites.setText(roviditesInfo);
                String nevInfo = e.getString(2) + " - " + e.getString(4);
                textNev.setText(nevInfo);
                textIdopont.setText(e.getString(1).substring(0, 16).replace('-', '.'));
                textIdotartam.setText(m.idotartamAtalakitas(e.getString(6)));
                String helyInfo = getString(R.string.seatInfo1) + " " + e.getString(0) + " " + getString(R.string.seatInfo2);
                textHelyekSzama.setText(helyInfo);
            }
        }
    }

    @Override
    public void onBackPressed() {
        s.edit().remove("jaratId").apply();
        Intent intent = new Intent(JaratActivity.this, InnerActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
