package com.example.universityproject;

import android.provider.BaseColumns;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.Contract;
import org.json.*;

public class DataBaseManager {
    private SQLiteOpenHelper dbh;
    private SQLiteDatabase db;

    public static class _questionTableNames implements BaseColumns {
        static String TABLE_NAME = "question_set";
        static String QUESTION_ROW = "questrion";
        static String OPTIONA_ROW = "optiona";
        static String OPTIONB_ROW = "optionb";
        static String OPTIONC_ROW = "optionc";
        static String OPTIOND_ROW = "optiond";
        static String OPTIONE_ROW = "optione";
        static String OPTIONF_ROW = "optionf";
    }

    // @todo Пока даже непонятно, как инициализировать. Надо нормально читать документаци на следующем этапе разработки
    public DataBaseManager() {
        String temp_name = _questionTableNames.TABLE_NAME;
//        String initString = "CREATE TABLE IF NOT EXISTS $temp_name (" +
//                "${QUESTIONSET.COLUMN_NAME_QUESTION} TEXT," +
//                "${QUESTIONSET.COLUMN_NAME_OPTIONA} TEXT," +
//                "${QUESTIONSET.COLUMN_NAME_OPTIONB} TEXT," +
//                "${QUESTIONSET.COLUMN_NAME_OPTIONC} TEXT," +
//                "${QUESTIONSET.COLUMN_NAME_OPTIOND} TEXT,";

//        db.execSQL(initString);
    }

    private void _addQuestion(JSONObject obj) {

    }
}
