package com.example.pjexpense;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Mode2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private SQLiteHelper mSqlite;
    private SQLiteDatabase mDb;
    private Spinner mSpinnerMonth;
    private Spinner mSpinnerYear;
    static String[] words;
    private String[] mThaiMonths = {
            "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน",
            "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"
    };
    private String myEmail,myName;

    private int currentYear;
    private int yearSelected;
    private String monthText;
    private int monthSelected;
    private TextView textView;
    private CheckBox shortMonthsCheckBox;

    private String textget;
    private int monthNumber=0;
    private int yearNumber=0;

    private String mflag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode2);

        Toolbar toolbar = findViewById(R.id.toolbar_mode2On);
        setSupportActionBar(toolbar);

//        mSqlite = SQLiteHelper.getInstance(this);
//        mDb = mSqlite.getWritableDatabase();
//
//        mflag = getIntent().getStringExtra("inStatus");
//        ContentValues cv_status = new ContentValues();
//        cv_status.put("inStatus", mflag);
//        mDb.insert("table_status", null, cv_status);

        show_profile();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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

        Button b1 = findViewById(R.id.btn_income2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b1 = new Intent(Mode2.this,InputIncomeMode2.class);
                startActivity(b1);
            }
        });
        Button b2 = findViewById(R.id.btn_expense2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b2 = new Intent(Mode2.this,InputExpenseMode2.class);
                startActivity(b2);
            }
        });

        //ใช้คลาส Calendar เพื่ออ่านค่าเดือนและปีปัจจุบัน
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR) + 543;   //แปลงเป็น พ.ศ.

        //สร้างรายชื่อเดือนเติมลงใน Spinner อันแรก
        mSpinnerMonth = (Spinner) findViewById(R.id.spinner_month);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, mThaiMonths);
        mSpinnerMonth.setAdapter(adapter);
        mSpinnerMonth.setSelection(currentMonth);

        //สร้างตัวเลือกแค่ 3 ปีล่าสุด เติมลงใน Spinner
        ArrayList years = new ArrayList();
        for(int i = 0; i < 3; i++) {
            years.add(String.valueOf(currentYear - i));
        }
        mSpinnerYear = (Spinner) findViewById(R.id.spinner_year);
        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, years);
        mSpinnerYear.setAdapter(adapter);
        mSpinnerYear.setSelection(0);

        //ดักอีเวนต์การเลือกรายการที่ Spinner แต่ละอัน เพื่อให้นำรายการที่ถูกเลือก
        //ไปเป็นเงื่อนไขในการอ่านข้อมูลจากตารางแล้วนำมาแสดงผลใหม่ทันที
        mSpinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createItem();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        mSpinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createItem();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        mSpinnerMonth.setVisibility(View.GONE);
        mSpinnerYear.setVisibility(View.GONE);
        navEmail.setVisibility(View.GONE);

