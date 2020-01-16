package com.leventenyiro.lightairlines;

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

import com.leventenyiro.lightairlines.segedOsztalyok.Database;

public class JegyActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDelete;
    private Database db;
    private ImageView btnBack;
    private TextView textRovidites, textNev, textIdopont, textIdotartam, textUles;
    private int brightness;

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

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        textRovidites = findViewById(R.id.textRovidites);
        textNev = findViewById(R.id.textNev);
        textIdopont = findViewById(R.id.textIdopont);
        textIdotartam = findViewById(R.id.textIdotartam);
        textUles = findViewById(R.id.textUles);
        db = new Database(this);

        brightness = Settings.System.getInt(getApplicationContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack: onBackPressed(); break;
            case R.id.btnDelete:
                // verify
                //getSharedPreferences("variables", Context.MODE_PRIVATE).edit().putString("muvelet", "jegytorles").apply();
                //intent
        }
    }

    private void select() {
        SharedPreferences sharedPreferences = getSharedPreferences("variables", Context.MODE_PRIVATE);
        Cursor e = db.selectJegy(sharedPreferences.getString("foglalasId", ""));
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

    public void delete() {
        
    }

    @Override
    public void onBackPressed() {
        setFenyesseg(brightness);
        getSharedPreferences("variables", Context.MODE_PRIVATE).edit().remove("foglalasId");
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
