package com.nyirolevente.lightairlines;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseUser extends SQLiteOpenHelper
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

    public DatabaseUser(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(255), email VARCHAR(255), firstname VARCHAR(255), lastname VARCHAR(255), birthdate DATE, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
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
        contentValues.put(COL_7, password); // javax.crypto

        long eredmeny = db.insert(TABLE_NAME, null, contentValues);
        return eredmeny == -1 ? false : true;
    }

    public Cursor selectUsername(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor eredmeny = db.rawQuery("SELECT username FROM " + TABLE_NAME + "WHERE username = '" + username + "'", null);
        Cursor eredmeny = db.rawQuery("SELECT username FROM " + TABLE_NAME + " WHERE username = '" + username + "'", null);
        return eredmeny;
    }

    public Cursor selectEmail(String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor eredmeny = db.rawQuery("SELECT email FROM " + TABLE_NAME + " WHERE email = '" + email + "'", null);
        return eredmeny;
    }

    public Cursor selectLogin(String usernameEmail, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor eredmeny = db.rawQuery("SELECT * FROM "+ TABLE_NAME + " WHERE (username = '" + usernameEmail + "' OR email = '" + usernameEmail + "') AND password = '" + password + "'", null);
        return eredmeny;
    }
}
