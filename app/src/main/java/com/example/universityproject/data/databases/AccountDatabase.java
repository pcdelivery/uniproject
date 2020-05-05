package com.example.universityproject.data.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.universityproject.ArcantownAccount;

public class AccountDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Users.db";
    private static final String TABLE_NAME = "accounts";
    private static final String CLEAR_COMMAND = "DELETE FROM " + TABLE_NAME;

    public AccountDatabase(Context cnt, int mode) {
        super(cnt, DATABASE_NAME, null, 1);

        if (mode == 1)
            return;

        SQLiteDatabase database = this.getWritableDatabase();

        // Frees table from any data (table still exists within database though)
        database.execSQL(CLEAR_COMMAND);

        // Fill table
        putInitialRows(database);

        database.close();
    }

    // Called when the database is created for the first time.
    // This is where the creation of tables and the initial population of the tables should happen.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "LOGIN TEXT," +
                        "PASSWORD TEXT," +
                        "NAME TEXT," +
                        "FAMILY_NAME TEXT" + ")";

        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static void putNewRow(SQLiteDatabase db, ArcantownAccount account) {
        ContentValues cv = account.generateDatabaseRow();
        db.insert(TABLE_NAME, null, cv);
    }

    // Puts initial rows in opened and existing database
    private static void putInitialRows(SQLiteDatabase db) {
        putNewRow(db,
                new ArcantownAccount("Admin", "qwerty123", "Админ", "Админович")
        );

//        cv = new ArcantownAccount("lookspam00@gmail.com", "_google_pass").generateDatabaseRow();
//        db.insert("accounts", null, cv);
    }
}
