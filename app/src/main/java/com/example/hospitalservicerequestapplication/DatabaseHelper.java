package com.example.hospitalservicerequestapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HospitalDB.db";
    // Increased version to 5 to force the addition of the 'role' column
    private static final int DATABASE_VERSION = 5;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. USERS TABLE - Now includes the 'role' column
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "password TEXT, " +
                "role TEXT)");

        // 2. SERVICES TABLE
        db.execSQL("CREATE TABLE services (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "s_name TEXT, " +
                "s_desc TEXT)");

        // 3. REQUESTS TABLE
        db.execSQL("CREATE TABLE requests (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "service TEXT, " +
                "ward TEXT, " +
                "notes TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS services");
        db.execSQL("DROP TABLE IF EXISTS requests");
        onCreate(db);
    }

    // ================= REGISTER WITH ROLE =================
    public boolean insertUser(String user, String pass, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", user);
        cv.put("password", pass);
        cv.put("role", role); // Saves 'Admin' or 'Patient'
        long result = db.insert("users", null, cv);
        return result != -1;
    }

    // ================= LOGIN WITH ROLE CHECK =================
    // This returns the role string so your LoginActivity knows where to go
    public String getUserRole(String user, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM users WHERE username=? AND password=?", new String[]{user, pass});

        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role; // Returns "Admin" or "Patient"
        }
        cursor.close();
        return null; // Returns null if login fails
    }

    public boolean checkUserExists(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users WHERE username=?", new String[]{user});
        boolean exists = c.getCount() > 0;
        c.close();
        return exists;
    }

    // ================= SERVICE & REQUEST METHODS =================

    public boolean addHospitalService(String name, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("s_name", name);
        cv.put("s_desc", desc);
        return db.insert("services", null, cv) != -1;
    }

    public boolean insertRequest(String service, String ward, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("service", service);
        cv.put("ward", ward);
        cv.put("notes", notes);
        return db.insert("requests", null, cv) != -1;
    }

    public Cursor getAllRequests() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM requests", null);
    }


    public boolean checkUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        int count = cursor.getCount();
        cursor.close();
        return count > 0; // Returns true if user IS in the database
    }

    public boolean deleteService(String nameToId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // This removes the row where s_name matches the one you clicked
        int result = db.delete("services", "s_name = ?", new String[]{nameToId});

        // If result > 0, it means at least one row was deleted successfully
        return result > 0;
    }
}