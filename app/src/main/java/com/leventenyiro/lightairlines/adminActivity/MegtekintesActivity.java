package com.leventenyiro.lightairlines.adminActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.Database;
import com.leventenyiro.lightairlines.segedOsztaly.Metodus;

import java.util.ArrayList;
import java.util.List;

public class MegtekintesActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private Button btnBack;
    private Context mContext;
    private Database db;
    private int sorId, dp8, dp10, dp35, dp200;
    private List<Integer> helyLista;
    private Metodus m;
    private RelativeLayout mRelativeLayout;
    private SharedPreferences s;
    private String jaratId;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_megtekintes);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

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
                if (m.foglaltE(finalUlesId, jaratId)) {
                    tv.setBackground(getResources().getDrawable(R.drawable.seatred));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ulesInfo(m.ulesKodolas(finalUlesId));
                        }
                    });
                } else {
                    tv.setBackground(getResources().getDrawable(R.drawable.seatfree));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, getString(R.string.noReserved), Toast.LENGTH_SHORT).show();
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
                if (m.foglaltE(finalUlesId, jaratId)) {
                    tv.setBackground(getResources().getDrawable(R.drawable.seatred));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ulesInfo(m.ulesKodolas(finalUlesId));
                        }
                    });
                } else {
                    tv.setBackground(getResources().getDrawable(R.drawable.seatfree));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, getString(R.string.noReserved), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                ulesId++;
                ll.addView(tv);
            }
            mRelativeLayout.addView(ll);
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.edit().putString("fragment", "jaratok").apply();
                Intent intent = new Intent(MegtekintesActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private void init() {
        mContext = getApplicationContext();
        mRelativeLayout = findViewById(R.id.plane);
        helyLista = new ArrayList<>();
        btnBack = findViewById(R.id.btnBack);
        db = new Database(this);
        m = new Metodus(this);
        dp8 = m.dpToPx(8, getResources());
        dp10 = m.dpToPx(10, getResources());
        dp35 = m.dpToPx(35, getResources());
        dp200 = m.dpToPx(200, getResources());
        s = getSharedPreferences("variables", Context.MODE_PRIVATE);
        jaratId = s.getString("jaratId", "");
    }

    private void ulesInfo(String ules) {
        Cursor eredmeny = db.selectUlesInfo(jaratId, ules);
        if (eredmeny != null && eredmeny.getCount() > 0) {
            while (eredmeny.moveToNext()) {
                alertDialogBuilder = new AlertDialog.Builder(MegtekintesActivity.this);
                alertDialogBuilder.setTitle(getString(R.string.seatInfo) + " " + ules);
                alertDialogBuilder.setMessage("Név: " + eredmeny.getString(2) + " " + eredmeny.getString(3) +
                        "\n" + getString(R.string.username) + ": " + eredmeny.getString(0) +
                        "\nE-mail: " + eredmeny.getString(1));
                alertDialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        s.edit().remove("jaratId").apply();
        Intent intent = new Intent(MegtekintesActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
