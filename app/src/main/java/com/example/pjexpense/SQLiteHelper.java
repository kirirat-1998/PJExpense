package com.example.pjexpense;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db_income_expense";
    private static final int VERSION = 1;
    private static SQLiteHelper sqliteHelper;

    //คอนสตรักเตอร์ต้องเป็นมีโมดิฟายเออร์เป็น private
    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //เมธอดสำหรับสร้างอินสแตนซ์ของคลาสนี้
    public static synchronized SQLiteHelper getInstance(Context c) {
        if(sqliteHelper == null) {
            sqliteHelper = new SQLiteHelper(c.getApplicationContext());
        }
        return sqliteHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =
                "CREATE TABLE IF NOT EXISTS table_income_expense(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "date INTEGER, " +
                    "month INTEGER, " +
                    "year INTEGER, " +
                    "type TEXT, " +
                    "title TEXT, " +
                    "amount TEXT)";
        db.execSQL(sql);

        String sqlt =
                "CREATE TABLE IF NOT EXISTS table_tools(" +
                        "_idt INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "namet TEXT, " +
                        "surnamet TEXT, " +
                        "emailt TEXT)";
        db.execSQL(sqlt);

        Log.d("table_tools","Create Table Successfully.");

        String sql_save =
                "CREATE TABLE IF NOT EXISTS table_saving(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "date INTEGER, " +
                        "month INTEGER, " +
                        "year INTEGER, " +
                        "type TEXT, " +
                        "title TEXT, " +
                        "amount TEXT)";
        db.execSQL(sql_save);

        String sql_status =
                "CREATE TABLE IF NOT EXISTS table_status(" +
                "_idStatus INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "inStatus TEXT)";
        db.execSQL(sql_status);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
    }

    boolean insertData(int valueX,int valueY){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("month", valueX);
        contentValues.put("amount",valueY);
        sqLiteDatabase.insert("table_income_expense",null,contentValues);
        return true;
    }
}
