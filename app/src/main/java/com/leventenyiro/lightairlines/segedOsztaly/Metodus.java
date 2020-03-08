package com.leventenyiro.lightairlines.segedOsztaly;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leventenyiro.lightairlines.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Metodus {

    private Context c;
    private DatabaseReference db;
    private List<String> foglaltHelyek;

    public Metodus(Context context) {
        c = context;
        db = FirebaseDatabase.getInstance().getReference();
        foglaltHelyek = new LinkedList<>();
    }

    public int dpToPx(int dp, Resources r) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void loading(View view) {

        if (view.getVisibility() == View.VISIBLE)
            view.setVisibility(View.INVISIBLE);
        else
            view.setVisibility(View.VISIBLE);
    }

    public boolean usernameHosszEllenorzes(String username) {
        return username.length() >= 5;
    }

    public boolean usernameWhiteSpaceEllenorzes(String username) {
        return !username.contains(" ");
    }

    public boolean emailEllenorzes(String email) {
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailPattern);
        return pattern.matcher(email).matches();
    }

    public String elsoNagybetu(String nev) {
        String[] nevek = nev.split(" ");
        String ujNev = "";
        for (String s : nevek) {
            ujNev += s.toUpperCase().charAt(0) + s.toLowerCase().substring(1, s.length()) + " ";
        }
        return ujNev;
    }

    public boolean jelszoErossegEllenorzes(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public String idotartamAtalakitas(String idotartam) {
        String[] idoresz = idotartam.split(":");
        if (Integer.parseInt(idoresz[0]) < 10) {
            return idoresz[0].substring(1, 2) + " " + c.getString(R.string.hour) + " " + idoresz[1] + " " + c.getString(R.string.minute);
        }
        else {
            return idoresz[0] + " " + c.getString(R.string.hour) + " " + idoresz[1] + " " + c.getString(R.string.minute);
        }
    }

    public String ulesKodolas(int szam) {
        String ules = "";
        ules += szam / 6 + 1;
        switch (szam % 6) {
            case 0: ules += "A"; break;
            case 1: ules += "B"; break;
            case 2: ules += "C"; break;
            case 3: ules += "D"; break;
            case 4: ules += "E"; break;
            case 5: ules += "F"; break;
        }
        return ules;
    }

    public int ulesDekodolas(String ules) {
        int szam;
        if (ules.length() == 2) {
            szam = (Integer.parseInt(ules.substring(0,1)) - 1) * 6;
            switch (ules.substring(1,2)) {
                case "A": szam += 0; break;
                case "B": szam += 1; break;
                case "C": szam += 2; break;
                case "D": szam += 3; break;
                case "E": szam += 4; break;
                case "F": szam += 5; break;
            }
        }
        else {
            szam = (Integer.parseInt(ules.substring(0, 2)) - 1) * 6;
            switch (ules.substring(2, 3)) {
                case "A": szam += 0; break;
                case "B": szam += 1; break;
                case "C": szam += 2; break;
                case "D": szam += 3; break;
                case "E": szam += 4; break;
                case "F": szam += 5; break;
            }
        }
        return szam;
    }

    public boolean foglaltE(int id, String jaratId) {
        for (String hely : selectFoglaltHelyek(jaratId)) {
            if (id == ulesDekodolas(hely))
                return true;
        }
        return false;
    }

    public void addFoglaltHelyek(String jaratId) {
        db.child("foglalas").orderByChild("jarat_id").equalTo(jaratId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    foglaltHelyek.add(String.valueOf(snapshot.child("ules").getValue()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public List<String> selectFoglaltHelyek(String jaratId) {
        addFoglaltHelyek(jaratId);
        return foglaltHelyek;
    }

    public String dateToString(int year, int month, int day) {
        if (month < 10)
            return year + "-0" + (month + 1) + "-" + day;
        return year + "-" + (month + 1) + "-" + day;
    }
}
