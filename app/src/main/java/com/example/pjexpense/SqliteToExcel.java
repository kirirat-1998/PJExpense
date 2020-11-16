package com.example.pjexpense;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.ajts.androidmads.library.SQLiteToExcel;

import java.io.File;

public class SqliteToExcel extends AppCompatActivity {

    EditText edtUser, edtContactNo;
    Button btnSaveUser, btnExport, btnExportExclude;
    ListView lvUsers;
    CustomAdapter lvUserAdapter;
    //List<Users> usersList = new ArrayList<>();

    SQLiteHelper dbHelper;
    DBQueries dbQueries;
    String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Backup/";
    SQLiteToExcel sqliteToExcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_to_excel);
        //initViews();

        btnExport = findViewById(R.id.btn_export);
        File file = new File(directory_path);
        if (!file.exists()) {
            Log.v("File Created", String.valueOf(file.mkdirs()));
        }

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Export SQLite DB as EXCEL FILE
                sqliteToExcel = new SQLiteToExcel(getApplicationContext(), SQLiteHelper.DATABASE_NAME, directory_path);
                sqliteToExcel.exportAllTables("users.xls", new SQLiteToExcel.ExportListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted(String filePath) {
                        //Utils.
                    }

                    @Override
                    public void onError(Exception e) {

                    }

//                    @Override
//                    public void onError(Exception e) {
//                        Utils.showSnackBar(view, e.getMessage());
//                    }
                });
            }
        });
    }

//    boolean validate(EditText editText) {
//        if (editText.getText().toString().length() == 0) {
//            editText.setError("Field Required");
//            editText.requestFocus();
//        }
//        return editText.getText().toString().length() > 0;
//    }

//    void initViews() {
//        assert getSupportActionBar() != null;
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        dbHelper = new SQLiteHelper(getApplicationContext());
//        dbQueries = new DBQueries(getApplicationContext());
//
//        edtUser = (EditText) findViewById(R.id.edt_user);
//        edtContactNo = (EditText) findViewById(R.id.edt_c_no);
//        btnSaveUser = (Button) findViewById(R.id.btn_save_user);
//        btnExport = (Button) findViewById(R.id.btn_export);
//        btnExportExclude = (Button) findViewById(R.id.btn_export_with_exclude);
//        lvUsers = (ListView) findViewById(R.id.lv_users);
//
//        dbQueries.open();
//        usersList = dbQueries.readUsers();
//        lvUserAdapter = new CustomAdapter(getApplicationContext(), usersList);
//        lvUsers.setAdapter(lvUserAdapter);
//        dbQueries.close();
//    }



}
