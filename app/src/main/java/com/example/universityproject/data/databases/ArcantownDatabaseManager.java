package com.example.universityproject.data.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.Statement;


// @TODO: ne dodelal eshe
public class ArcantownDatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data.db";

    private static final String[] mTables =
            {"accounts",
            "gallery",
            "local_places",
            "questions"};

    public ArcantownDatabaseManager(Context cnt) {
        super(cnt, DATABASE_NAME, null, 1);
    }

    // Frees table from any data (table still exists within database though)
    public void clearDatabase(SQLiteDatabase database) {
        for (String tableName : mTables)
            database.execSQL("DELETE FROM " + tableName);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String create =
                    "CREATE TABLE IF NOT EXISTS " + "accounts" + "(" +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            // @todo: social media id to connect with friends
                            "LOGIN TEXT," +
                            "PASSWORD TEXT," +
                            "NAME TEXT," +
                            "FAMILY_NAME TEXT" + ")";

            db.execSQL(create);

        create =
                "CREATE TABLE IF NOT EXISTS " + "gallery" + "(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "DATETIME TEXT," +
                        "LOCATION TEXT," +
                        "IMAGE_PATH TEXT" + ")";

        db.execSQL(create);

        create =
                "CREATE TABLE IF NOT EXISTS " + "local_places" + "(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "NAME TEXT," +
                        "LOCATION_LAT REAL," +
                        "LOCATION_LONG REAL," +
//                        "LOCATION_NE REAL," +
//                        "LOCATION_SW REAL," +
                        "LOCATION_OPEN_TIME TEXT," +
                        "LOCATION_CLOSE_TIME TEXT," +
                        "PHOTOS TEXT," +
                        "TYPES TEXT," +
                        "STATUS TEXT," +
                        "RATING REAL" + ")";

        db.execSQL(create);

        create =
                "CREATE TABLE IF NOT EXISTS " + "questions" + "(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "QUESTION TEXT," +
                        "QUESTION_PIC_URI TEXT," +
                        "ANSWER1 TEXT," +
                        "ANSWER2 TEXT," +
                        "ANSWER3 TEXT," +
                        "ANSWER4 TEXT," +
                        "CORRECT_ANSWER INTEGER" + ")";

        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String tableName: mTables) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
        }

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String tableName: mTables) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
        }

        onCreate(db);
    }
}
