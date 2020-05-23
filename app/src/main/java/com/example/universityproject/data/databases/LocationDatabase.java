package com.example.universityproject.data.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Locations.db";
    private static final String TABLE_NAME = "places";
    private static final String CLEAR_COMMAND = "DELETE FROM " + TABLE_NAME;

    public LocationDatabase(Context cnt) {
        super(cnt, DATABASE_NAME, null, 1);

        SQLiteDatabase database = this.getWritableDatabase();

        // Frees table from any data (table still exists within database though)
        database.execSQL(CLEAR_COMMAND);

        // Fill table
//        putInitialRows(database);

        database.close();
    }

    // Called when the database is created for the first time.
    // This is where the creation of tables and the initial population of the tables should happen.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "NAME TEXT," +
                        "LOCATION_LAT REAL," +
                        "LOCATION_LONG REAL," +
                        "LOCATION_NE REAL," +
                        "LOCATION_SW REAL," +
                        "LOCATION_OPEN_TIME INTEGER," +
                        "LOCATION_CLOSE_TIME INTEGER," +
                        "PHOTOS TEXT," +
                        "TYPES TEXT," +
                        "STATUS TEXT," +
                        "RATING REAL" + ")";

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

    /*public static void putNewRow(SQLiteDatabase db, Place place) {
        ContentValues cv = place.generateDatabaseRow();

        db.insert(TABLE_NAME, null, cv);
    }

    // Puts initial rows in opened and existing database
    private static void putInitialRows(SQLiteDatabase db) {
        putNewRow(db,
                new Place(
                        "Казанский Собор",
                        59.9343346,
                        30.3229606,
                        51.42,
                        51.42,
                        1000,
                        1830,
                        "data/data",
                        "Religious, Museum, Architecture",
                        "OK",
                        4.54
                )
        );
    }*/
}
