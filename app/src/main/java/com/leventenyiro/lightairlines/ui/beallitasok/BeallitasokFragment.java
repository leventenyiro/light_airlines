package com.leventenyiro.lightairlines.ui.beallitasok;

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
import androidx.lifecycle.ViewModelProviders;

import com.leventenyiro.lightairlines.Database;
import com.leventenyiro.lightairlines.PasswordUpdate;
import com.leventenyiro.lightairlines.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeallitasokFragment extends Fragment implements View.OnClickListener{

    private BeallitasokViewModel beallitasokViewModel;
    private EditText inputUsername, inputEmail, inputFirstname, inputLastname;
    private Button btnUpdate, btnCancel, btnPasswordUpdate;
    private Database db;
    private int userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        beallitasokViewModel =
                ViewModelProviders.of(this).get(BeallitasokViewModel.class);
        View root = inflater.inflate(R.layout.fragment_beallitasok, container, false);

        inputUsername = root.findViewById(R.id.inputUsername);
        inputEmail = root.findViewById(R.id.inputEmail);
        inputFirstname = root.findViewById(R.id.inputFirstname);
        inputLastname = root.findViewById(R.id.inputLastname);
        btnUpdate = root.findViewById(R.id.btnUpdate);
        btnCancel = root.findViewById(R.id.btnCancel);
        btnPasswordUpdate = root.findViewById(R.id.btnPasswordUpdate);
        db = new Database(getActivity());
        userId = Integer.parseInt(this.getActivity().getSharedPreferences("variables", Context.MODE_PRIVATE).getString("userId", ""));


        beallitasok();

        inputUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                inputUsername.setBackground(getResources().getDrawable(R.drawable.input));
                inputUsername.setPaddingRelative(70, 40, 40, 40);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                inputEmail.setBackground(getResources().getDrawable(R.drawable.input));
                inputEmail.setPaddingRelative(70, 40, 40, 40);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        inputFirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                inputFirstname.setBackground(getResources().getDrawable(R.drawable.input));
                inputFirstname.setPaddingRelative(70, 40, 40, 40);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        inputLastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                inputLastname.setBackground(getResources().getDrawable(R.drawable.input));
                inputLastname.setPaddingRelative(70, 40, 40, 40);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnPasswordUpdate.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnUpdate:
                if (!inputUsername.isEnabled())
                {
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
                else
                {
                    ellenorzes();
                    if (vanEUsername())
                    {
                        Toast.makeText(getActivity(), "A felhasználónév foglalt!", Toast.LENGTH_LONG).show();
                    }
                    else if (!usernameEllenorzes(inputUsername.getText().toString()))
                    {
                        Toast.makeText(getActivity(), "A felhasználónév túl rövid!", Toast.LENGTH_LONG).show();
                    }
                    else if (inputUsername.getText().toString().isEmpty())
                    {
                        Toast.makeText(getActivity(), "Nincs megadva felhasználónév!", Toast.LENGTH_LONG).show();
                    }
                    else if (vanEEmail())
                    {
                        Toast.makeText(getActivity(), "Az e-mail cím foglalt!", Toast.LENGTH_LONG).show();
                    }
                    else if (inputEmail.getText().toString().isEmpty())
                    {
                        Toast.makeText(getActivity(), "Nincs megadva e-mail cím!", Toast.LENGTH_LONG).show();
                    }
                    else if (!emailEllenorzes(inputEmail.getText().toString()))
                    {
                        Toast.makeText(getActivity(), "Helytelen e-mail cím!", Toast.LENGTH_LONG).show();
                    }
                    else if (inputFirstname.getText().toString().isEmpty())
                    {
                        Toast.makeText(getActivity(), "Nincs megadva keresztnév!", Toast.LENGTH_SHORT).show();
                    }
                    else if (inputLastname.getText().toString().isEmpty())
                    {
                        Toast.makeText(getActivity(), "Nincs megadva vezetéknév!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        inputsDisable();
                        if (update())
                        {
                            btnCancel.setVisibility(View.INVISIBLE);
                            btnUpdate.setText("Módosítás");
                        }
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
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void beallitasok()
    {
        Cursor eredmeny = db.selectAll(userId);
        if (eredmeny != null && eredmeny.getCount() > 0)
        {
            while (eredmeny.moveToNext())
            {
                inputUsername.setText(eredmeny.getString(0));
                inputEmail.setText(eredmeny.getString(1));
                inputFirstname.setText(eredmeny.getString(2));
                inputLastname.setText(eredmeny.getString(3));
            }
        }
    }

    public void inputsDisable()
    {
        inputUsername.setEnabled(false);
        inputUsername.setTextColor(getResources().getColor(R.color.midGray));
        inputUsername.setBackground(getResources().getDrawable(R.drawable.input));
        inputUsername.setPaddingRelative(70, 40, 40, 40);
        inputEmail.setEnabled(false);
        inputEmail.setTextColor(getResources().getColor(R.color.midGray));
        inputEmail.setBackground(getResources().getDrawable(R.drawable.input));
        inputEmail.setPaddingRelative(70, 40, 40, 40);
        inputFirstname.setEnabled(false);
        inputFirstname.setTextColor(getResources().getColor(R.color.midGray));
        inputFirstname.setBackground(getResources().getDrawable(R.drawable.input));
        inputFirstname.setPaddingRelative(70, 40, 40, 40);
        inputLastname.setEnabled(false);
        inputLastname.setTextColor(getResources().getColor(R.color.midGray));
        inputLastname.setBackground(getResources().getDrawable(R.drawable.input));
        inputLastname.setPaddingRelative(70, 40, 40, 40);
    }

    private void ellenorzes() {
        if (!inputUsername.getText().toString().isEmpty() && usernameEllenorzes(inputUsername.getText().toString()))
        {
            inputUsername.setBackground(getResources().getDrawable(R.drawable.inputgreen));
            inputUsername.setPaddingRelative(70, 40, 40, 40);
        }
        if (!inputEmail.getText().toString().isEmpty() && emailEllenorzes(inputEmail.getText().toString()))
        {
            inputEmail.setBackground(getResources().getDrawable(R.drawable.inputgreen));
            inputEmail.setPaddingRelative(70, 40, 40, 40);
        }
        if (!inputFirstname.getText().toString().isEmpty())
        {
            inputFirstname.setBackground(getResources().getDrawable(R.drawable.inputgreen));
            inputFirstname.setPaddingRelative(70, 40, 40, 40);
        }
        if (!inputLastname.getText().toString().isEmpty())
        {
            inputLastname.setBackground(getResources().getDrawable(R.drawable.inputgreen));
            inputLastname.setPaddingRelative(70, 40, 40, 40);
        }
        if (vanEUsername() || inputUsername.getText().toString().isEmpty() || !usernameEllenorzes(inputUsername.getText().toString()))
        {
            inputUsername.setBackground(getResources().getDrawable(R.drawable.inputred));
            inputUsername.setPaddingRelative(70, 40, 40, 40);
        }
        if (vanEEmail() || inputEmail.getText().toString().isEmpty() || !emailEllenorzes(inputEmail.getText().toString()))
        {
            inputEmail.setBackground(getResources().getDrawable(R.drawable.inputred));
            inputEmail.setPaddingRelative(70, 40, 40, 40);
        }
        if (inputFirstname.getText().toString().isEmpty())
        {
            inputFirstname.setBackground(getResources().getDrawable(R.drawable.inputred));
            inputFirstname.setPaddingRelative(70, 40, 40, 40);
        }
        if (inputLastname.getText().toString().isEmpty())
        {
            inputLastname.setBackground(getResources().getDrawable(R.drawable.inputred));
            inputLastname.setPaddingRelative(70, 40, 40, 40);
        }
    }

    public boolean usernameEllenorzes(String username)
    {
        if (username.length() >= 5)
            return true;
        return false;
    }
    public boolean emailEllenorzes(String email)
    {
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
    public boolean vanEUsername()
    {
        String username = "";
        Cursor eredmeny = db.selectAll(userId);
        if (eredmeny != null && eredmeny.getCount() > 0)
        {
            while (eredmeny.moveToNext())
            {
                username = eredmeny.getString(0);
            }
        }
        if (username.equals(inputUsername.getText().toString()))
        {
            return false;
        }
        else
        {
            eredmeny = db.selectUsername(inputUsername.getText().toString());
            return eredmeny.getCount() == 1;
        }
    }
    public boolean vanEEmail()
    {
        String email = "";
        Cursor eredmeny = db.selectAll(userId);
        if (eredmeny != null && eredmeny.getCount() > 0)
        {
            while (eredmeny.moveToNext())
            {
                email = eredmeny.getString(1);
            }
        }
        if (email.equals(inputEmail.getText().toString()))
        {
            return false;
        }
        else
        {
            eredmeny = db.selectEmail(inputEmail.getText().toString());
            return eredmeny.getCount() == 1;
        }
    }
    public String elsoNagybetu(String nev)
    {
        String[] nevek = nev.split(" ");
        String ujNev = "";
        for (String s : nevek) {
            ujNev += s.toUpperCase().charAt(0) + s.toLowerCase().substring(1, s.length()) + " ";
        }
        return ujNev;
    }

    public boolean update()
    {
        long eredmeny = db.update(String.valueOf(userId), inputUsername.getText().toString(), inputEmail.getText().toString(), inputFirstname.getText().toString(), inputLastname.getText().toString());
        if (eredmeny == -1 || eredmeny == 0)
        {
            Toast.makeText(getActivity(), "Szerverhiba! Sikertelen módosítás!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            Toast.makeText(getActivity(), "Sikeres módosítás!", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}