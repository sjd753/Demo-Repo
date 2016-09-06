package com.ogma.demo.database;

/**
 * Created by alokdas on 07/09/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.ogma.demo.bean.User;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "demo";

    // Table names
    private static final String TABLE_USER = "user_table";

    // Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";


    // Create table queries
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER
            + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_PHONE + " TEXT,"
            + KEY_PASSWORD + " TEXT," + ")";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new ticket
    public void addUser(String name, String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PHONE, phone);
        values.put(KEY_PASSWORD, password);

        // Inserting Row
        db.insert(TABLE_USER, null, values);

        db.close(); // Closing database connection
    }


    // Get single user
    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, //table
                new String[]{KEY_NAME, KEY_EMAIL, KEY_PHONE, KEY_PASSWORD}, //selection columns
                KEY_ID + "=?", //selection
                new String[]{String.valueOf(id)}, //selection arguments
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            User user = new User(String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_ID))),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(KEY_PHONE)),
                    cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
            return user;
        } else {
            throw new SQLiteException("No users found for id = " + id);
        }
    }


    // Getting All users
    public ArrayList<User> getAllUsers() {
        ArrayList<User> values = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                values.add(new User(String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_ID))),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(KEY_PHONE)),
                        cursor.getString(cursor.getColumnIndex(KEY_PASSWORD))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return values;
    }


    // Updating single user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_PASSWORD, user.getPassword());

        // updating row
        return db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }


    // Deleting single user
    public void deleteEventPayment(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
