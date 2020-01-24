package com.leventenyiro.lightairlines.adminActivityk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztalyok.Database;
import com.leventenyiro.lightairlines.segedOsztalyok.Metodus;

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
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_megtekintes);

        init();

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

        alertDialogBuilder = new AlertDialog.Builder(MegtekintesActivity.this);
        //alertDialogBuilder.setTitle(getString(R.string.seatInfo) + " " + );
        alertDialogBuilder.setMessage(getString(R.string.finalizeMessage));
        alertDialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog = alertDialogBuilder.create();
    }

    private void init() {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MegtekintesActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