//        show_profile();
        setUI();
        createItem();
        setSpinnerDropdownHeight();   //ปรับแต่ง dropdown ของ Spinner
    }

    private void createItem() {
        mSqlite = SQLiteHelper.getInstance(this);
        mDb = mSqlite.getWritableDatabase();

        //อ่านข้อมูลจากตารางโดยเอาเฉพาะรายการที่ตรงกับเดือนและปีที่ระบุ
        //และเรียงตามวันที่จากน้อยไปมาก
        String sql =
                "SELECT * FROM table_income_expense " +
                        "WHERE month = ? AND year = ?  " +
                        "ORDER BY date DESC";

        //อ่านค่าเดือนและปีที่ถูกเลือกจาก Spinner เพื่อเติมลงในเงื่อนไขของ SQL
//        int m = mSpinnerMonth.getSelectedItemPosition() + 1;
//        int y = Integer.valueOf(mSpinnerYear.getSelectedItem().toString()) - 543;
        int m = monthNumber;
        int y = yearNumber - 543;

        String[] args = {m + "", y + ""};
        Cursor cursor = mDb.rawQuery(sql, args);

        //_id(0), date(1), month(2), year(3), type(4), title(5), amount(6)
        int date = 0;
        String dateMonth = "";
        String type = "";
        int drawable = 0;
        String title = "";
        int amount = 0;
        int lastDate = 0;
        int total_income = 0;
        int total_expense = 0;
        int _id = 0;
        ArrayList items = new ArrayList();

        while(cursor.moveToNext()) {
            _id = cursor.getInt(0);
            date = cursor.getInt(1);
            if(date != lastDate) {
                if(cursor.getInt(1) < 10) {
                    dateMonth = "0";
                } else {
                    dateMonth = "";
                }
                dateMonth += cursor.getInt(1) + "  " + mThaiMonths[cursor.getInt(2) - 1];
                items.add(new SectionItem(dateMonth));
                lastDate = date;
            }

            type = cursor.getString(4);
            title = cursor.getString(5);
            amount = cursor.getInt(6);
            if(type.equals("รายรับ")) {
                drawable = R.drawable.ic_plus_circle;
                total_income += amount;
            } else if(type.equals("รายจ่าย")) {
                drawable = R.drawable.ic_minus_circle;
                total_expense += amount;
            }

            items.add(new ChildItem(_id, drawable, title, amount));
        }

        CustomAdapter adapter = new CustomAdapter(this, items);
        RecyclerView rcv = (RecyclerView) findViewById(R.id.recyclerView);
        rcv.setAdapter(adapter);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        //ต่อไปเป็นการแสดงรายรับ-รายรวมทั้งเดือนและจำนวนคงเหลือที่แถบด้านล่าง
        String ic = NumberFormat.getIntegerInstance().format(total_income);
        TextView textIncome = (TextView) findViewById(R.id.text_income);
        textIncome.setText(ic);

        String ex = NumberFormat.getIntegerInstance().format(total_expense);
        TextView textExpense = (TextView) findViewById(R.id.text_expense);
        textExpense.setText(ex);

        String ba = NumberFormat.getIntegerInstance().format(total_income - total_expense);
        TextView textBalance = (TextView) findViewById(R.id.text_balance);
        textBalance.setText(ba);

        cursor.close();

        //เมื่อคลิกที่รายการใด ให้แสดง Item Dialog ที่มีตัวเลือกลบและแก้ไข
        //ซึ่งได้สร้าง Custom Listener ไว้ในคลาส Adapter แล้ว
        adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int _id) {
                showItemDialog(_id);
            }
        });
    }

    private void showMyProfile(){
        TextView nameSurname,nameEmail;
        String FnameLname;

        mSqlite = SQLiteHelper.getInstance(this);
        mDb = mSqlite.getWritableDatabase();

        //อ่านข้อมูลจากตารางโดยเอาเฉพาะรายการที่ตรงกับเดือนและปีที่ระบุ
        //และเรียงตามวันที่จากน้อยไปมาก
        String sqlt = "SELECT * FROM table_tools ORDER BY _idt DESC LIMIT 1";
        Cursor cursorPro = mDb.rawQuery(sqlt, null);

        int _idt = 0;
        String Fname = "";
        String Lname = "";
        String email = "";

        while(cursorPro.moveToNext()) {
            _idt = cursorPro.getInt(0);
            Fname = cursorPro.getString(1);
            Lname = cursorPro.getString(2);
            email = cursorPro.getString(3);

        }
        nameSurname = findViewById(R.id.nameSurname);
        nameEmail = findViewById(R.id.nameEmail);

        nameSurname.setText(Fname+"   "+Lname);
        nameEmail.setText(email);

    }

    private void showItemDialog(final int _id) {
        FragmentManager fm = getSupportFragmentManager();
        String[] items = {"แก้ไข", "ลบ"};

        ItemDialog dialog = ItemDialog.newInstance("เลือกสิ่งที่จะทำ", items);
        dialog.show(fm, null);
        dialog.setOnFinishDialogListener(new ItemDialog.OnFinishDialogListener() {
            @Override
            public void onFinishDialog(int selectedIndex) {
                if(selectedIndex == -1) {
                    return;
                }
                //ถ้าเลือก "แก้ไข" ให้ส่งอินเทนต์พร้อมแนบ _id ของรายการ (แถว) นั้นไปยังแอคทิวิตี้รับข้อมูล
                if(selectedIndex == 0) {
                    Intent intent = new Intent(Mode2.this, InputIncomeMode2.class);
                    intent.putExtra("_id", _id + "");
                    startActivity(intent);
                } else if(selectedIndex == 1) {   //ถ้าเลือก "ลบ"
                    String sql = "DELETE FROM table_income_expense WHERE _id = " + _id;
                    mDb.execSQL(sql);
                    createItem();
                }
            }
        });
    }

    private void setSpinnerDropdownHeight() {
        mSpinnerMonth.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSpinnerMonth.setDropDownVerticalOffset(mSpinnerMonth.getHeight() + 5);
            }
        }, 500);

        mSpinnerYear.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSpinnerYear.setDropDownVerticalOffset(mSpinnerYear.getHeight() + 5);
            }
        }, 500);
    }

    private void showToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Exit");
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setCancelable(true);
                dialog.setMessage("Do you want to exit?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
//                    ContentValues cv_status = new ContentValues();
//                    cv_status.put("inStatus", mflag);
//                    mDb.insert("table_status", null, cv_status);
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
//            startActivity(new Intent(Mode2.this, MainActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mode2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
////            Mode2Tools tooll = new Mode2Tools();
////            tooll.set_profile_name();
//
//            Intent swit = new Intent(Mode2.this, EasyMode.class);
////            swit.putExtra("inStatus","mode1");
////            mSqlite = SQLiteHelper.getInstance(this);
////            mDb = mSqlite.getWritableDatabase();
//
//            ContentValues cv_status = new ContentValues();
//            cv_status.put("inStatus", "mode1");
//            mDb.insert("table_status", null, cv_status);
//            startActivity(swit);
////            break;
//            return true;
//        }

        /**************************************************************************************/
        if (id == R.id.action_settings) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("เปลี่ยนโหมด");
            dialog.setIcon(R.mipmap.ic_launcher);
            dialog.setCancelable(true);
            dialog.setMessage("ต้องการเปลี่ยนโหมดการใช้งานหรือไม่");
            dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent swit = new Intent(Mode2.this, MainActivity.class);
                    startActivity(swit);
                }
            });

            dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            dialog.show();
            return true;
        }
