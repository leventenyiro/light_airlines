package com.leventenyiro.lightairlines;

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

import com.leventenyiro.lightairlines.segedOsztalyok.Database;

public class JaratActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnHelyFoglalas, btnMegse;
    private Database db;
    private ImageView btnBack;
    private TextView textRovidites, textNev, textIdopont, textIdotartam, textHelyekSzama;

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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack: onBackPressed(); break;
            case R.id.btnMegse: onBackPressed(); break;
            case R.id.btnHelyFoglalas:
                // helyfoglalas activity...
                Intent intent = new Intent(JaratActivity.this, FoglalasActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void select() {
        SharedPreferences sharedPreferences = getSharedPreferences("variables", Context.MODE_PRIVATE);
        Cursor e = db.selectJarat(sharedPreferences.getString("jaratId", ""));
        if (e != null && e.getCount() > 0) {
            while (e.moveToNext()) {
                textRovidites.setText(e.getString(3) + " - " + e.getString(5));
                textNev.setText(e.getString(2) + " - " + e.getString(4));
                textIdopont.setText(e.getString(1).substring(0, 16).replace('-', '.'));

                String[] idoresz = e.getString(6).split(":");
                if (Integer.parseInt(idoresz[0]) < 10) {
                    textIdotartam.setText(idoresz[0].substring(1, 2) + " óra " + idoresz[1] + " perc ");
                }
                else {
                    textIdotartam.setText(idoresz[0] + " óra " + idoresz[1] + " perc ");
                }
                textHelyekSzama.setText("Még " + e.getString(0) + " elérhető hely");
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
