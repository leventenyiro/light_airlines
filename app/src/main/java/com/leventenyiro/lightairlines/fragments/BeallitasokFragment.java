package com.leventenyiro.lightairlines.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leventenyiro.lightairlines.segedOsztalyok.Database;
import com.leventenyiro.lightairlines.PasswordUpdate;
import com.leventenyiro.lightairlines.R;

import java.util.regex.Pattern;

public class BeallitasokFragment extends Fragment implements View.OnClickListener {

    private Button btnUpdate, btnCancel, btnPasswordUpdate, btnLogout;
    private Database db;
    private EditText inputUsername, inputEmail, inputFirstname, inputLastname;
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
                    btnUpdate.setText("Mentés");
                }
                else {
                    if (vanEUsername()) {
                        inputSzin("usernameRed");
                        Toast.makeText(getActivity(), "A felhasználónév foglalt!", Toast.LENGTH_LONG).show();
                    }
                    else if (!usernameEllenorzes(inputUsername.getText().toString())) {
                        inputSzin("usernameRed");
                        Toast.makeText(getActivity(), "A felhasználónév túl rövid!", Toast.LENGTH_LONG).show();
                    }
                    else if (inputUsername.getText().toString().isEmpty()) {
                        inputSzin("usernameRed");
                        Toast.makeText(getActivity(), "Nincs megadva felhasználónév!", Toast.LENGTH_LONG).show();
                    }
                    else if (vanEEmail()) {
                        inputSzin("usernameGreen");
                        inputSzin("emailRed");
                        Toast.makeText(getActivity(), "Az e-mail cím foglalt!", Toast.LENGTH_LONG).show();
                    }
                    else if (inputEmail.getText().toString().isEmpty()) {
                        inputSzin("usernameGreen");
                        inputSzin("emailRed");
                        Toast.makeText(getActivity(), "Nincs megadva e-mail cím!", Toast.LENGTH_LONG).show();
                    }
                    else if (!emailEllenorzes(inputEmail.getText().toString())) {
                        inputSzin("usernameGreen");
                        inputSzin("emailRed");
                        Toast.makeText(getActivity(), "Helytelen e-mail cím!", Toast.LENGTH_LONG).show();
                    }
                    else if (inputFirstname.getText().toString().isEmpty()) {
                        inputSzin("usernameGreen");
                        inputSzin("emailGreen");
                        inputSzin("firstnameRed");
                        Toast.makeText(getActivity(), "Nincs megadva keresztnév!", Toast.LENGTH_SHORT).show();
                    }
                    else if (inputLastname.getText().toString().isEmpty()) {
                        inputSzin("usernameGreen");
                        inputSzin("emailGreen");
                        inputSzin("firstnameGreen");
                        inputSzin("lastnameRed");
                        Toast.makeText(getActivity(), "Nincs megadva vezetéknév!", Toast.LENGTH_SHORT).show();
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
                btnUpdate.setText("Módosítás");
                break;
            case R.id.btnPasswordUpdate:
                Intent intent = new Intent(getActivity(), PasswordUpdate.class);
                startActivity(intent);
                //getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.btnLogout:
                getActivity().onBackPressed();
                break;
        }
    }

    public void beallitasok() {
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

    public void inputsDisable() {
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

    public void inputSzin(String mod) {
        switch (mod) {
            case "username":
                inputUsername.setBackground(getResources().getDrawable(R.drawable.input));
                inputUsername.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "usernameGreen":
                inputUsername.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputUsername.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "usernameRed":
                inputUsername.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputUsername.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "email":
                inputEmail.setBackground(getResources().getDrawable(R.drawable.input));
                inputEmail.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "emailGreen":
                inputEmail.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputEmail.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "emailRed":
                inputEmail.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputEmail.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "firstname":
                inputFirstname.setBackground(getResources().getDrawable(R.drawable.input));
                inputFirstname.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "firstnameGreen":
                inputFirstname.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputFirstname.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "firstnameRed":
                inputFirstname.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputFirstname.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "lastname":
                inputLastname.setBackground(getResources().getDrawable(R.drawable.input));
                inputLastname.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "lastnameGreen":
                inputLastname.setBackground(getResources().getDrawable(R.drawable.inputgreen));
                inputLastname.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
            case "lastnameRed":
                inputLastname.setBackground(getResources().getDrawable(R.drawable.inputred));
                inputLastname.setPaddingRelative(dpToPx(20), dpToPx(15), dpToPx(20), dpToPx(15));
                break;
        }
    }

    public int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }


    public boolean usernameEllenorzes(String username) {
        return username.length() >= 5;
    }

    public boolean emailEllenorzes(String email) {
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailPattern);
        return pattern.matcher(email).matches();
    }

    public boolean vanEUsername() {
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

    public boolean vanEEmail() {
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

    public String elsoNagybetu(String nev) {
        String[] nevek = nev.split(" ");
        String ujNev = "";
        for (String s : nevek) {
            ujNev += s.toUpperCase().charAt(0) + s.toLowerCase().substring(1, s.length()) + " ";
        }
        return ujNev;
    }

    public void update() {
        boolean eredmeny = db.updateUser(userId, inputUsername.getText().toString(), inputEmail.getText().toString(), elsoNagybetu(inputFirstname.getText().toString()), elsoNagybetu(inputLastname.getText().toString()));
        if (eredmeny) {
            Toast.makeText(getActivity(), "Sikeres módosítás!", Toast.LENGTH_SHORT).show();
            btnCancel.setVisibility(View.INVISIBLE);
            btnUpdate.setText("Módosítás");
        }
        else {
            Toast.makeText(getActivity(), "Szerverhiba! Sikertelen módosítás!", Toast.LENGTH_SHORT).show();
        }
    }
}