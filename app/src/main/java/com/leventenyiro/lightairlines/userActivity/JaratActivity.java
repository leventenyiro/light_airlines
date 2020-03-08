package com.leventenyiro.lightairlines.userActivity;

import androidx.annotation.NonNull;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.Database;
import com.leventenyiro.lightairlines.segedOsztaly.Metodus;

public class JaratActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnHelyFoglalas, btnMegse;
    private DatabaseReference db;
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
        db = FirebaseDatabase.getInstance().getReference();
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
        db.child("jarat").orderByKey().equalTo(s.getString("jaratId", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    textRovidites.setText(snapshot.child("indulas_rovidites").getValue() + " - " + snapshot.child("celallomas_rovidites").getValue());
                    textNev.setText(snapshot.child("indulas_nev").getValue() + " - " + snapshot.child("celallomas_nev").getValue());
                    textIdopont.setText(String.valueOf(snapshot.child("idopont").getValue()).substring(0, 16).replace('-', '.'));
                    textIdotartam.setText(m.idotartamAtalakitas(String.valueOf(snapshot.child("idotartam").getValue())));
                    final int helyekszama = Integer.parseInt(String.valueOf(snapshot.child("helyek_szama").getValue()));
                    db.child("foglalas").orderByKey().equalTo(s.getString("jaratid", "")).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            textHelyekSzama.setText(getString(R.string.seatInfo1) + " " + (helyekszama - dataSnapshot.getChildrenCount()) + " " + getString(R.string.seatInfo2));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