/**********************************************************************************/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
//        set_profile_name();
        switch (id) {

            case R.id.nav_home2:
                Intent home2 = new Intent(Mode2.this, Mode2.class);
                startActivity(home2);
                break;
//            case R.id.nav_account2:
//                Intent account2 = new Intent(Mode2.this, Mode2Account.class);
//                startActivity(account2);
//                break;
//            case R.id.nav_category2:
//                Intent category2 = new Intent(Mode2.this, Mode2Category.class);
//                startActivity(category2);
//                break;
            case R.id.nav_trend2:
                Intent trend2 = new Intent(Mode2.this, Mode2Trend.class);
                startActivity(trend2);
                break;
            case R.id.nav_saving2:
                Intent saving2 = new Intent(Mode2.this, Mode2Saving.class);
                startActivity(saving2);
                break;
            case R.id.nav_tools2:
                Intent tools2 = new Intent(Mode2.this, Mode2Tools.class);
                startActivity(tools2);
                break;
//            case R.id.nav_help2:
////                Intent help2 = new Intent(Mode2.this, Mode2Help.class);
////                startActivity(help2);
////                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "กรุณาพูดรายการ");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    words = result.get(0).split(" ");
                    if (words[0].contains("ได้") || words[0].contains("รับ")) {
                        Intent intent = new Intent(Mode2.this, InputIncomeMode2.class);
                        intent.putExtra("mm_key", words[0]);
                        intent.putExtra("mm_key1", words[1]);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(Mode2.this,InputExpenseMode2.class);
                        intent.putExtra("mm_key", words[0]);
                        intent.putExtra("mm_key1", words[1]);
                        startActivity(intent);
                    }
                    //txvResult.setText(result.get(0));
                }
                break;
        }
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


    private void setUI() {
        textView = findViewById(R.id.yearMonth);
        final CheckBox dateRangeCheckBox = findViewById(R.id.cbDateRange);
        final CheckBox CustomTitleCheckBox = findViewById(R.id.cbCustomTitle);
        shortMonthsCheckBox = findViewById(R.id.cbShortMonth);

        dateRangeCheckBox.setVisibility(View.GONE);
        CustomTitleCheckBox.setVisibility(View.GONE);
        shortMonthsCheckBox.setVisibility(View.GONE);
//        dateRangeCheckBox.setVisibility(View.VISIBLE);

        findViewById(R.id.yearMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMonthYearPickerDialogFragment(
                        dateRangeCheckBox.isChecked(),
                        CustomTitleCheckBox.isChecked()
                );
            }
        });

        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR)+543;
        yearSelected = currentYear;
        monthSelected = calendar.get(Calendar.MONTH);

        updateViews();
    }

    private MonthYearPickerDialogFragment createDialog(boolean customTitle) {
        return MonthYearPickerDialogFragment
                .getInstance(monthSelected,
                        yearSelected,
                        customTitle ? getString(R.string.custom_title).toUpperCase() : null,
                        shortMonthsCheckBox.isChecked() ? MonthFormat.SHORT : MonthFormat.LONG);
    }

    private MonthYearPickerDialogFragment createDialogWithRanges(boolean customTitle) {
        final int minYear = 2010;
        final int maxYear = currentYear;
        final int maxMoth = 11;
        final int minMoth = 0;
        final int minDay = 1;
        final int maxDay = 31;
        long minDate;
        long maxDate;

        Calendar calendar = Calendar.getInstance();

        calendar.clear();
        calendar.set(minYear, minMoth, minDay);
        minDate = calendar.getTimeInMillis();

        calendar.clear();
        calendar.set(maxYear, maxMoth, maxDay);
        maxDate = calendar.getTimeInMillis();

        return MonthYearPickerDialogFragment
                .getInstance(monthSelected,
                        yearSelected,
                        minDate,
                        maxDate,
                        customTitle ? getString(R.string.custom_title).toUpperCase() : null,
                        shortMonthsCheckBox.isChecked() ? MonthFormat.SHORT : MonthFormat.LONG);
    }

    private void displayMonthYearPickerDialogFragment(boolean withRanges,
                                                      boolean customTitle) {
        MonthYearPickerDialogFragment dialogFragment = withRanges ?
                createDialogWithRanges(customTitle) :
                createDialog(customTitle);

        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int year, int monthOfYear) {
                monthSelected = monthOfYear;
                yearSelected = year;
                updateViews();
            }
        });

        dialogFragment.show(getSupportFragmentManager(), null);
    }

    private void updateViews() {
        monthText = new DateFormatSymbols().getMonths()[monthSelected];
        textView.setText(String.format("  %s %s  ", monthText, yearSelected));
        textget = textView.getText().toString();

        String textttt[] = textget.split(" ");

        if (textttt[2].equals("มกราคม")){
            monthNumber = 1;
        }else if (textttt[2].equals("กุมภาพันธ์")){
            monthNumber = 2;
        }else if (textttt[2].equals("มีนาคม")){
            monthNumber = 3;
        }else if (textttt[2].equals("เมษายน")){
            monthNumber = 4;
        }else if (textttt[2].equals("พฤษภาคม")){
            monthNumber = 5;
        }else if (textttt[2].equals("มิถุนายน")){
            monthNumber = 6;
        }else if (textttt[2].equals("กรกฎาคม")){
            monthNumber = 7;
        }else if (textttt[2].equals("สิงหาคม")){
            monthNumber = 8;
        }else if (textttt[2].equals("กันยายน")){
            monthNumber = 9;
        }else if (textttt[2].equals("ตุลาคม")){
            monthNumber = 10;
        }else if (textttt[2].equals("พฤศจิกายน")){
            monthNumber = 11;
        }else if (textttt[2].equals("ธันวาคม")){
            monthNumber = 12;
        }else {
            Log.i(String.valueOf("error"), "error error error error: ");
        }
        yearNumber = Integer.valueOf(textttt[3]);

        Log.i(String.valueOf(monthNumber), "sfdsdfsdfds: "+yearNumber);

        Log.i(String.valueOf(textttt.length), "textget: "+textttt[0]+
                "\n 1 "+textttt[1]+
                "\n 2 "+textttt[2]+
                "\n 3 "+textttt[3]);
        Log.i(String.valueOf(textget), "textget: "+textget);

        createItem();
    }


}
