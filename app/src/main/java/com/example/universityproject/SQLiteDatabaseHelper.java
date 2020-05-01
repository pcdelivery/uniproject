package com.example.universityproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.List;


// This class main purpose is to create and upgrade SQLite database and tables
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private List<String> tableNameList = null;
    private List<String> sqlCommand = null;
    private Context context = null;

    public SQLiteDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    private void clearAll() {
        final String COMMAND_CLEAR = "DROP TABLE IF EXISTS accounts";

        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteStatement clear = database.compileStatement(COMMAND_CLEAR);
        clear.execute();

        database.close();
    }

    /**
     * Just for debugging. Inition only!
     * @return
     */
    public SQLiteDatabaseHelper Build() {
        final String COMMAND_CREATE =
                "CREATE TABLE IF NOT EXISTS accounts (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "login TEXT," +
                "password TEXT," +
                "name TEXT," +
                "family_name TEXT" +
//                "avatar_image BLOB" +
                ")";

        this.clearAll();

        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteStatement createStatement = database.compileStatement(COMMAND_CREATE);
        createStatement.execute();

        ContentValues cv = new ArcantownAccount("Admin", "qwerty123").generateDatabaseRow();
        database.insert("accounts", null, cv);

        database.close();
        return this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        if (sqlCommand != null) {
//            for (int i = 0; i < sqlCommand.size(); ++i)
//                db.execSQL(sqlCommand.get(i));
//        }
    }

    /* When the new db version is bigger than current exist db version, this method will be invoked.
     * It always drop all tables and then call onCreate() method to create all table again.*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (tableNameList != null) {
//            for (int i = 0; i < tableNameList.size(); ++i)
//                db.execSQL("DROP TABLE IF EXISTS " + tableNameList.get(i));
//        }

        this.onCreate(db);
    }

    public String getAccountInfo() {
        SQLiteDatabase database = this.getReadableDatabase();
        String res = "";

        Cursor cursor = database.rawQuery("SELECT * FROM accounts", null);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++) {
//            if (cursor.moveToFirst()) {
                res = res.concat(
                        Integer.toString(
                                cursor.getInt(0))
                );
            res = res.concat(" ");

                res = res.concat(
                        cursor.getString(1)
                );
                res = res.concat(
                        cursor.getString(2)
                );
                res += Integer.toString(cursor.getCount());
//            }
            res += "\n";
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return res;
    }
}
