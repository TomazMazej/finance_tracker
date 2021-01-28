package com.mazej.financetracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DB1";

    public static final String TABLE_INCOME = "income_data";
    public static final String TABLE_CATEGORY = "category_data";
    public static final String TABLE_ACCOUNTS = "accounts_data";
    public static final String TABLE_EXPENSES = "expenses_data";

    public static final String ID = "ID";
    public static final String CATEGORY_NAME = "Category";
    public static final String DESCRIPTION = "Description";
    public static final String AMOUNT = "Amount";
    public static final String DATE = "Date";
    public static final String ACCOUNT_NAME = "Account";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_INCOME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + " Category TEXT, " + " Description TEXT, " + " Amount INTEGER, " + " Date DATETIME)");
        db.execSQL("CREATE TABLE " + TABLE_EXPENSES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + " Category TEXT, " + " Description TEXT, " + " Amount INTEGER, " + " Date DATETIME)");
        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + " Category TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_ACCOUNTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + " Account TEXT, " + " Amount INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        onCreate(db);
    }

    private String getDate() { //get current date
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public boolean addIncome(String item1, String item2, String item3) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME, item1);
        contentValues.put(DESCRIPTION, item2);
        contentValues.put(AMOUNT, item3);
        contentValues.put(DATE, getDate());

        long result = db.insert(TABLE_INCOME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addExpenses(String item1, String item2, String item3) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME, item1);
        contentValues.put(DESCRIPTION, item2);
        contentValues.put(AMOUNT, item3);
        contentValues.put(DATE, getDate());

        long result = db.insert(TABLE_EXPENSES, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addCategory(String item1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME, item1);

        long result = db.insert(TABLE_CATEGORY, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addAccount(String item1, String item2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_NAME, item1);
        contentValues.put(AMOUNT, item2);

        long result = db.insert(TABLE_ACCOUNTS, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getIncome(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_INCOME, null);
        return data;
    }

    public Cursor getTodayIncome(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_INCOME + " WHERE Date = " + "'" + getDate() + "'", null);
        return data;
    }

    public Cursor getMonthIncome(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = c.getTime();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_INCOME + " WHERE Date BETWEEN " + "'" + dateFormat.format(date) + "' AND " + "'" + getDate() + "'", null);
        //System.out.println("SELECT * FROM " + TABLE_INCOME + " WHERE Date BETWEEN " + "'" + dateFormat.format(date) + "' AND " + "'" + getDate() + "'");
        return data;
    }

    public Cursor getExpenses(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES, null);
        return data;
    }

    public Cursor getTodayExpenses(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " WHERE Date = " + "'" + getDate() + "'", null);
        return data;
    }

    public Cursor getMonthExpenses(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = c.getTime();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " WHERE Date BETWEEN " + "'" + dateFormat.format(date) + "' AND " + "'" + getDate() + "'", null);
        return data;
    }

    public Cursor getCategories(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY, null);
        return data;
    }

    public Cursor getAccounts() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_ACCOUNTS, null);
        return data;
    }

    public void addToAccount(String acc_name, String amount) {
        int new_amount =  Integer.parseInt(getAccountAmount(acc_name)) + Integer.parseInt(amount);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(AMOUNT, new_amount);
        db.update(TABLE_ACCOUNTS, args, ACCOUNT_NAME + " = '" + acc_name + "'", null);
    }

    public void removeFromAccount(String acc_name, String amount) {
        int new_amount =  Integer.parseInt(getAccountAmount(acc_name)) - Integer.parseInt(amount);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(AMOUNT, new_amount);
        db.update(TABLE_ACCOUNTS, args, ACCOUNT_NAME + " = '" + acc_name + "'", null);
    }

    public String getAccountAmount(String acc_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + AMOUNT + " FROM " + TABLE_ACCOUNTS + " WHERE " + ACCOUNT_NAME + " = " + "'" + acc_name + "'", null);

        String amount = "";
        while(data.moveToNext()) {
            amount = data.getString(0);
        }
        return amount;
    }

    public boolean deleteCategory(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CATEGORY, CATEGORY_NAME + " = " + "'" + name + "'", null) > 0;
    }

    public boolean deleteIncome(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_INCOME, ID + " = " + "'" + id + "'", null) > 0;
    }

    public boolean deleteExpense(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EXPENSES, ID + " = " + "'" + id + "'", null) > 0;
    }

    public boolean deleteAccount(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ACCOUNTS, ID + " = " + "'" + id + "'", null) > 0;
    }
}
