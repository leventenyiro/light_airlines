package com.leventenyiro.lightairlines;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "lightairlines.db";
    public static final String TABLE_NAME = "user";

    public static final String COL_1 = "id";
    public static final String COL_2 = "username";
    public static final String COL_3 = "email";
    public static final String COL_4 = "firstname";
    public static final String COL_5 = "lastname";
    public static final String COL_6 = "birthdate";
    public static final String COL_7 = "password";

    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL, firstname VARCHAR(255) NOT NULL, lastname VARCHAR(255) NOT NULL, birthdate DATE NOT NULL, password TEXT NOT NULL)");
        db.execSQL("CREATE TABLE airport (id INTEGER PRIMARY KEY AUTOINCREMENT, nev varchar(100) NOT NULL UNIQUE, rovidites varchar(3) NOT NULL UNIQUE)");
        db.execSQL("INSERT INTO airport (nev, rovidites) VALUES ('Budapest', 'BUD'), ('London', 'LHR'), ('Párizs', 'CDG')");
        db.execSQL("CREATE TABLE foglalas(id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                                "jarat_id INTEGER NOT NULL REFERENCES jarat ON UPDATE cascade ON DELETE restrict, \n" +
                                "user_id INTEGER NOT NULL REFERENCES user ON UPDATE cascade ON DELETE restrict,\n" +
                                "ules VARCHAR(4) NOT NULL)");
        db.execSQL("CREATE TABLE jarat(id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                                "utvonal_id INTEGER NOT NULL REFERENCES utvonal ON UPDATE cascade ON DELETE restrict,\n" +
                                "helyek_szama INTEGER NOT NULL,\n" +
                                "idopont DATETIME NOT NULL)");
        db.execSQL("INSERT INTO jarat (utvonal_id, helyek_szama, idopont) VALUES " +
                        "(1, 180, '2020-03-15 08:00:00'), " +
                        "(2, 180, '2020-03-15 14:00:00'), " +
                        "(3, 180, '2020-04-15 09:00:00'), " +
                        "(4, 180, '2020-04-15 15:00:00'), " +
                        "(1, 180, '2020-05-15 08:00:00'), " +
                        "(2, 180, '2020-05-15 14:00:00'), " +
                        "(3, 180, '2020-06-15 09:00:00'), " +
                        "(4, 180, '2020-06-15 15:00:00')");
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS airport");
        db.execSQL("DROP TABLE IF EXISTS jarat");
        db.execSQL("DROP TABLE IF EXISTS utvonal");
        db.execSQL("DROP TABLE IF EXISTS foglalas");
    }

    public boolean insert(String username, String email, String firstname, String lastname, String birthdate, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, email);
        contentValues.put(COL_4, firstname);
        contentValues.put(COL_5, lastname);
        contentValues.put(COL_6, birthdate);
        contentValues.put(COL_7, password);

        long eredmeny = db.insert(TABLE_NAME, null, contentValues);
        return eredmeny != -1;
    }

    public Cursor selectUsername(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor eredmeny = db.rawQuery("SELECT username FROM " + TABLE_NAME + " WHERE username = '" + username + "'", null);
        return eredmeny;
    }

    public Cursor selectEmail(String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor eredmeny = db.rawQuery("SELECT email FROM " + TABLE_NAME + " WHERE email = '" + email + "'", null);
        return eredmeny;
    }

    public Cursor selectLogin(String usernameEmail)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor eredmeny = db.rawQuery("SELECT * FROM "+ TABLE_NAME + " WHERE username = '" + usernameEmail + "' OR email = '" + usernameEmail + "'", null);
        return eredmeny;
    }

    public Cursor selectPassword(String usernameEmail)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor passwordEllenorzes = db.rawQuery("SELECT password FROM " + TABLE_NAME + " WHERE username = '" + usernameEmail + "' OR email = '" + usernameEmail + "'", null);
        return passwordEllenorzes;
    }

    public Cursor selectAll(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor eredmeny = db.rawQuery("SELECT username, email, firstname, lastname FROM user WHERE id = " + id, null);
        return eredmeny;
    }

    public long update (String id, String username, String email, String firstname, String lastname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, email);
        contentValues.put(COL_4, firstname);
        contentValues.put(COL_5, lastname);
        return db.update(TABLE_NAME, contentValues, COL_1 + " = " + id, null);
    }
}
