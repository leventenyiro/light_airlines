package com.leventenyiro.lightairlines.userActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.Database;
import com.leventenyiro.lightairlines.segedOsztaly.Metodus;

import java.util.ArrayList;
import java.util.List;

public class FoglalasActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private Button btnFoglalas, btnBack;
    private Context mContext;
    private Database db;
    private int sorId, dp8, dp10, dp35, dp200;
    private List<Integer> helyLista;
    private Metodus m;
    private RelativeLayout mRelativeLayout;
    private SharedPreferences s;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foglalas);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();
        m.selectFoglaltHelyek(s.getString("jaratId", ""));

        int ulesId = 0;
        for (int i = 1; i < 21; i++) {
            LinearLayout ll = new LinearLayout(mContext);
            RelativeLayout.LayoutParams paramsSor = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, dp35);
            if (i == 1) {
                paramsSor.setMargins(0, dp10, 0, dp10);
                paramsSor.addRule(RelativeLayout.BELOW, R.id.betuk);
            } else {
                if (i == 20)
                    paramsSor.setMargins(0, dp10, 0, dp200);
                else
                    paramsSor.setMargins(0, dp10, 0, dp10);
                paramsSor.addRule(RelativeLayout.BELOW, sorId);
            }
            ll.setLayoutParams(paramsSor);
            ll.setGravity(Gravity.CENTER);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setId(ll.generateViewId());
            sorId = ll.getId();
            for (int j = 0; j < 3; j++) {
                tv = new TextView(mContext);
                LinearLayout.LayoutParams paramsUles = new LinearLayout.LayoutParams(dp35, LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsUles.setMargins(dp8, 0, dp8, 0);
                tv.setLayoutParams(paramsUles);
                tv.setId(tv.generateViewId());
                helyLista.add(tv.getId());
                final int finalUlesId = ulesId;
                if (m.foglaltE(finalUlesId, s.getString("jaratId", ""))) {
                    tv.setBackground(getResources().getDrawable(R.drawable.seatred));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, getString(R.string.seatReserved), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    tv.setBackground(getResources().getDrawable(R.drawable.seatfree));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int id : helyLista) {
                                if (!m.foglaltE(helyLista.indexOf(id), s.getString("jaratId", ""))) {
                                    findViewById(id).setBackground(getResources().getDrawable(R.drawable.seatfree));
                                }
                            }
                            findViewById(helyLista.get(finalUlesId)).setBackground(getResources().getDrawable(R.drawable.seatgreen));
                            s.edit().putString("ules", m.ulesKodolas(finalUlesId)).apply();
                        }
                    });
                }
                ulesId++;
                ll.addView(tv);
            }
            tv = new TextView(mContext);
            LinearLayout.LayoutParams paramsSorszam = new LinearLayout.LayoutParams(dp35, LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsSorszam.setMargins(20, 0, 20, 0);
            paramsSorszam.gravity = Gravity.CENTER;
            tv.setLayoutParams(paramsSorszam);
            tv.setText(String.valueOf(i));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ll.addView(tv);
            for (int j = 0; j < 3; j++) {
                tv = new TextView(mContext);
                LinearLayout.LayoutParams paramsUles = new LinearLayout.LayoutParams(dp35, LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsUles.setMargins(dp8, 0, dp8, 0);
                tv.setLayoutParams(paramsUles);
                tv.setId(tv.generateViewId());
                helyLista.add(tv.getId());
                final int finalUlesId = ulesId;
                if (m.foglaltE(finalUlesId, s.getString("jaratId", ""))) {
                    tv.setBackground(getResources().getDrawable(R.drawable.seatred));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, getString(R.string.seatReserved), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    tv.setBackground(getResources().getDrawable(R.drawable.seatfree));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int id : helyLista) {
                                if (!m.foglaltE(helyLista.indexOf(id), s.getString("jaratId", ""))) {
                                    findViewById(id).setBackground(getResources().getDrawable(R.drawable.seatfree));
                                }
                            }
                            findViewById(helyLista.get(finalUlesId)).setBackground(getResources().getDrawable(R.drawable.seatgreen));
                            s.edit().putString("ules", m.ulesKodolas(finalUlesId)).apply();
                        }
                    });
                }
                ulesId++;
                ll.addView(tv);
            }
            mRelativeLayout.addView(ll);
        }
        btnFoglalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!s.getString("ules", "").isEmpty()) {
                    alertDialog.show();
                } else {
                    Toast.makeText(mContext, getString(R.string.noSeatReserved), Toast.LENGTH_LONG).show();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        mContext = getApplicationContext();
        mRelativeLayout = findViewById(R.id.plane);
        helyLista = new ArrayList<>();
        btnFoglalas = findViewById(R.id.btnFoglalas);
        btnBack = findViewById(R.id.btnBack);
        db = new Database(this);
        m = new Metodus(this);
        dp8 = m.dpToPx(8, getResources());
        dp10 = m.dpToPx(10, getResources());
        dp35 = m.dpToPx(35, getResources());
        dp200 = m.dpToPx(200, getResources());
        s = getSharedPreferences("variables", Context.MODE_PRIVATE);

        alertDialogBuilder = new AlertDialog.Builder(FoglalasActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.finalize));
        alertDialogBuilder.setMessage(getString(R.string.finalizeMessage));
        alertDialogBuilder.setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                insertFoglalas();
                s.edit().putString("fragment", "jegyek").apply();
                Intent intent = new Intent(FoglalasActivity.this, InnerActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        alertDialog = alertDialogBuilder.create();
    }

    public void insertFoglalas() {
        String jaratId = s.getString("jaratId", "");
        String userId = s.getString("userId", "");
        String ules = s.getString("ules", "");
        boolean eredmeny = db.insertFoglalas(jaratId, userId, ules);
        if (eredmeny)
            Toast.makeText(this, getString(R.string.successReserve), Toast.LENGTH_LONG);
        else
            Toast.makeText(this, getString(R.string.unsuccessReserve), Toast.LENGTH_LONG).show();
        s.edit().remove("jaratId").apply();
        s.edit().remove("ules").apply();
    }


    @Override
    public void onBackPressed() {
        s.edit().remove("ules").apply();
        Intent intent = new Intent(FoglalasActivity.this, JaratActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}