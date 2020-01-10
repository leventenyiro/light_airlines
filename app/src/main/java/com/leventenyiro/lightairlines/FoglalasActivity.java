package com.leventenyiro.lightairlines;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leventenyiro.lightairlines.fragments.JegyekFragment;
import com.leventenyiro.lightairlines.segedOsztalyok.Database;

import java.util.ArrayList;
import java.util.List;

public class FoglalasActivity extends AppCompatActivity {

    private Context mContext;
    private RelativeLayout mRelativeLayout;
    private List<Integer> helyLista;
    private int sorId;
    private Button btnFoglalas, btnBack;
    private TextView tv;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foglalas);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();
        foglaltHelyek();

        int ulesId = 0;
        for (int i = 1; i < 21; i++) {
            LinearLayout ll = new LinearLayout(mContext);
            RelativeLayout.LayoutParams paramsSor = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, dpToPx(35));
            if (i == 1) {
                paramsSor.setMargins(0, dpToPx(10), 0, dpToPx(10));
                paramsSor.addRule(RelativeLayout.BELOW, R.id.betuk);
            }
            else {
                if (i == 20)
                    paramsSor.setMargins(0, dpToPx(10), 0, dpToPx(200));
                else
                    paramsSor.setMargins(0, dpToPx(10), 0, dpToPx(10));
                paramsSor.addRule(RelativeLayout.BELOW, sorId);
            }
            ll.setLayoutParams(paramsSor);
            ll.setGravity(Gravity.CENTER);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setId(ll.generateViewId());
            sorId = ll.getId();
            for (int j = 0; j < 3; j++) {
                tv = new TextView(mContext);
                LinearLayout.LayoutParams paramsUles = new LinearLayout.LayoutParams(dpToPx(35), LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsUles.setMargins(dpToPx(8), 0, dpToPx(8), 0);
                tv.setLayoutParams(paramsUles);
                tv.setId(tv.generateViewId());
                helyLista.add(tv.getId());
                final int finalUlesId = ulesId;
                if (foglaltE(finalUlesId)) {
                    tv.setBackground(getResources().getDrawable(R.drawable.ic_seatred));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "Ez a hely már foglalt!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    tv.setBackground(getResources().getDrawable(R.drawable.ic_seatfree));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int id : helyLista) {
                                if (!foglaltE(helyLista.indexOf(id))) {
                                    findViewById(id).setBackground(getResources().getDrawable(R.drawable.ic_seatfree));
                                }
                            }
                            findViewById(helyLista.get(finalUlesId)).setBackground(getResources().getDrawable(R.drawable.ic_seatgreen));
                            SharedPreferences sharedPreferences = getSharedPreferences("variables", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("ules", ulesKodolas(finalUlesId));
                            editor.apply();
                        }
                    });
                }
                ulesId++;
                ll.addView(tv);
            }
            tv = new TextView(mContext);
            LinearLayout.LayoutParams paramsSorszam = new LinearLayout.LayoutParams(dpToPx(35), LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsSorszam.setMargins(20, 0, 20, 0);
            paramsSorszam.gravity = Gravity.CENTER;
            tv.setLayoutParams(paramsSorszam);
            tv.setText(String.valueOf(i));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ll.addView(tv);
            for (int j = 0; j < 3; j++) {
                tv = new TextView(mContext);
                LinearLayout.LayoutParams paramsUles = new LinearLayout.LayoutParams(dpToPx(35), LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsUles.setMargins(dpToPx(8), 0, dpToPx(8), 0);
                tv.setLayoutParams(paramsUles);
                tv.setId(tv.generateViewId());
                helyLista.add(tv.getId());
                final int finalUlesId = ulesId;
                if (foglaltE(finalUlesId)) {
                    tv.setBackground(getResources().getDrawable(R.drawable.ic_seatred));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "Ez a hely már foglalt!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    tv.setBackground(getResources().getDrawable(R.drawable.ic_seatfree));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int id : helyLista) {
                                if (!foglaltE(helyLista.indexOf(id))) {
                                    findViewById(id).setBackground(getResources().getDrawable(R.drawable.ic_seatfree));
                                }
                            }
                            findViewById(helyLista.get(finalUlesId)).setBackground(getResources().getDrawable(R.drawable.ic_seatgreen));
                            SharedPreferences sharedPreferences = getSharedPreferences("variables", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("ules", ulesKodolas(finalUlesId));
                            editor.apply();
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
                if (!getSharedPreferences("variables", Context.MODE_PRIVATE).getString("ules", "").isEmpty()) {
                    alertDialog.show();
                }
                else {
                    Toast.makeText(mContext, "Nincs kiválasztott hely!", Toast.LENGTH_SHORT).show();
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

        alertDialogBuilder = new AlertDialog.Builder(FoglalasActivity.this);
        alertDialogBuilder.setTitle("Véglegesítés");
        alertDialogBuilder.setMessage("Ezzel a lépéssel véglegesíted a helyfoglalást.");
        alertDialogBuilder.setPositiveButton("Nem", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setNegativeButton("Igen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                insertFoglalas();
                Intent intent = new Intent(FoglalasActivity.this, InnerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finishAffinity();
            }
        });
        alertDialog = alertDialogBuilder.create();
    }

    @Override
    public void onBackPressed() {
        getSharedPreferences("variables", Context.MODE_PRIVATE).edit().remove("ules");
        finish();
    }

    public int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }

    public String ulesKodolas(int szam) {
        String ules = "";
        ules += szam / 6 + 1;
        switch (szam % 6) {
            case 0: ules += "A"; break;
            case 1: ules += "B"; break;
            case 2: ules += "C"; break;
            case 3: ules += "D"; break;
            case 4: ules += "E"; break;
            case 5: ules += "F"; break;
        }
        return ules;
    }

    public int ulesDekodolas(String ules) {
        int szam;
        if (ules.length() == 2) {
            szam = (Integer.parseInt(ules.substring(0,1)) - 1) * 6;
            switch (ules.substring(1,2)) {
                case "A": szam += 0; break;
                case "B": szam += 1; break;
                case "C": szam += 2; break;
                case "D": szam += 3; break;
                case "E": szam += 4; break;
                case "F": szam += 5; break;
            }
        }
        else {
            szam = (Integer.parseInt(ules.substring(0, 2)) - 1) * 6;
            switch (ules.substring(2, 3)) {
                case "A": szam += 0; break;
                case "B": szam += 1; break;
                case "C": szam += 2; break;
                case "D": szam += 3; break;
                case "E": szam += 4; break;
                case "F": szam += 5; break;
            }
        }
        return szam;
    }

    public boolean foglaltE(int id) {
        for (String hely : selectFoglaltHelyek()) {
            if (id == ulesDekodolas(hely)) {
                return true;
            }
        }
        return false;
    }

    public List<String> selectFoglaltHelyek() {
        List<String> foglaltHelyek = new ArrayList<>();
        Cursor eredmeny = db.selectUlesek(getSharedPreferences("variables", Context.MODE_PRIVATE).getString("jaratId", ""));
        if (eredmeny != null && eredmeny.getCount() > 0) {
            while (eredmeny.moveToNext()) {
                foglaltHelyek.add(eredmeny.getString(0));
            }
        }
        return foglaltHelyek;
    }

    public void insertFoglalas() {
        SharedPreferences sharedPreferences = getSharedPreferences("variables", Context.MODE_PRIVATE);
        String jaratId = sharedPreferences.getString("jaratId", "");
        String userId = sharedPreferences.getString("userId", "");
        String ules = sharedPreferences.getString("ules", "");
        Boolean eredmeny = db.insertFoglalas(jaratId, userId, ules);
        if (eredmeny)
            Toast.makeText(this, "Sikeres foglalás!", Toast.LENGTH_LONG);
        else
            Toast.makeText(this, "Szerverhiba! Sikertelen foglalás!", Toast.LENGTH_SHORT).show();
        sharedPreferences.edit().remove("jaratId").apply();
        sharedPreferences.edit().remove("ules").apply();
    }
}