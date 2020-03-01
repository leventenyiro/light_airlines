package com.leventenyiro.lightairlines.userActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.Database;
import com.leventenyiro.lightairlines.segedOsztaly.Metodus;

public class JegyActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDelete;
    private Database db;
    private ImageView btnBack, imgQr;
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
        qrCode("lightairlines_foglalas_" + s.getString("foglalasId", ""));
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
        imgQr = findViewById(R.id.imgQr);
        db = new Database(this);
        m = new Metodus(this);
        s = getSharedPreferences("variables", Context.MODE_PRIVATE);
        brightness = Settings.System.getInt(getApplicationContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
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
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    private void select() {
        Cursor e = db.selectJegy(s.getString("foglalasId", ""));
        if (e != null && e.getCount() > 0) {
            while (e.moveToNext()) {
                String roviditesInfo = e.getString(3) + " - " + e.getString(5);
                textRovidites.setText(roviditesInfo);
                String nevInfo = e.getString(2) + " - " + e.getString(4);
                textNev.setText(nevInfo);
                textIdopont.setText(e.getString(1).substring(0, 16).replace('-', '.'));
                textIdotartam.setText(m.idotartamAtalakitas(e.getString(6)));
                String ulesInfo = getString(R.string.seatInfo) + " " + e.getString(0);
                textUles.setText(ulesInfo);
            }
        }
    }

    public void qrCode(String ertek) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(ertek, BarcodeFormat.QR_CODE, m.dpToPx(200, getResources()), m.dpToPx(200, getResources()));
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imgQr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
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
