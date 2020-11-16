package com.example.pjexpense;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Mode2Tools extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private SQLiteDatabase mDb;
    private SQLiteHelper mSqlite;

    EditText e_name;
    EditText surname;
    EditText email;

    private String mname;
    private String msurname;
    private String memail;

    private String m_id = "";

    TextView nameView,lastView,emailView;

    private String myName,myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode2_tools);
        Toolbar toolbar = findViewById(R.id.toolbar_tools);
        setSupportActionBar(toolbar);

        show_profile();

        mSqlite = SQLiteHelper.getInstance(this);
        mDb = mSqlite.getWritableDatabase();

        DrawerLayout drawer = findViewById(R.id.drawer_layout_tools);
        NavigationView navigationView = findViewById(R.id.nav_view_tools);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nameSurname);
        navUsername.setText(myName);

        TextView navEmail = (TextView) headerView.findViewById(R.id.nameEmail);
        navEmail.setText(myEmail);

//        Button export_excel = findViewById(R.id.S_dataB2);
//        export_excel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent b1 = new Intent(Mode2Tools.this,SqliteToExcel.class);
//                startActivity(b1);
//            }
//        });

        e_name = findViewById(R.id.editF_Name);
        surname = findViewById(R.id.Last_Name);
        email = findViewById(R.id.Email);

        findViewById(R.id.save_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readDataFromView();
            }
        });
