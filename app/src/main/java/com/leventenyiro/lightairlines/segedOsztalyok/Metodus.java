package com.leventenyiro.lightairlines.segedOsztalyok;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.TypedValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Metodus {

    private Database db;

    public Metodus(Context context) {
        db = new Database(context);
    }

    public int dpToPx(int dp, Resources r) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public boolean usernameEllenorzes(String username) {
        return username.length() >= 5;
    }

    public boolean emailEllenorzes(String email) {
        String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailPattern);
        return pattern.matcher(email).matches();
    }




    public boolean jelszoEllenorzes(String userId, String inputPassword) {
        Cursor eredmeny = db.selectPasswordById(userId);

        String password = null;
        String salt = null;

        if (eredmeny != null && eredmeny.getCount() > 0) {
            while (eredmeny.moveToNext()) {
                String[] adatok = eredmeny.getString(0).split(";");
                password = adatok[0];
                salt = adatok[1];
            }
        }
        return PasswordUtils.verifyUserPassword(inputPassword, password, salt);
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
            return idoresz[0].substring(1, 2) + " óra " + idoresz[1] + " perc";
        }
        else {
            return idoresz[0] + " óra " + idoresz[1] + " perc";
        }
    }
}
