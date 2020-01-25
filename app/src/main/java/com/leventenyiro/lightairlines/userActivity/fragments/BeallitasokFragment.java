package com.leventenyiro.lightairlines.userActivity.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leventenyiro.lightairlines.segedOsztaly.Database;
import com.leventenyiro.lightairlines.globalActivity.PasswordUpdate;
import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.Metodus;

public class BeallitasokFragment extends Fragment implements View.OnClickListener {

    private Button btnUpdate, btnCancel, btnPasswordUpdate, btnLogout;
    private Database db;
    private EditText inputUsername, inputEmail, inputFirstname, inputLastname;
    private int dp15, dp20;
    private Metodus m;
    private String userId;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_beallitasok, container, false);
        init(root);
        beallitasok();
        inputUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputSzin("username");
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputSzin("email");
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputFirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputSzin("firstname");
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputLastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputSzin("lastname");
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnPasswordUpdate.setOnClickListener(this);
        return root;
    }

    private void init(View root) {
        inputUsername = root.findViewById(R.id.inputUsername);
        inputEmail = root.findViewById(R.id.inputEmail);
        inputFirstname = root.findViewById(R.id.inputFirstname);
        inputLastname = root.findViewById(R.id.inputLastname);
        btnUpdate = root.findViewById(R.id.btnUpdate);
        btnCancel = root.findViewById(R.id.btnCancel);
        btnPasswordUpdate = root.findViewById(R.id.btnPasswordUpdate);
        btnLogout = root.findViewById(R.id.btnLogout);
        db = new Database(getActivity());
        m = new Metodus(getActivity());
        dp15 = m.dpToPx(15, getResources());
        dp20 = m.dpToPx(20, getResources());
        userId = this.getActivity().getSharedPreferences("variables", Context.MODE_PRIVATE).getString("userId", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                if (!inputUsername.isEnabled()) {
                    inputUsername.setEnabled(true);
                    inputUsername.setTextColor(getResources().getColor(R.color.gray));
                    inputEmail.setEnabled(true);
                    inputEmail.setTextColor(getResources().getColor(R.color.gray));
                    inputFirstname.setEnabled(true);
                    inputFirstname.setTextColor(getResources().getColor(R.color.gray));
                    inputLastname.setEnabled(true);
                    inputLastname.setTextColor(getResources().getColor(R.color.gray));
                    btnCancel.setVisibility(View.VISIBLE);
                    btnUpdate.setText(getString(R.string.save));
                }
                else {
                    if (vanEUsername()) {
                        inputSzin("usernameRed");
                        Toast.makeText(getActivity(), getString(R.string.usernameExists), Toast.LENGTH_LONG).show();
                    }
                    else if (!m.usernameWhiteSpaceEllenorzes(inputUsername.getText().toString())) {
                        Toast.makeText(getActivity(), getString(R.string.usernameWhiteSpace), Toast.LENGTH_LONG).show();
                        inputSzin("usernameRed");
                    }
                    else if (!m.usernameHosszEllenorzes(inputUsername.getText().toString())) {
                        Toast.makeText(getActivity(), getString(R.string.username5char), Toast.LENGTH_LONG).show();
                        inputSzin("usernameRed");
                    }
                    else if (inputUsername.getText().toString().isEmpty()) {
                        inputSzin("usernameRed");
                        Toast.makeText(getActivity(), getString(R.string.noUsername), Toast.LENGTH_LONG).show();
                    }
                    else if (vanEEmail()) {
                        inputSzin("usernameGreen");
                        inputSzin("emailRed");
                        Toast.makeText(getActivity(), getString(R.string.emailExists), Toast.LENGTH_LONG).show();
                    }
                    else if (!m.emailEllenorzes(inputEmail.getText().toString())) {
                        inputSzin("usernameGreen");
                        inputSzin("emailRed");
                        Toast.makeText(getActivity(), getString(R.string.wrongEmail), Toast.LENGTH_LONG).show();
                    }
                    else if (inputEmail.getText().toString().isEmpty()) {
                        inputSzin("usernameGreen");
                        inputSzin("emailRed");
                        Toast.makeText(getActivity(), getString(R.string.noEmail), Toast.LENGTH_LONG).show();
                    }
                    else if (inputFirstname.getText().toString().isEmpty()) {
                        inputSzin("usernameGreen");
                        inputSzin("emailGreen");
                        inputSzin("firstnameRed");
                        Toast.makeText(getActivity(), getString(R.string.noFirstname), Toast.LENGTH_LONG).show();
                    }
                    else if (inputLastname.getText().toString().isEmpty()) {
                        inputSzin("usernameGreen");
                        inputSzin("emailGreen");
                        inputSzin("firstnameGreen");
                        inputSzin("lastnameRed");
                        Toast.makeText(getActivity(), getString(R.string.noLastname), Toast.LENGTH_LONG).show();
                    }
                    else {
                        inputsDisable();
                        update();
                    }
                }
                break;
            case R.id.btnCancel:
                inputsDisable();
                beallitasok();
                btnCancel.setVisibility(View.INVISIBLE);
                btnUpdate.setText(getString(R.string.modify));
                break;
            case R.id.btnPasswordUpdate:
                getActivity().getSharedPreferences("variables", Context.MODE_PRIVATE).edit().putString("fragment", "beallitasok").apply();
                Intent intent = new Intent(getActivity(), PasswordUpdate.class);
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.btnLogout:
                getActivity().onBackPressed();
                break;
        }
    }

    private void beallitasok() {
        Cursor eredmeny = db.selectUser(userId);
        if (eredmeny != null && eredmeny.getCount() > 0) {
            while (eredmeny.moveToNext()) {
                inputUsername.setText(eredmeny.getString(0));
                inputEmail.setText(eredmeny.getString(1));
                inputFirstname.setText(eredmeny.getString(2));
                inputLastname.setText(eredmeny.getString(3));
            }
        }
    }

    private void inputsDisable() {
        inputUsername.setEnabled(false);
        inputUsername.setTextColor(getResources().getColor(R.color.midGray));
        inputSzin("username");
        inputEmail.setEnabled(false);
        inputEmail.setTextColor(getResources().getColor(R.color.midGray));
        inputSzin("email");
        inputFirstname.setEnabled(false);
        inputFirstname.setTextColor(getResources().getColor(R.color.midGray));
        inputSzin("firstname");
        inputLastname.setEnabled(false);
        inputLastname.setTextColor(getResources().getColor(R.color.midGray));
        inputSzin("lastname");
    }

    private void inputSzin(String mod) {
        switch (mod) {
            case "username":
                inputUsername.setBackground(getResources().getDrawable(R.drawable.input));
                inputUsername.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "usernameGreen":
                inputUsername.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputUsername.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "usernameRed":
                inputUsername.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputUsername.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "email":
                inputEmail.setBackground(getResources().getDrawable(R.drawable.input));
                inputEmail.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "emailGreen":
                inputEmail.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputEmail.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "emailRed":
                inputEmail.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputEmail.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "firstname":
                inputFirstname.setBackground(getResources().getDrawable(R.drawable.input));
                inputFirstname.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "firstnameGreen":
                inputFirstname.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputFirstname.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "firstnameRed":
                inputFirstname.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputFirstname.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "lastname":
                inputLastname.setBackground(getResources().getDrawable(R.drawable.input));
                inputLastname.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "lastnameGreen":
                inputLastname.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputLastname.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
            case "lastnameRed":
                inputLastname.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputLastname.setPaddingRelative(dp20, dp15, dp20, dp15);
                break;
        }
    }

    private boolean vanEUsername() {
        String username = "";
        Cursor eredmeny = db.selectUser(userId);
        if (eredmeny != null && eredmeny.getCount() > 0) {
            while (eredmeny.moveToNext()) {
                username = eredmeny.getString(0);
            }
        }
        if (username.equals(inputUsername.getText().toString())) {
            return false;
        }
        else {
            eredmeny = db.selectUsername(inputUsername.getText().toString());
            return eredmeny.getCount() == 1;
        }
    }

    private boolean vanEEmail() {
        String email = "";
        Cursor eredmeny = db.selectUser(userId);
        if (eredmeny != null && eredmeny.getCount() > 0) {
            while (eredmeny.moveToNext()) {
                email = eredmeny.getString(1);
            }
        }
        if (email.equals(inputEmail.getText().toString())) {
            return false;
        }
        else {
            eredmeny = db.selectEmail(inputEmail.getText().toString());
            return eredmeny.getCount() == 1;
        }
    }

    private void update() {
        boolean eredmeny = db.updateUser(userId, inputUsername.getText().toString(), inputEmail.getText().toString(), m.elsoNagybetu(inputFirstname.getText().toString()), m.elsoNagybetu(inputLastname.getText().toString()));
        if (eredmeny) {
            Toast.makeText(getActivity(), getString(R.string.successModify), Toast.LENGTH_SHORT).show();
            btnCancel.setVisibility(View.INVISIBLE);
            btnUpdate.setText(getString(R.string.modify));
        }
        else {
            Toast.makeText(getActivity(), getString(R.string.unsuccessModify), Toast.LENGTH_SHORT).show();
        }
    }
}