package com.example.pjexpense;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.blackbox_vision.datetimepickeredittext.view.DatePickerEditText;

public class InputExpenseMode1 extends AppCompatActivity {
    private SQLiteDatabase mDb;
    private SQLiteHelper mSqlite;

    private DatePickerDialog mdpd;
    private DatePickerEditText mDatePicker;
    private RadioButton mRadioIncome;
    private RadioButton mRadioExpense;
    private EditText mEditTitle;
    private EditText mEditAmount;
    private String[] mItems;

    private String mType;
    private String totalDate;
    private int mDate;
    private int mMonth;
    private int mYear;
    private String mTitle;
    private int mAmount;
    private String m_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_expense_mode1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_input_expense_mode1);
        toolbar.setTitle("บันทึกรายรับ - รายจ่าย");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSqlite = SQLiteHelper.getInstance(this);
        mDb = mSqlite.getWritableDatabase();

        mRadioIncome = (RadioButton) findViewById(R.id.radio_income);
        mRadioExpense = (RadioButton) findViewById(R.id.radio_expense);

        String date_auto = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//        String date_split[];
//        date_split = date_auto.split("-");
//        if()

        mDatePicker = (DatePickerEditText) findViewById(R.id.datePickerEditText);
        mDatePicker.setManager(getSupportFragmentManager());
        String[] yyyy = date_auto.toString().split("-");
        int mDateyyy = Integer.valueOf(yyyy[0]);
        int mMonthyyy =  Integer.valueOf(yyyy[1]);
        int mYearyyy  = Integer.valueOf(yyyy[2])+543;
        mDatePicker.setText(String.valueOf(mDateyyy+"-"+mMonthyyy+"-"+mYearyyy));

        String mm0 = getIntent().getStringExtra("mm_key");
        String mm1 = getIntent().getStringExtra("mm_key1");
        mEditTitle = (EditText)findViewById(R.id.edit_title);
        mEditTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    handled = true;
                }
                return handled;
            }
        });
        mEditTitle.setText(mm0);
        mEditAmount = (EditText)findViewById(R.id.edit_amount);
        mEditAmount.setText(mm1);

        //ตรวจสอบว่าเป็นการเปิดแอคทิวิตี้เพื่อแก้ไขข้อมูลหรือไม่ โดยหากเป็นการแก้ไข จะมีค่า _id ของแถวนั้น
        //ส่งจากแอคทิวิตี้อื่นเข้ามากับอินเทนต์ ก็ให้อ่านข้อมูลจากตารางมาแสดงในวิว
        Intent intent = getIntent();
        if(intent.getStringExtra("_id") != null) {
            m_id = intent.getStringExtra("_id");
            readDataFromDb();
        }


        //เมื่อคลิกปุ่ม ... ตรงช่องรับข้อมูลวันเดือนปี ให้แสดง DatePickerEditText
        findViewById(R.id.button_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mDatePicker.onClick(mDatePicker);
                Calendar calendar = Calendar.getInstance();
                int mDmy = calendar.get(Calendar.DAY_OF_MONTH);
                int mMmy =  calendar.get(Calendar.MONTH);
                int mYmy  = calendar.get(Calendar.YEAR);
                mdpd = new DatePickerDialog(InputExpenseMode1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mmmYear, int mmmMonth, int mmmDay) {
                        mDatePicker.setText(String.valueOf(mmmDay+"-"+(mmmMonth+1)+"-"+(mmmYear+543)));
                    }
                },mYmy,mMmy,mDmy);
                mdpd.show();
            }
        });

        //เมื่อคลิกปุ่ม ... ตรงช่องรับข้อมูลชื่อรายการ ให้แสดง Item Dialog
        findViewById(R.id.button_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemDialog();
            }
        });

        //เมือคลิกปุ่ม บันทึกข้อมูล ให้อ่านค่าจากวิวต่างๆ แล้วจัดเก็บลงในตาราง
        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readDataFromView();
            }
        });
    }

    //ถ้าเป็นการเปิดแอคทิวิตี้เพื่อแก้ไข ให้อ่านข้อมูลของแถวนั้นจากตารางมาแสดงใน EditText
    private void readDataFromDb() {
        //ถ้ามีค่า _id ส่งเข้ามา ให้นำค่า _id ไปเป็นเงื่อนในการอ่านข้อมูลของแถวนั้น
        String sql = "SELECT * FROM table_income_expense WHERE _id =" + m_id;
        Cursor cursor = mDb.rawQuery(sql, null);

        //นำข้อมูลที่อ่านได้ ไปกำนดให้แก่วิวที่ใช้รับข้อมูลนั้นๆ
        if(cursor.moveToNext()) {
            if(cursor.getString(4).equals("-")) {
                mRadioExpense.setChecked(true);
            } else {
                mRadioIncome.setChecked(true);
            }

            //ถ้าเป็นเลขหลักเดียว ให้เติม 0 ข้างหน้า เพื่อให้เป็นรูปแบบเดียวกับกรณีที่เลือกจาก DatePicker
            String d = ((cursor.getInt(1) < 10) ? "0" : "") + cursor.getInt(1);
            String m = ((cursor.getInt(2) < 10) ? "0" : "") + cursor.getInt(2);
            int y = cursor.getInt(3);
            mDatePicker.setText(d + "-" + m + "-" + y);

            mEditTitle.setText(cursor.getString(5));
            mEditAmount.setText(cursor.getString(6));
        }
    }

    private void readDataFromView() {
        //ตรวจสอบว่าใส่ข้อมูลครบหรือไม่
        String errMsg = "";
        if(!mRadioIncome.isChecked() && !mRadioExpense.isChecked()) {
            errMsg = "กรุณาเลือกชนิดรายการ";
        } else if(mDatePicker.getText().toString().isEmpty()) {
            errMsg = "กรุณากำหนดวันเดือนปี";
        }
        else if(mEditTitle.getText().toString().trim().isEmpty()) {
            // errMsg = "กรุณาใส่ชื่อรายการ";
            if (mRadioExpense.isChecked()) {
                mEditTitle.setText("รายจ่าย");
            } else if (mRadioIncome.isChecked()) {
                mEditTitle.setText("รายรับ");
            }
        }
        else if(mEditAmount.getText().toString().trim().isEmpty()) {
            errMsg = "กรุณาใส่จำนวนเงิน";
        }

        if(!errMsg.isEmpty()) {
            showToast(errMsg);
            return;
        }

        if(mRadioExpense.isChecked()) {
            mType = "รายจ่าย";
        } else if(mRadioIncome.isChecked()) {
            mType = "รายรับ";

        }

        //ค่าใน DatePickerEditText จะอยู่ในรุปแบบ date-month-year
        //ให้แยกวัน เดือน ปี ออกจากกัน เพื่อเก็บไว้คนละคอลัมน์
        String[] dmy = mDatePicker.getText().toString().split("-");
        mDate = Integer.valueOf(dmy[0]);
        mMonth =  Integer.valueOf(dmy[1]);
        mYear  = Integer.valueOf(dmy[2])-543;

        mTitle = mEditTitle.getText().toString();

        String ceee = mEditAmount.getText().toString();

        double a = 0;
        if(ceee.contains(",")){
            String[] eee;
            eee = mEditAmount.getText().toString().split(",");
            Log.i("TAG", "readDataFromViewAmount: "+eee[0]+" / "+eee[1]);
            a = Double.valueOf(eee[0]+eee[1]);
        }else {
            a = Double.valueOf(mEditAmount.getText().toString());
        }

        //เราต้องการให้จำนวนเงินเป็นเลขจำนวนเต็มเท่านั้น
        //แต่ผู้ใช้อาจใส่่ค่าที่มีทศนิยม จึงต้องแปลงเป็น double ก่อนแล้วค่อยแปลงเป็น int
//        double a = Double.valueOf(eee[0]+eee[1]);
        mAmount = (int)a;

        saveData();
    }

    private void saveData() {
        //ที่เมธอด readValueFromView() เราอ่านข้อมูลจากวิวไปเก็บในตัวแปรฟิลด์แล้ว
        ContentValues cv = new ContentValues();
        cv.put("date", mDate);
        cv.put("month", mMonth);
        cv.put("year", mYear);
        cv.put("type", mType);
        cv.put("title", mTitle);
        cv.put("amount", mAmount);

        //ถ้าเป็นกรณีการเปิดแอคทิวิตี้เพื่อแก้ไข ที่เมธอด checkForUpdate()
        //จะเก็บค่า id ของแถวนั้นไว้ในตัวแปร m_id แล้ว
        //แต่ถ้าตัวแปร m_id ไม่มีค่าใดๆ  แสดงว่าเป็นการเพิ่มข้อมูลใหม่
        if(m_id.equals("")) {
            mDb.insert("table_income_expense", null, cv);
        } else {
            mDb.update("table_income_expense", cv, "_id = ?", new String[] {m_id});
        }

        showToast("บันทึกข้อมูลแล้ว");

        //หลังการบันทึกเสร็จ ให้ล้างข้อมูลออกไป
        mRadioExpense.setChecked(true);
        mDatePicker.setText("");
        mEditTitle.setText("");
        mEditAmount.setText("");
        onBackPressed();
    }

    //แสดง Item Dialog เพื่อสร้างตัวเลือก
    private void showItemDialog() {
        FragmentManager fm = getSupportFragmentManager();
        //รายการที่เป็นรายรับ ให้ใส่เพิ่มเติมตามต้องการ
        String[] items_income = {
                "เงินเดือน", "ยอดขาย", "โบนัส", "รายได้พิเศษ", "เงินดอกเบี้ย", "ของขวัญ"
        };
        //รายการที่เป็นรายจ่ายๆ ให้ใส่เพิ่มเติมตามต้องการ
        String[] items_expense = {
                "อาหาร", "เครื่องดื่ม", "ช้อปปิ้ง", "การเดินทาง", "การลงทุน", "การศึกษา", "ค่าน้ำมัน", "เติมเงินในเกม"
        };

        //เลือกรายการที่จะแสดงให้ตรงกับชนิดรายรับหรือรายจ่าย
        if(mRadioIncome.isChecked()) {
            mItems = items_income;
        } else if(mRadioExpense.isChecked()) {
            mItems = items_expense;
        }

        //แสดง Item Dialog
        ItemDialog dialog = ItemDialog.newInstance("กรุณาเลือกรายการ", mItems);
        dialog.show(fm, null);
        dialog.setOnFinishDialogListener(new ItemDialog.OnFinishDialogListener() {
            @Override
            public void onFinishDialog(int selectedIndex) {
                if(selectedIndex <= -1) {
                    return;
                }
                //นำรายการที่ถูกเลือก มาเติมลงในช่องชื่อรายการ
                mEditTitle.setText(mItems[selectedIndex]);
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            startActivity(new Intent(InputExpenseMode1.this, EasyMode.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InputExpenseMode1.this, EasyMode.class));
    }

}
