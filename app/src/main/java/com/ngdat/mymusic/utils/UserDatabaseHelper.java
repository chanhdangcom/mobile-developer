package com.ngdat.mymusic.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ngdat.mymusic.Model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyMusic.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_FULLNAME = "fullName";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FULLNAME + " TEXT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean insertUser(String fullName, String username, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FULLNAME, fullName);
        values.put(COL_USERNAME, username);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, role);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public Cursor getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE username = ? AND password = ?", new String[]{username, password});
    }

    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE username = ?", new String[]{username});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users", null);

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String fullName = c.getString(1);
                String username = c.getString(2);
                String email = c.getString(3);
                String role = c.getString(5);

                list.add(new User(id, fullName, username, email, role));
            } while (c.moveToNext());
        }

        c.close();
        return list;
    }
}
