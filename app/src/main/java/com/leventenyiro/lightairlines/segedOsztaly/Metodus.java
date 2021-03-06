package com.leventenyiro.lightairlines.segedOsztaly;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.TypedValue;

import com.leventenyiro.lightairlines.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Metodus {

    private Database db;
    private Context c;

    public Metodus(Context context) {
        db = new Database(context);
        c = context;
    }

    public int dpToPx(int dp, Resources r) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
            return idoresz[0].substring(1, 2) + " " + c.getString(R.string.hour) + " " + idoresz[1] + " " + c.getString(R.string.minute);
        } else {
            return idoresz[0] + " " + c.getString(R.string.hour) + " " + idoresz[1] + " " + c.getString(R.string.minute);
        }
    }

    public String ulesKodolas(int szam) {
        String ules = "";
        ules += szam / 6 + 1;
        switch (szam % 6) {
            case 0:
                ules += "A";
                break;
            case 1:
                ules += "B";
                break;
            case 2:
                ules += "C";
                break;
            case 3:
                ules += "D";
                break;
            case 4:
                ules += "E";
                break;
            case 5:
                ules += "F";
                break;
        }
        return ules;
    }

    public int ulesDekodolas(String ules) {
        int szam;
        if (ules.length() == 2) {
            szam = (Integer.parseInt(ules.substring(0, 1)) - 1) * 6;
            switch (ules.substring(1, 2)) {
                case "A":
                    szam += 0;
                    break;
                case "B":
                    szam += 1;
                    break;
                case "C":
                    szam += 2;
                    break;
                case "D":
                    szam += 3;
                    break;
                case "E":
                    szam += 4;
                    break;
                case "F":
                    szam += 5;
                    break;
            }
        } else {
            szam = (Integer.parseInt(ules.substring(0, 2)) - 1) * 6;
            switch (ules.substring(2, 3)) {
                case "A":
                    szam += 0;
                    break;
                case "B":
                    szam += 1;
                    break;
                case "C":
                    szam += 2;
                    break;
                case "D":
                    szam += 3;
                    break;
                case "E":
                    szam += 4;
                    break;
                case "F":
                    szam += 5;
                    break;
            }
        }
        return szam;
    }

    public boolean foglaltE(int id, String jaratId) {
        for (String hely : selectFoglaltHelyek(jaratId)) {
            if (id == ulesDekodolas(hely)) {
                return true;
            }
        }
        return false;
    }

    public List<String> selectFoglaltHelyek(String jaratId) {
        List<String> foglaltHelyek = new ArrayList<>();
        Cursor eredmeny = db.selectUlesek(jaratId);
        if (eredmeny != null && eredmeny.getCount() > 0) {
            while (eredmeny.moveToNext()) {
                foglaltHelyek.add(eredmeny.getString(0));
            }
        }
        return foglaltHelyek;
    }

    public String dateToString(int year, int month, int day) {
        String date = String.valueOf(year);
        if (month < 10) {
            date += "-0" + (month + 1);
            if (day < 10) {
                return date + "-0" + day;
            }
            return date + "-" + day;
        }
        return date + "-" + (month + 1) + "-" + day;
    }
}
