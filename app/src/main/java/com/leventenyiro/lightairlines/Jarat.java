package com.leventenyiro.lightairlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Jarat extends AppCompatActivity implements View.OnClickListener{

    private ImageView btnBack;
    private TextView textRovidites, textNev, textIdopont, textIdotartam, textHelyekSzama;
    private Button btnHelyFoglalas, btnMegse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jarat);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnBack: onBackPressed(); break;
            case R.id.btnMegse: onBackPressed(); break;
            case R.id.btnHelyFoglalas:
                // helyfoglalas activity...
        }
    }

    @Override
    public void onBackPressed() { finish(); }
}
