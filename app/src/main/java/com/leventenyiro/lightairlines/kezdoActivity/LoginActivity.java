package com.leventenyiro.lightairlines.kezdoActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.adminActivity.*;
import com.leventenyiro.lightairlines.segedOsztaly.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private Button btnLogin;
    private EditText inputUsernameEmail, inputPassword;
    private FirebaseAuth mAuth;
    private int dp15, dp20;
    private Metodus m;
    private TextView btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        inputUsernameEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputSzin("usernameEmail");
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputSzin("password");
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnReg.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    public void init() {
        btnReg = findViewById(R.id.btnReg);
        btnLogin = findViewById(R.id.btnLogin);
        inputUsernameEmail = findViewById(R.id.inputUsernameEmail);
        inputPassword = findViewById(R.id.inputPassword);
        m = new Metodus(this);
        dp15 = m.dpToPx(15, getResources());
        dp20 = m.dpToPx(20, getResources());
        mAuth = FirebaseAuth.getInstance();

        alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.exit));
        alertDialogBuilder.setMessage(getString(R.string.exitMessage));
        alertDialogBuilder.setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                System.exit(0);
            }
        });
        alertDialog = alertDialogBuilder.create();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnReg:
                if (!isNetworkConnected())
                    Toast.makeText(this, getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
                else {
                    intent = new Intent(LoginActivity.this, Reg1Activity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                break;
            case R.id.btnLogin:
                if (!isNetworkConnected())
                    Toast.makeText(this, getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
                else {
                    if (inputUsernameEmail.getText().toString().isEmpty()) {
                        Toast.makeText(this, getString(R.string.noUsernameEmail), Toast.LENGTH_LONG).show();
                        inputPassword.setText("");
                        inputSzin("usernameEmailRed");
                    }
                    else if (inputPassword.getText().toString().isEmpty()) {
                        Toast.makeText(this, getString(R.string.noPassword), Toast.LENGTH_LONG).show();
                        inputSzin("passwordRed");
                    }
                    else {
                        m.loading(findViewById(R.id.loading));
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
                        ref.orderByChild("username").equalTo(inputUsernameEmail.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if (dataSnapshot.getChildrenCount() == 1)
                                            login(String.valueOf(snapshot.child("email").getValue()));
                                    }
                                }
                                else
                                    login(inputUsernameEmail.getText().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                break;
        }
    }

    public void inputSzin(String mod) {
        switch (mod) {
            case "usernameEmail":
                inputUsernameEmail.setBackground(getResources().getDrawable(R.drawable.inputlogin));
                inputUsernameEmail.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "usernameEmailRed":
                inputUsernameEmail.setBackground(getResources().getDrawable(R.drawable.inputloginred));
                inputUsernameEmail.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "password":
                inputPassword.setBackground(getResources().getDrawable(R.drawable.inputlogin));
                inputPassword.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "passwordRed":
                inputPassword.setBackground(getResources().getDrawable(R.drawable.inputloginred));
                inputPassword.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
        }
    }

    private void login(String email) {
        final String finalEmail = email;
        mAuth.signInWithEmailAndPassword(email, inputPassword.getText().toString())
            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (finalEmail.equals("admin@lightairlines.com")) {
                            loginIntent(finalEmail);
                        }
                        else if (!user.isEmailVerified()) {
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(LoginActivity.this, getString(R.string.validateEmail), Toast.LENGTH_SHORT).show();
                                    inputPassword.setText("");
                                    inputSzin("usernameEmail");
                                    inputSzin("password");
                                    m.loading(findViewById(R.id.loading));
                                }
                            });
                        }
                        else {
                            loginIntent(finalEmail);
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, getString(R.string.wrongLoginData), Toast.LENGTH_LONG).show();
                        inputPassword.setText("");
                        inputSzin("usernameEmailRed");
                        inputSzin("passwordRed");
                        m.loading(findViewById(R.id.loading));
                    }
                }
            });
    }

    private void loginIntent(String email) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
        ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    getSharedPreferences("variables", Context.MODE_PRIVATE).edit()
                            .putString("userId", snapshot.getKey()).apply();
                    if (snapshot.getKey().equals("1")) {
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        m.loading(findViewById(R.id.loading));
                    }
                    else {
                        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        m.loading(findViewById(R.id.loading));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onBackPressed() {
        alertDialog.show();
    }
}
