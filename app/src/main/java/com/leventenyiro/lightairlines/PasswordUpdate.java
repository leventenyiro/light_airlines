package com.leventenyiro.lightairlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUpdate extends AppCompatActivity implements View.OnClickListener{

    private EditText inputOldPassword, inputPassword, inputPasswordAgain;
    private Button btnCancel, btnUpdate;
    private ImageView btnBack;
    private Database db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordupdate);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        inputOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                inputOldPassword.setBackground(getResources().getDrawable(R.drawable.input));
                inputOldPassword.setPaddingRelative(70, 40, 40, 40);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                inputPassword.setBackground(getResources().getDrawable(R.drawable.input));
                inputPassword.setPaddingRelative(70, 40, 40, 40);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                inputPasswordAgain.setBackground(getResources().getDrawable(R.drawable.input));
                inputPasswordAgain.setPaddingRelative(70, 40, 40, 40);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    private void init() {
        inputOldPassword = findViewById(R.id.inputOldPassword);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordAgain = findViewById(R.id.inputPasswordAgain);
        btnBack = findViewById(R.id.btnBack);
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);
        db = new Database(this);
        SharedPreferences sharedPreferences = getSharedPreferences("variables", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnBack: onBackPressed(); break;
            case R.id.btnCancel: onBackPressed(); break;
            case R.id.btnUpdate:
                if (inputOldPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Nincs megadva a régi jelszó!", Toast.LENGTH_SHORT).show();
                    inputClear();
                }
                else if (!jelszoEllenorzes())
                {
                    Toast.makeText(this, "Helytelen a régi jelszó!", Toast.LENGTH_SHORT).show();
                    inputClear();
                }
                else if (!jelszoErossegEllenorzes(inputPassword.getText().toString()))
                {
                    Toast.makeText(this, "Gyenge jelszó!", Toast.LENGTH_SHORT).show();
                    inputClear();
                }
                else if (inputPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Nincs megadva jelszó!", Toast.LENGTH_SHORT).show();
                    inputClear();
                }
                else if (inputPasswordAgain.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Ismételd meg a jelszót!", Toast.LENGTH_SHORT).show();
                    inputClear();
                }
                else if (!inputPassword.getText().toString().equals(inputPasswordAgain.getText().toString()))
                {
                    Toast.makeText(this, "A két jelszó nem egyezik!", Toast.LENGTH_SHORT).show();
                    inputClear();
                }
                else
                {
                    jelszoInsert();
                    onBackPressed();
                }
                break;
        }
    }

    public void inputClear()
    {
        inputOldPassword.setText("");
        inputPassword.setText("");
        inputPasswordAgain.setText("");
    }

    public boolean jelszoEllenorzes()
    {

        Cursor eredmeny = db.selectPasswordById(userId);

        String password = null;
        String salt = null;

        if (eredmeny != null && eredmeny.getCount() > 0)
        {
            while (eredmeny.moveToNext())
            {
                String[] adatok = eredmeny.getString(0).split(";");
                password = adatok[0];
                salt = adatok[1];
            }
        }
        return PasswordUtils.verifyUserPassword(inputOldPassword.getText().toString(), password, salt);
    }

    private void jelszoInsert()
    {
        String salt = PasswordUtils.getSalt(30);
        String titkositottPassword = PasswordUtils.generateSecurePassword(inputPassword.getText().toString(), salt);
        String password = titkositottPassword + ";" + salt;

        if (db.updatePassword(userId, password))
            Toast.makeText(this, "Sikeres jelszómódosítás!", Toast.LENGTH_LONG);
        else
            Toast.makeText(this, "Szerverhiba! Sikertelen jelszómódosítás!", Toast.LENGTH_SHORT).show();
    }

    public boolean jelszoErossegEllenorzes(String password)
    {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
