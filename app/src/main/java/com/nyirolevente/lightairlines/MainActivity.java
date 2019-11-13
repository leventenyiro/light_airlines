package com.nyirolevente.lightairlines;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button btnLogin;
    private TextView btnReg;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        btnReg.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        //btnLogin.setEnabled(false); >> kell egy tiltott drawable
        //btnLogin.setBackground(getResources().getDrawable(R.drawable.buttondisabled));

        /*alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    alertDialog.setMessage("Biztos ki szeretnél lépni?");
                    alertDialog.show();
                }
                return false;
            }
        });*/
    }

    public void init()
    {
        btnLogin = findViewById(R.id.btnLogin);
        btnReg = findViewById(R.id.btnReg);
        /*alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setPositiveButton("Igen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("Nem", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnReg:
                Intent intent = new Intent(MainActivity.this, Reg1Activity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.);
                finish();
                break;
            case R.id.btnLogin:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
        }
    }
}
