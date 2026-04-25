package com.example.budgettracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Budget.db";
    public static final String TABLE_NAME = "expense_table";

    public static final String USER_TABLE = "user_table";
    public static final String U_COL_1 = "ID";
    public static final String U_COL_2 = "USERNAME";
    public static final String U_COL_3 = "PASSWORD";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TITLE";
    public static final String COL_3 = "AMOUNT";
    public static final String COL_4 = "DATE";
    public static final String COL_5 = "TYPE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, AMOUNT REAL, DATE TEXT, TYPE TEXT)");
        db.execSQL("CREATE TABLE " + USER_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // ÚJ KIADÁS MENTÉSE (Ezt hívjuk meg a DashboardActivity-ben)
    public boolean insertData(String title, double amount, String date, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, title);
        contentValues.put(COL_3, amount);
        contentValues.put(COL_4, date);
        contentValues.put(COL_5, type);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
    public Cursor getSummaryByType() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT TYPE, SUM(AMOUNT) FROM " + TABLE_NAME + " GROUP BY TYPE", null);
    }

    public double getTotalAmount() {
        double total = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_3 + ") FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }
    public boolean registerUser(String user, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(U_COL_2, user);
        contentValues.put(U_COL_3, pass);
        long result = db.insert(USER_TABLE, null, contentValues);
        return result != -1;
    }
    public boolean checkUser(String user, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE USERNAME=? AND PASSWORD=?", new String[]{user, pass});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}