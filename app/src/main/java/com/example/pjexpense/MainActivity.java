package com.example.pjexpense;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button b1,b2;
    private String flag = "";
    private TextView myEmail;

    private SQLiteDatabase mDb;
    private SQLiteHelper mSqlite;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSqlite = SQLiteHelper.getInstance(this);
        mDb = mSqlite.getWritableDatabase();

        ContentValues cv = new ContentValues();

            b1=(Button)findViewById(R.id.btn_EasyMode);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,EasyMode.class);
//                    intent.putExtra("inStatus","mode1");
                    cv.put("inStatus", "mode1");
                    mDb.insert("table_status", null, cv);
                    startActivity(intent);
                }
            });

            b2=(Button)findViewById(R.id.btn_NormalMode);
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,Mode2.class);
//                    intent.putExtra("inStatus","mode2");
                    cv.put("inStatus", "mode2");
                    mDb.insert("table_status", null, cv);
                    startActivity(intent);
                }
            });

        Log.i(String.valueOf(flag), "flag mainis : "+flag);

    }

    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setCancelable(true);
        dialog.setMessage("Do you want to exit?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                System.exit(0);
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();

//        startActivity(new Intent(EasyMode.this, MainActivity.class));
    }

}
