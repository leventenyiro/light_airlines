package com.leventenyiro.lightairlines.kezdoActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.*;

public class Reg3Activity extends AppCompatActivity implements View.OnClickListener {

    private Button btnReg;
    private DatabaseReference db;
    private EditText inputPassword, inputPasswordAgain;
    private FirebaseAuth mAuth;
    private ImageView btnBack, btnHome;
    private int dp15, dp20;
    private long id;
    private Metodus m;
    private SharedPreferences s;
    private TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg3);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputPassword.setBackground(getResources().getDrawable(R.drawable.input));
                inputPassword.setPaddingRelative(dp20, dp15, dp20, dp15);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputPasswordAgain.setBackground(getResources().getDrawable(R.drawable.input));
                inputPasswordAgain.setPaddingRelative(dp20, dp15, dp20, dp15);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnBack.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);
    }

    public void init() {
        btnBack = findViewById(R.id.btnBack);
        btnHome = findViewById(R.id.btnHome);
        btnLogin = findViewById(R.id.btnLogin);
        btnReg = findViewById(R.id.btnReg);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordAgain = findViewById(R.id.inputPasswordAgain);
        db = FirebaseDatabase.getInstance().getReference().child("user");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    id = dataSnapshot.getChildrenCount();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        mAuth = FirebaseAuth.getInstance();
        m = new Metodus(this);
        dp15 = m.dpToPx(15, getResources());
        dp20 = m.dpToPx(20, getResources());
        s = getSharedPreferences("regisztracio", Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnBack: onBackPressed(); break;
            case R.id.btnHome:
            case R.id.btnLogin: backToLogin(); break;
            case R.id.btnReg:
                if (inputPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.noPassword), Toast.LENGTH_LONG).show();
                    inputClear();
                }
                else if (inputPasswordAgain.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.noPasswordAgain), Toast.LENGTH_LONG).show();
                    inputClear();
                }
                else if (!inputPassword.getText().toString().equals(inputPasswordAgain.getText().toString())) {
                    Toast.makeText(this, getString(R.string.noPasswordPass), Toast.LENGTH_LONG).show();
                    inputClear();
                }
                else if (!m.jelszoErossegEllenorzes(inputPassword.getText().toString())) {
                    Toast.makeText(this, getString(R.string.weakPassword), Toast.LENGTH_LONG).show();
                    inputClear();
                }
                else {
                    m.loading(findViewById(R.id.loading));
                    s.edit().putString("password", inputPassword.getText().toString()).apply();
                    insertUser();
                    backToLogin();
                }
                break;
        }
    }

    public void inputClear() {
        inputPassword.setText("");
        inputPasswordAgain.setText("");
    }

    public void insertUser() {
        String username = s.getString("username", "");
        String email = s.getString("email", "");
        String firstname = s.getString("firstname", "");
        String lastname = s.getString("lastname", "");
        String birthdate = s.getString("birthdate", "");

        db.child(String.valueOf(id + 1)).child("username").setValue(username);
        db.child(String.valueOf(id + 1)).child("email").setValue(email);
        db.child(String.valueOf(id + 1)).child("firstname").setValue(firstname);
        db.child(String.valueOf(id + 1)).child("lastname").setValue(lastname);
        db.child(String.valueOf(id + 1)).child("birthdate").setValue(birthdate);

        mAuth.createUserWithEmailAndPassword(email, inputPassword.getText().toString())
                .addOnCompleteListener(Reg3Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser fUser = mAuth.getCurrentUser();
                            if (!fUser.isEmailVerified()) {
                                fUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Reg3Activity.this, getString(R.string.validateEmail), Toast.LENGTH_LONG).show();
                                        backToLogin();
                                        m.loading(findViewById(R.id.loading));
                                    }
                                });
                            }
                        }
                        else {
                            Toast.makeText(Reg3Activity.this, getString(R.string.unsuccessReg), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Reg3Activity.this, Reg2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void backToLogin() {
        s.edit().clear().apply();
        Intent intent = new Intent(Reg3Activity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
