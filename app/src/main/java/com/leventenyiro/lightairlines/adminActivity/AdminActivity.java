package com.leventenyiro.lightairlines.adminActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.kezdoActivity.LoginActivity;

public class AdminActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private SharedPreferences s;
    private String fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        init();

        switch (fragment) {
            case "beallitasok":
                navView.setSelectedItemId(R.id.navigation_admin_beallitasok);
                break;
            case "jaratInsert":
                navView.setSelectedItemId(R.id.navigation_admin_jarat_insert);
                break;
            default:
                navView.setSelectedItemId(R.id.navigation_admin_jaratok);
                break;
        }
        s.edit().remove("fragment").apply();
    }

    private void init() {
        s = getSharedPreferences("variables", Context.MODE_PRIVATE);
        fragment = s.getString("fragment", "");
        alertDialogBuilder = new AlertDialog.Builder(AdminActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.logout));
        alertDialogBuilder.setMessage(getString(R.string.logoutMessage));
        alertDialogBuilder.setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getSharedPreferences("variables", Context.MODE_PRIVATE).edit().remove("userId").apply();
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        alertDialog = alertDialogBuilder.create();
    }

    @Override
    public void onBackPressed() {
        alertDialog.show();
    }
}
