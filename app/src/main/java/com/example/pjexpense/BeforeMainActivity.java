package com.example.pjexpense;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class BeforeMainActivity extends AppCompatActivity {
    private SQLiteDatabase mDb;
    private SQLiteHelper mSqlite;

    private String flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_main);

        mSqlite = SQLiteHelper.getInstance(this);
        mDb = mSqlite.getWritableDatabase();

        String check_status =  "SELECT * FROM table_status ORDER BY _idStatus DESC LIMIT 1";
        Cursor cursor = mDb.rawQuery(check_status, null);

        String check_status2 =  "SELECT * FROM table_status ORDER BY _idStatus ASC";
        Cursor cursor2 = mDb.rawQuery(check_status2, null);

        int _idStatus = 0;
        String inStatus = "";

        int idd = 0;
        String ssss = "";
        int i=0;
        while(cursor.moveToNext()) {
            _idStatus = cursor.getInt(0);
            inStatus = cursor.getString(1);

            Log.i(String.valueOf(_idStatus), "flagLIMIT : "+inStatus);
        }
        while(cursor2.moveToNext()) {
            idd = cursor2.getInt(0);
            ssss = cursor2.getString(1);

            i += 1;
            Log.i(String.valueOf(idd), "flagTotal :[ "+i+" ]"+ssss);
        }

        flag = inStatus;
        Log.i(String.valueOf(inStatus), "flag is : "+flag);
        cursor.close();

        if(flag.equals("") || flag.isEmpty() || flag.isEmpty() || flag.equals(null) || flag == null || inStatus == "null"){
            Intent intent = new Intent(BeforeMainActivity.this,MainActivity.class);
            startActivity(intent);
        }else if (flag.equals("mode1")){
            Intent intent = new Intent(BeforeMainActivity.this,EasyMode.class);
            startActivity(intent);
        }else if (flag.equals("mode2")){
            Intent intent = new Intent(BeforeMainActivity.this,Mode2.class);
            startActivity(intent);
        }else {
            Log.i("error", "error error flag"+flag);
        }


    }

}
