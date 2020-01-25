package com.leventenyiro.lightairlines.adminActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.Database;
import com.leventenyiro.lightairlines.segedOsztaly.Metodus;

import java.util.Calendar;

public class JaratInsertActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnInsert;
    private Database db;
    private DatePicker inputDate;
    private ImageView btnBack;
    private Metodus m;
    private SharedPreferences s;
    private TextView textRovidites, textNev;
    private TimePicker inputTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jarat_insert);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        init();
        selectUtvonal();
        btnBack.setOnClickListener(this);
        btnInsert.setOnClickListener(this);
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        btnInsert = findViewById(R.id.btnInsert);
        inputDate = findViewById(R.id.inputDate);
        inputDate.setMaxDate(System.currentTimeMillis() + 31556952000L);
        Calendar c = Calendar.getInstance();
        inputDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        inputDate.setMinDate(System.currentTimeMillis() + 604800000);
        inputTime = findViewById(R.id.inputTime);
        inputTime.setIs24HourView(true);
        textRovidites = findViewById(R.id.textRovidites);
        textNev = findViewById(R.id.textNev);
        db = new Database(this);
        m = new Metodus(this);
        s = getSharedPreferences("variables", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnInsert:
                insertJarat();
                onBackPressed();
                break;
        }
    }

    public void selectUtvonal() {
        Cursor eredmeny = db.selectUtvonal(s.getString("utvonalId", ""));
        if (eredmeny != null && eredmeny.getCount() > 0) {
            while (eredmeny.moveToNext()) {
                textRovidites.setText(eredmeny.getString(1) + " - " + eredmeny.getString(3));
                textNev.setText(eredmeny.getString(0) + " - " + eredmeny.getString(2));
            }
        }
    }

    public String timeToString(int hour, int minute) {
        return hour + ":" + minute + ":00";
    }

    public void insertJarat() {
        boolean eredmeny = db.insertJarat(s.getString("utvonalId", ""), m.dateToString(inputDate.getYear(), inputDate.getMonth(), inputDate.getDayOfMonth()), timeToString(inputTime.getCurrentHour(), inputTime.getCurrentMinute()));
        if (eredmeny)
            Toast.makeText(this, getString(R.string.successInsert), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getString(R.string.unsuccessInsert), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        s.edit().remove("utvonalId").apply();
        Intent intent = new Intent(JaratInsertActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
