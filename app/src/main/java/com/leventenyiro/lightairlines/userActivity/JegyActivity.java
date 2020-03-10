package com.leventenyiro.lightairlines.userActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
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

public class JegyActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDelete;
    private DatabaseReference db;
    private ImageView btnBack;
    private int brightness;
    private Metodus m;
    private SharedPreferences s;
    private TextView textRovidites, textNev, textIdopont, textIdotartam, textUles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jegy);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        init();
        select();
        setFenyesseg(255);
        btnBack.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        textRovidites = findViewById(R.id.textRovidites);
        textNev = findViewById(R.id.textNev);
        textIdopont = findViewById(R.id.textIdopont);
        textIdotartam = findViewById(R.id.textIdotartam);
        textUles = findViewById(R.id.textUles);
        db = FirebaseDatabase.getInstance().getReference();
        m = new Metodus(this);
        s = getSharedPreferences("variables", Context.MODE_PRIVATE);
        brightness = Settings.System.getInt(getApplicationContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack: onBackPressed(); break;
            case R.id.btnDelete:
                setFenyesseg(brightness);
                Intent intent = new Intent(JegyActivity.this, MegerositesActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void setFenyesseg(int brightness) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getApplicationContext())) {
                Settings.System.putInt(getApplicationContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
            }
            else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    private void select() {
        db.child("foglalas").orderByKey().equalTo(s.getString("foglalasId", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String ules = String.valueOf(snapshot.child("ules").getValue());
                    db.child("jarat").orderByKey().equalTo(String.valueOf(snapshot.child("jarat_id").getValue())).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                textRovidites.setText(snapshot.child("indulas_rovidites").getValue() + " - " + snapshot.child("celallomas_rovidites").getValue());
                                textNev.setText(snapshot.child("indulas_nev").getValue() + " - " + snapshot.child("celallomas_nev").getValue());
                                textIdopont.setText(String.valueOf(snapshot.child("idopont").getValue()).substring(0, 16).replace('-', '.'));
                                textIdotartam.setText(m.idotartamAtalakitas(String.valueOf(snapshot.child("idotartam").getValue())));
                                textUles.setText(getString(R.string.seatInfo) + " " + ules);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public void onBackPressed() {
        setFenyesseg(brightness);
        s.edit().remove("foglalasId").apply();
        Intent intent = new Intent(JegyActivity.this, InnerActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
