package com.leventenyiro.lightairlines.segedOsztaly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    public Database(Context context) {
        super(context, "lightairlines.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(255) NOT NULL, " +
                "email VARCHAR(255) NOT NULL, " +
                "firstname VARCHAR(255) NOT NULL, " +
                "lastname VARCHAR(255) NOT NULL, " +
                "birthdate DATE NOT NULL, " +
                "password TEXT NOT NULL)");
        db.execSQL("INSERT INTO user (username, email, firstname, lastname, birthdate, password) VALUES('LightAirlinesAdmin', 'admin@lightairlines.com', '', '', '', 'NQwJ7ZLUIs5aoopXUAHf12cUfLP3ySANJK860KIKWXg=;AlSqfhSZ647ITO94aghq58tt9k8ucI')");
        db.execSQL("CREATE TABLE airport (id INTEGER PRIMARY KEY AUTOINCREMENT, nev varchar(100) NOT NULL UNIQUE, rovidites varchar(3) NOT NULL UNIQUE)");
        db.execSQL("INSERT INTO airport (nev, rovidites) VALUES ('Budapest', 'BUD'), ('London', 'LHR'), ('Paris', 'CDG')");
        db.execSQL("CREATE TABLE foglalas(id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "jarat_id INTEGER NOT NULL REFERENCES jarat ON UPDATE cascade ON DELETE restrict, \n" +
                "user_id INTEGER NOT NULL REFERENCES user ON UPDATE cascade ON DELETE restrict,\n" +
                "ules VARCHAR(3) NOT NULL)");
        db.execSQL("CREATE TABLE jarat(id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "utvonal_id INTEGER NOT NULL REFERENCES utvonal ON UPDATE cascade ON DELETE restrict,\n" +
                "helyek_szama INTEGER NOT NULL,\n" +
                "idopont DATETIME NOT NULL)");
        db.execSQL("INSERT INTO jarat (utvonal_id, helyek_szama, idopont) VALUES " +
                "(1, 120, '2020-03-15 08:00:00'), " +
                "(2, 120, '2020-03-15 14:00:00'), " +
                "(3, 120, '2020-04-15 09:00:00'), " +
                "(4, 120, '2020-04-15 15:00:00'), " +
                "(1, 120, '2020-05-15 08:00:00'), " +
                "(2, 120, '2020-05-15 14:00:00'), " +
                "(3, 120, '2020-06-15 09:00:00'), " +
                "(4, 120, '2020-06-15 15:00:00')");
        db.execSQL("CREATE TABLE utvonal(id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "indulas_id INTEGER NOT NULL REFERENCES airport ON UPDATE cascade ON DELETE restrict,\n" +
                "celallomas_id INTEGER NOT NULL REFERENCES airport ON UPDATE cascade ON DELETE restrict,\n" +
                "idotartam time NOT NULL);");
        db.execSQL("INSERT INTO utvonal (indulas_id, celallomas_id, idotartam) VALUES " +
                "(1, 2, '02:45:00'), " +
                "(2, 1, '02:45:00'), " +
                "(1, 3, '02:25:00'), " +
                "(3, 1, '02:25:00')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS airport");
        db.execSQL("DROP TABLE IF EXISTS jarat");
        db.execSQL("DROP TABLE IF EXISTS utvonal");
        db.execSQL("DROP TABLE IF EXISTS foglalas");
    }

    public boolean insertUser(String username, String email, String firstname, String lastname, String birthdate, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("firstname", firstname);
        contentValues.put("lastname", lastname);
        contentValues.put("birthdate", birthdate);
        contentValues.put("password", password);
        long eredmeny = db.insert("user", null, contentValues);
        return eredmeny != -1;
    }

    public Cursor selectUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT username FROM user WHERE username = '" + username + "'", null);
    }

    public Cursor selectEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT email FROM user WHERE email = '" + email + "'", null);
    }

    public Cursor selectLogin(String usernameEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM user WHERE username = '" + usernameEmail + "' OR email = '" + usernameEmail + "'", null);
    }

    public Cursor selectUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT username, email, firstname, lastname FROM user WHERE id = " + id, null);
    }

    public boolean updateUser(String id, String username, String email, String firstname, String lastname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("firstname", firstname);
        contentValues.put("lastname", lastname);
        long eredmeny = db.update("user", contentValues, "id = " + id, null);
        return eredmeny == 1;
    }

    public Cursor selectPasswordById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT password FROM user WHERE id = " + id, null);
    }

    public boolean updatePassword(String id, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        long eredmeny = db.update("user", contentValues, "id = " + id, null);
        return eredmeny == 1;
    }

    public Cursor selectJaratok(String honnan, String hova, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor szam = db.rawQuery("SELECT f.id FROM foglalas f WHERE user_id = " + userId, null);
        if (szam.getCount() > 0) {
            return db.rawQuery("SELECT j.id, j.helyek_szama - (SELECT COUNT(*) FROM foglalas WHERE jarat_id = j.id), j.idopont, ai.nev, ac.nev, u.idotartam FROM jarat j\n" +
                    "INNER JOIN utvonal u ON j.utvonal_id = u.id\n" +
                    "INNER JOIN airport ai ON u.indulas_id = ai.id\n" +
                    "INNER JOIN airport ac ON u.celallomas_id = ac.id\n" +
                    "WHERE (ai.nev LIKE '%" + honnan.trim() + "%' OR ai.rovidites LIKE '%" + honnan.trim() + "%') AND (ac.nev LIKE '%" + hova.trim() + "%' OR ac.rovidites LIKE '%" + hova.trim() + "%') " +
                    "AND j.idopont > datetime('now') " +
                    "AND j.helyek_szama - (SELECT COUNT(*) FROM foglalas f WHERE f.jarat_id = j.id) > 0 " +
                    "AND j.id NOT IN (SELECT f.jarat_id FROM foglalas f WHERE user_id = " + userId + ") " +
                    "ORDER BY j.idopont", null);
        } else {
            return db.rawQuery("SELECT j.id, j.helyek_szama - (SELECT COUNT(*) FROM foglalas WHERE jarat_id = j.id), j.idopont, ai.nev, ac.nev, u.idotartam FROM jarat j\n" +
                    "INNER JOIN utvonal u ON j.utvonal_id = u.id\n" +
                    "INNER JOIN airport ai ON u.indulas_id = ai.id\n" +
                    "INNER JOIN airport ac ON u.celallomas_id = ac.id\n" +
                    "WHERE (ai.nev LIKE '%" + honnan + "%' OR ai.rovidites LIKE '%" + honnan + "%') AND (ac.nev LIKE '%" + hova + "%' OR ac.rovidites LIKE '%" + hova + "%') " +
                    "AND j.idopont > datetime('now') " +
                    "AND j.helyek_szama - (SELECT COUNT(*) FROM foglalas f WHERE f.jarat_id = j.id) > 0 " +
                    "ORDER BY j.idopont", null);
        }
    }

    public Cursor selectJarat(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT j.helyek_szama - (SELECT COUNT(*) FROM foglalas WHERE jarat_id = " + id + "), j.idopont, ai.nev, ai.rovidites, ac.nev, ac.rovidites, u.idotartam FROM jarat j\n" +
                "INNER JOIN utvonal u ON j.utvonal_id = u.id\n" +
                "INNER JOIN airport ai ON u.indulas_id = ai.id\n" +
                "INNER JOIN airport ac ON u.celallomas_id = ac.id\n" +
                "WHERE j.id = " + id, null);
    }

    public boolean insertFoglalas(String jaratId, String userId, String ules) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("jarat_id", jaratId);
        contentValues.put("user_id", userId);
        contentValues.put("ules", ules);
        long eredmeny = db.insert("foglalas", null, contentValues);
        return eredmeny != -1;
    }

    public Cursor selectUlesek(String jaratId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT ules FROM `foglalas` WHERE jarat_id = " + jaratId, null);
    }

    public Cursor selectJegyek(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT f.id, f.ules, j.idopont, ai.nev, ac.nev, u.idotartam\n" +
                "FROM foglalas f \n" +
                "INNER JOIN jarat j ON f.jarat_id = j.id\n" +
                "INNER JOIN utvonal u ON j.utvonal_id = u.id\n" +
                "INNER JOIN airport ai ON u.indulas_id = ai.id\n" +
                "INNER JOIN airport ac ON u.celallomas_id = ac.id\n" +
                "WHERE f.user_id = " + userId + "\n" +
                "AND j.idopont > datetime('now')\n" +
                "ORDER BY j.idopont", null);
    }

    public Cursor selectJegy(String foglalasId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT f.ules, j.idopont, ai.nev, ai.rovidites, ac.nev, ac.rovidites, u.idotartam\n" +
                "FROM foglalas f \n" +
                "INNER JOIN jarat j ON f.jarat_id = j.id\n" +
                "INNER JOIN utvonal u ON j.utvonal_id = u.id\n" +
                "INNER JOIN airport ai ON u.indulas_id = ai.id\n" +
                "INNER JOIN airport ac ON u.celallomas_id = ac.id\n" +
                "WHERE f.id = " + foglalasId, null);
    }

    public boolean deleteJegy(String foglalasId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long eredmeny = db.delete("foglalas", "id = ?", new String[]{foglalasId});
        return eredmeny == 1;
    }

    public Cursor selectUtvonalak(String honnan, String hova) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT u.id, ai.nev, ai.rovidites, ac.nev, ac.rovidites, u.idotartam\n" +
                "FROM utvonal u\n" +
                "INNER JOIN airport ai ON u.indulas_id = ai.id\n" +
                "INNER JOIN airport ac ON u.celallomas_id = ac.id\n" +
                "WHERE (ai.nev LIKE '%" + honnan + "%' OR ai.rovidites LIKE '%" + honnan + "%') AND (ac.nev LIKE '%" + hova + "%' OR ac.rovidites LIKE '%" + hova + "%')", null);
    }

    public Cursor selectUlesInfo(String jaratId, String ules) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT u.username, u.email, u.firstname, u.lastname\n" +
                "FROM user u\n" +
                "INNER JOIN foglalas f ON u.id = f.user_id\n" +
                "WHERE f.jarat_id = " + jaratId + " AND f.ules = '" + ules + "'", null);
    }

    public Cursor selectUtvonal(String utvonalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT ai.nev, ai.rovidites, ac.nev, ac.rovidites\n" +
                "FROM utvonal u\n" +
                "INNER JOIN airport ai ON u.indulas_id = ai.id\n" +
                "INNER JOIN airport ac ON u.celallomas_id = ac.id\n" +
                "WHERE u.id = " + utvonalId, null);
    }

    public boolean insertJarat(String utvonalId, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("utvonal_id", utvonalId);
        contentValues.put("helyek_szama", 120);
        contentValues.put("idopont", date + " " + time);
        long eredmeny = db.insert("jarat", null, contentValues);
        return eredmeny != -1;
    }
}
