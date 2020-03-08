package com.leventenyiro.lightairlines.userActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leventenyiro.lightairlines.R;

public class MegerositesActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCancel, btnVerify;
    private DatabaseReference db;
    private EditText inputPassword;
    private ImageView btnBack;
    private SharedPreferences s;
    private FirebaseAuth mAuth;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_megerosites);
        init();
        btnBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        btnCancel = findViewById(R.id.btnCancel);
        btnVerify = findViewById(R.id.btnVerify);
        inputPassword = findViewById(R.id.inputPassword);
        db = FirebaseDatabase.getInstance().getReference();
        s = getSharedPreferences("variables", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        db.child("user").orderByKey().equalTo(s.getString("userId", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    email = String.valueOf(snapshot.child("email").getValue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
            case R.id.btnCancel: onBackPressed(); break;
            case R.id.btnVerify:
                if (inputPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, getString(R.string.noPassword), Toast.LENGTH_LONG).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, inputPassword.getText().toString()).addOnCompleteListener(MegerositesActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                deleteJegy();
                                s.edit().remove("foglalasId").apply();
                                Intent intent = new Intent(MegerositesActivity.this, InnerActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            } else
                                rosszJelszo();
                        }
                    });
                }
                break;
        }
    }

    public void rosszJelszo() {
        Toast.makeText(this, getString(R.string.unsuccessFinalize), Toast.LENGTH_LONG).show();
        inputPassword.setText("");
    }

    public void deleteJegy() {
        db.child("foglalas").orderByKey().equalTo(s.getString("foglalasId", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MegerositesActivity.this,JegyActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
