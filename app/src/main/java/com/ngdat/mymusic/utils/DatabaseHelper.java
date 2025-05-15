package com.ngdat.mymusic.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ngdat.mymusic.Model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyMusic.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng users
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_FULLNAME = "fullName";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";

    // Bảng favorites
    public static final String TABLE_FAVORITES = "favorites";
    public static final String COL_FAV_ID = "id";
    public static final String COL_SONG_ID = "song_id";
    public static final String COL_USER_ID = "user_id";
    // Bảng HISTORY
    public static final String TABLE_HISTORY = "history";
    public static final String COL_HIS_ID = "id";
    public static final String COL_HIS_SONGID = "song_id";
    public static final String COL_HIS_USERID = "user_id";
    public static final String COL_PLAYED_AT = "played_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FULLNAME + " TEXT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)";
        db.execSQL(createUsersTable);

        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COL_FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SONG_ID + " INTEGER, " +
                COL_USER_ID + " INTEGER)";
        db.execSQL(createFavoritesTable);

        String createHistoryTable = "CREATE TABLE " + TABLE_HISTORY + " (" +
                COL_HIS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_HIS_SONGID + " INTEGER, " +
                COL_HIS_USERID + " INTEGER, " +
                COL_PLAYED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createHistoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    // ==== XỬ LÝ USERS ====

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
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

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

    public boolean deleteUserById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USERS, COL_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    // ==== XỬ LÝ FAVORITES ====

    public boolean addFavorite(int userId, int songId) {
        if (isFavoriteExists(userId, songId)) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_SONG_ID, songId);

        long result = db.insert(TABLE_FAVORITES, null, values);
        return result != -1;
    }

    public boolean deleteFavorite(int userId, int songId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Corrected WHERE clause to delete from the favorites table based on user_id and song_id
        int result = db.delete(TABLE_FAVORITES, COL_USER_ID + " = ? AND " + COL_SONG_ID + " = ?", new String[]{String.valueOf(userId), String.valueOf(songId)});
        // Close the database connection
        db.close();
        return result > 0;
    }

    public boolean isFavoriteExists(int userId, int songId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FAVORITES + " WHERE " +
                COL_USER_ID + " = ? AND " + COL_SONG_ID + " = ?", new String[]{String.valueOf(userId), String.valueOf(songId)});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public List<Integer> getFavoriteSongs(int userId) {
        List<Integer> songIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_SONG_ID + " FROM " + TABLE_FAVORITES +
                " WHERE " + COL_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int songId = cursor.getInt(0);
                songIds.add(songId);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return songIds;
    }

    // ==== XỬ LÝ HISTORY ====
    public boolean addToHistory(int userId, int songId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Xoá nếu bài này đã có (để cập nhật lại thời gian)
            db.delete(TABLE_HISTORY, COL_HIS_SONGID + "=? AND " + COL_HIS_USERID + "=?",
                    new String[]{String.valueOf(songId), String.valueOf(userId)});

            ContentValues values = new ContentValues();
            values.put(COL_HIS_USERID, userId);
            values.put(COL_HIS_SONGID, songId);
            long result = db.insert(TABLE_HISTORY, null, values);  // Kiểm tra kết quả chèn vào cơ sở dữ liệu

            // Giới hạn chỉ 3 bài gần nhất
            db.execSQL("DELETE FROM " + TABLE_HISTORY +
                            " WHERE " + COL_HIS_ID + " NOT IN (" +
                            "SELECT " + COL_HIS_ID + " FROM " + TABLE_HISTORY +
                            " WHERE " + COL_HIS_USERID + " = ?" +
                            " ORDER BY " + COL_PLAYED_AT + " DESC LIMIT 5)",
                    new String[]{String.valueOf(userId)});

            // Kiểm tra kết quả của câu lệnh insert
            return result != -1;  // Nếu result == -1, tức là việc chèn vào cơ sở dữ liệu không thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Nếu có lỗi, trả về false
        }
    }


    public List<Integer> getHistorySongIds(int userId) {
        List<Integer> songIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COL_HIS_SONGID + " FROM " + TABLE_HISTORY +
                        " WHERE " + COL_HIS_USERID + " = ?" +
                        " ORDER BY " + COL_PLAYED_AT + " DESC",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            do {
                songIds.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return songIds;
    }
    // Thêm bài hát vào danh sách nghe gần đây
//    public boolean addRecentlyPlayed(int songId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("song_id", songId);
//        values.put("timestamp", System.currentTimeMillis());
//        long result = db.insert("recently_played", null, values);
//        return result != -1;
//    }

    // Kiểm tra bài hát có trong danh sách nghe gần đây không
//    public boolean isRecentlyPlayed(int songId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM recently_played WHERE song_id = ?", new String[]{String.valueOf(songId)});
//        boolean exists = cursor.getCount() > 0;
//        cursor.close();
//        return exists;
//    }


}