//        set_profile_name();
        navEmail.setVisibility(View.GONE);

    }

    private void readDataFromView() {
        String errMsg = "";
        if(e_name.getText().toString().isEmpty()) {
            errMsg = "กรุณากำหนดชื่อ";
        }
        else if(surname.getText().toString().isEmpty()) {
            errMsg = "กรุณากำหนดนามสกุล";
        }
        else if(email.getText().toString().isEmpty()) {
            errMsg = "กรุณากำหนด e-mail";
        }

        if(!errMsg.isEmpty()) {
            showToast(errMsg);
            return;
        }

        mname = e_name.getText().toString();
        msurname = surname.getText().toString();
        memail = email.getText().toString();



        //เราต้องการให้จำนวนเงินเป็นเลขจำนวนเต็มเท่านั้น
        //แต่ผู้ใช้อาจใส่่ค่าที่มีทศนิยม จึงต้องแปลงเป็น double ก่อนแล้วค่อยแปลงเป็น int

        saveData();
    }

    private void saveData() {
//        mSqlite = SQLiteHelper.getInstance(this);
//        mDb = mSqlite.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("namet", mname);
        cv.put("surnamet", msurname);
        cv.put("emailt", memail);

        Log.i(String.valueOf(mname), "checkProfile: "+mname);
        Log.i(String.valueOf(msurname), "checkProfile2: "+msurname);
        Log.i(String.valueOf(memail), "checkProfile3: "+memail);

        //ถ้าเป็นกรณีการเปิดแอคทิวิตี้เพื่อแก้ไข ที่เมธอด checkForUpdate()
        //จะเก็บค่า id ของแถวนั้นไว้ในตัวแปร m_id แล้ว
        //แต่ถ้าตัวแปร m_id ไม่มีค่าใดๆ  แสดงว่าเป็นการเพิ่มข้อมูลใหม่
        if(m_id.equals("")) {
            mDb.insert("table_tools", null, cv);
            Log.i(String.valueOf(mname), "checkProfile: "+mname);
        } else {
            Log.i(String.valueOf(memail), "checkProfile3: "+memail);
        }
//        else {
//            mDb.update("table_tools", cv, "_idt = ?", new String[] {m_id});
//        }

        show_profile();
        showToast("บันทึกข้อมูลแล้ว");

        //หลังการบันทึกเสร็จ ให้ล้างข้อมูลออกไป
        onBackPressed();
    }

    private void showToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_tools);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(Mode2Tools.this,Mode2.class));
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.mode2, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {

            case R.id.nav_home2:
                Intent home2 = new Intent(Mode2Tools.this, Mode2.class);
                startActivity(home2);
                break;
//            case R.id.nav_account2:
//                Intent account2 = new Intent(Mode2Tools.this, Mode2Account.class);
//                startActivity(account2);
//                break;
//            case R.id.nav_category2:
//                Intent category2 = new Intent(Mode2Tools.this, Mode2Category.class);
//                startActivity(category2);
//                break;
            case R.id.nav_trend2:
                Intent trend2 = new Intent(Mode2Tools.this, Mode2Trend.class);
                startActivity(trend2);
                break;
            case R.id.nav_saving2:
                Intent saving2 = new Intent(Mode2Tools.this, Mode2Saving.class);
                startActivity(saving2);
                break;
            case R.id.nav_tools2:
                Intent tools2 = new Intent(Mode2Tools.this, Mode2Tools.class);
                startActivity(tools2);
                break;
//            case R.id.nav_help2:
//                Intent help2 = new Intent(Mode2Tools.this, Mode2Help.class);
//                startActivity(help2);
//                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_tools);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void exportToExcel(View view){
        mSqlite = SQLiteHelper.getInstance(this);
        mDb = mSqlite.getWritableDatabase();

        //อ่านข้อมูลจากตารางโดยเอาเฉพาะรายการที่ตรงกับเดือนและปีที่ระบุ
        //และเรียงตามวันที่จากน้อยไปมาก
        String sql =
                "SELECT * FROM table_income_expense ORDER BY year, month, date ASC";
        Cursor cursor = mDb.rawQuery(sql, null);

        //_id(0), date(1), month(2), year(3), type(4), title(5), amount(6)
        int date = 0;
        int month = 0;
        int year = 0;
        String type = "";
        String title = "";
        int amount = 0;
        int total_income = 0;
        int total_expense = 0;
        int _id = 0;
        float tototal = 0;
        float income = 0;
        float expense = 0;
        ArrayList fileExport = new ArrayList();

        StringBuilder d1 = new StringBuilder();
        StringBuilder d2 = new StringBuilder();
        StringBuilder d3 = new StringBuilder();
        StringBuilder li = new StringBuilder();
        StringBuilder in = new StringBuilder();
        StringBuilder ex = new StringBuilder();
        StringBuilder to = new StringBuilder();

        StringBuilder data = new StringBuilder();
        data.append("วันที่/เดือน/ปี,รายการ,รายรับ,รายจ่าย,คงเหลือ");
//        for(int i = 0; i<d1.length(); i++){
//            data.append("\n"+d1.toString());
//        }

        while(cursor.moveToNext()) {
            _id = cursor.getInt(0);
            date = cursor.getInt(1);
            month =cursor.getInt(2);
            year =cursor.getInt(3);
            type = cursor.getString(4);
            title = cursor.getString(5);
            amount = cursor.getInt(6);
            if(type.equals("รายรับ")) {
                income = amount;
            } else if(type.equals("รายจ่าย")) {
                expense = amount;
            }
            tototal = (tototal+income) - expense;

            data.append("\n"+date+"/"+month+"/"+year+","+title+","+income+","+expense+","+tototal);
            expense = 0;
            income = 0;
            Log.d("datadata","data");
            Log.i(String.valueOf(data), "exportToExcel333: "+data);
//            d1.append(date+",");
//            d2.append(month);
//            d3.append(year);
//            li.append(title);
//            in.append(total_income);
//            ex.append(total_expense);
//            to.append(tototal);
////
//            fileExport.add(new fileExport(_id,date, month,year,title,total_income,total_expense,tototal));
        }
        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.pjexpense.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        cursor.close();
    }

    public void show_profile() {

        mSqlite = SQLiteHelper.getInstance(this);
        mDb = mSqlite.getWritableDatabase();

        //อ่านข้อมูลจากตารางโดยเอาเฉพาะรายการที่ตรงกับเดือนและปีที่ระบุ
        //และเรียงตามวันที่จากน้อยไปมาก
        String sql =  "SELECT * FROM table_tools ORDER BY _idt DESC LIMIT 1";
        Cursor cursor = mDb.rawQuery(sql, null);

        int _idt = 0;
        String namet = "";
        String surnamet = "";
        String emailt = "";

        ArrayList itemsPro = new ArrayList();

        while(cursor.moveToNext()) {
            _idt = cursor.getInt(0);
            namet = cursor.getString(1);
            surnamet = cursor.getString(2);
            emailt = cursor.getString(3);

            itemsPro.add(new dbProfile(_idt,namet,surnamet,emailt));
        }
        myName = namet+"   "+surnamet;
        myEmail = emailt;

        Log.i(String.valueOf(_idt), "checkProfile111111: "+namet);
        Log.i(String.valueOf(_idt), "checkProfile111111: "+surnamet);
        Log.i(String.valueOf(_idt), "checkProfile111111: "+emailt);

        cursor.close();

    }
}
