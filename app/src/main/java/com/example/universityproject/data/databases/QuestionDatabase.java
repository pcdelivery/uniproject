package com.example.universityproject.data.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class QuestionDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Questions.db";
    private static final String TABLE_NAME = "question_table";
    private static final String CLEAR_COMMAND = "DELETE FROM " + TABLE_NAME;

    public QuestionDatabase(Context cnt) {
        super(cnt, DATABASE_NAME, null, 2);
        SQLiteDatabase database = this.getWritableDatabase();


        // Frees table from any data (table still exists within database though)
        database.execSQL(CLEAR_COMMAND);

        // Inserts first test row
        database.insert(TABLE_NAME, null, getInitialRow());

        database.close();
    }

    // Called when the database is created for the first time.
    // This is where the creation of tables and the initial population of the tables should happen.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void execute(String query) {
        SQLiteDatabase wdb = this.getWritableDatabase();
        SQLiteStatement statement = wdb.compileStatement(query);
        statement.execute();
        wdb.close();
    }

    private static ContentValues getInitialRow() {
        ContentValues cv = new ContentValues();
        cv.put("QUESTION", "QuestionQ");
        cv.put("QUESTION_PIC_URI", "/data/data/com.example.universityproject/files/que_1.jpg");
        cv.put("ANSWER1", "AnswerA");
        cv.put("ANSWER2", "AnswerB");
        cv.put("ANSWER3", "AnswerC");
        cv.put("ANSWER4", "AnswerD");
        cv.put("CORRECT_ANSWER", 1);

        return cv;
    }
}
