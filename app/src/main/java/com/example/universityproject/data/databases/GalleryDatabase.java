package com.example.universityproject.data.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class GalleryDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Images.db";
    private static final String TABLE_NAME = "user_album";
    private static final String CLEAR_COMMAND = "DELETE FROM " + TABLE_NAME;

    public GalleryDatabase(Context cnt) {
        super(cnt, DATABASE_NAME, null, 1);
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
                        "DATETIME TEXT," +
                        "LOCATION TEXT," +
                        "IMAGE_PATH TEXT" + ")";

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

    // Puts initial rows in opened and existing database
    private static void putInitialRows(SQLiteDatabase db) {
        ContentValues cv;

        for (int i = 1; i < 13; i++) {
            cv = new ContentValues();
            cv.put("DATETIME", String.format("[%d] 2020/05/17 23:15:39", i));
            cv.put("LOCATION", String.format("[%d] sgdfs45325:daf3.w", i));
            cv.put("IMAGE_PATH", String.format("/data/data/com.example.universityproject/files/album_examples/pic%d.png", i));

            db.insert(TABLE_NAME, null, cv);
        }

        // JPG using example
        cv = new ContentValues();
        cv.put("DATETIME", "[13] 2020/05/17 23:15:39");
        cv.put("LOCATION", "[13] sgdfs45325:daf3.w");
        cv.put("IMAGE_PATH", "/data/data/com.example.universityproject/files/album_examples/pic13.jpg");
        db.insert(TABLE_NAME, null, cv);

        // JPF using example
        cv = new ContentValues();
        cv.put("DATETIME", "[14] 2020/05/17 23:15:39");
        cv.put("LOCATION", "[14] sgdfs45325:daf3.w");
        cv.put("IMAGE_PATH", "/data/data/com.example.universityproject/files/album_examples/pic14.jpf");
        db.insert(TABLE_NAME, null, cv);

        // JPS using example
        cv = new ContentValues();
        cv.put("DATETIME", "[15] 2020/05/17 23:15:39");
        cv.put("LOCATION", "[15] sgdfs45325:daf3.w");
        cv.put("IMAGE_PATH", "/data/data/com.example.universityproject/files/album_examples/pic15.jps");
        db.insert(TABLE_NAME, null, cv);
    }
}
