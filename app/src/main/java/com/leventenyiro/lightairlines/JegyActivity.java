package com.leventenyiro.lightairlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leventenyiro.lightairlines.segedOsztalyok.Database;

public class JegyActivity extends AppCompatActivity implements View.OnClickListener {

    private Database db;
    private ImageView btnBack;
    private TextView textRovidites, textNev, textIdopont, textIdotartam, textUles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jegy);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        select();

        btnBack.setOnClickListener(this);
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        textRovidites = findViewById(R.id.textRovidites);
        textNev = findViewById(R.id.textNev);
        textIdopont = findViewById(R.id.textIdopont);
        textIdotartam = findViewById(R.id.textIdotartam);
        textUles = findViewById(R.id.textUles);
        db = new Database(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack: onBackPressed(); break;
        }
    }

    private void select() {
        SharedPreferences sharedPreferences = getSharedPreferences("variables", Context.MODE_PRIVATE);
        Cursor e = db.selectJarat(sharedPreferences.getString("foglalasId", ""));
        if (e != null && e.getCount() > 0) {
            while (e.moveToNext()) {
                textRovidites.setText(e.getString(3) + " - " + e.getString(5));
                textNev.setText(e.getString(2) + " - " + e.getString(4));
                textIdopont.setText(e.getString(1).substring(0, 16).replace('-', '.'));
                textIdotartam.setText(idotartamAtalakitas(e.getString(6)));
                textUles.setText("Ülőhely: " + e.getString(0));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public String idotartamAtalakitas(String idotartam) {
        String[] idoresz = idotartam.split(":");
        if (Integer.parseInt(idoresz[0]) < 10) {
            return idoresz[0].substring(1, 2) + " óra " + idoresz[1] + " perc";
        }
        else {
            return idoresz[0] + " óra " + idoresz[1] + " perc";
        }
    }
}
