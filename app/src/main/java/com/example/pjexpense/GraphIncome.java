package com.example.pjexpense;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GraphIncome extends AppCompatActivity {

    private SQLiteHelper mSqlite;
    private SQLiteDatabase mDb;
    private Spinner mIncomeExpense;
    private Spinner Startmonth;
    private Spinner Endmonth;
    private Spinner Syear;
    private Spinner Eyear;

    private int date = 0;
    private int month = 0;
    private int year = 0;
    private String type = "";
    private String title = "";
    private int amount = 0;
    private int _id = 0;

    private int date2 = 0;
    private int month2 = 0;
    private int year2 = 0;
    private String type2 = "";
    private String title2 = "";
    private int amount2 = 0;
    private int _id2 = 0;

    private String[] mThaiMonths = {
            "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน",
            "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"
    };

    BarChart chart;

    List<Venta> listaVen = new ArrayList<>();
//    List<BarEntry> entradas = new ArrayList<>();
    List<Venta> listaVenEx = new ArrayList<>();
//    List<BarEntry> entradasEx = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_income);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.toolbar_graph_income);
        toolbar.setTitle("แผนภูมิแท่ง");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);

        chart = findViewById(R.id.chart_income);

        Startmonth = findViewById(R.id.spinner_month_start);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, mThaiMonths);
        Startmonth.setAdapter(adapter);
        Startmonth.setSelection(currentMonth);

        Endmonth = findViewById(R.id.spinner_month_end);
        ArrayAdapter<String> adapter_end = new ArrayAdapter<>(this, R.layout.spinner_item, mThaiMonths);
        Endmonth.setAdapter(adapter_end);
        Endmonth.setSelection(currentMonth);
        //ดักอีเวนต์การเลือกรายการที่ Spinner แต่ละอัน เพื่อให้นำรายการที่ถูกเลือก
        //ไปเป็นเงื่อนไขในการอ่านข้อมูลจากตารางแล้วนำมาแสดงผลใหม่ทันที
        Startmonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createItem();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
        //ดักอีเวนต์การเลือกรายการที่ Spinner แต่ละอัน เพื่อให้นำรายการที่ถูกเลือก
        //ไปเป็นเงื่อนไขในการอ่านข้อมูลจากตารางแล้วนำมาแสดงผลใหม่ทันที
        Endmonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createItem();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        Syear = findViewById(R.id.spinner_year_start);
        ArrayList years = new ArrayList();
        for(int i = 0; i < 3; i++) {
            years.add(String.valueOf(currentYear - i));
        }
        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, years);
        Syear.setAdapter(adapter);
        Syear.setSelection(0);

        Eyear = findViewById(R.id.spinner_year_end);
        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, years);
        Eyear.setAdapter(adapter);
        Eyear.setSelection(0);

        Syear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createItem();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        Eyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createItem();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        createItem();
        setSpinnerDropdownHeight();
    }

    private void createItem() {
        mSqlite = SQLiteHelper.getInstance(this);
        mDb = mSqlite.getWritableDatabase();

        String sql =
                "SELECT _id,date, month, year, type, title,SUM(amount) " +
                        "FROM table_income_expense " +
                        "WHERE type = 'รายรับ' " +
                        "AND month BETWEEN ? AND ?" +
                        "AND year BETWEEN ? AND ?";

        String sql2 =
                "SELECT _id,date, month, year, type, title,SUM(amount) " +
                        "FROM table_income_expense " +
                        "WHERE type = 'รายจ่าย' " +
                        "AND month BETWEEN ? AND ?" +
                        "AND year BETWEEN ? AND ?";

        int m = Startmonth.getSelectedItemPosition() + 1;
        int y = Integer.valueOf(Syear.getSelectedItem().toString());
        int me = Endmonth.getSelectedItemPosition() + 1;
        int ye = Integer.valueOf(Eyear.getSelectedItem().toString());

        String[] args = {m + "", me + "", y + "", ye + ""};
        Cursor cursor = mDb.rawQuery(sql, args);
        String[] args2 = {m + "", me + "", y + "", ye + ""};
        Cursor cursor_sumEx = mDb.rawQuery(sql2,args);

        //_id(0), date(1), month(2), year(3), type(4), title(5), amount(6)


        if(cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                _id = cursor.getInt(0);
                date = cursor.getInt(1);
                month = cursor.getInt(2);
                year = cursor.getInt(3);
                type = cursor.getString(4);
                title = cursor.getString(5);
                amount = cursor.getInt(6);

                listaVen.add(new Venta(
                        _id,
                        date,
                        month,
                        year,
                        type,
                        title,
                        amount
                ));
            } while(cursor.moveToNext());
        } else {
            Toast.makeText(this, "NO HAY REGISTROS", Toast.LENGTH_SHORT).show();
        }

        if(cursor_sumEx != null && cursor_sumEx.getCount() != 0) {
            cursor_sumEx.moveToFirst();
            do {
                _id2 = cursor_sumEx.getInt(0);
                date2 = cursor_sumEx.getInt(1);
                month2 = cursor_sumEx.getInt(2);
                year2 = cursor_sumEx.getInt(3);
                type2 = cursor_sumEx.getString(4);
                title2 = cursor_sumEx.getString(5);
                amount2 = cursor_sumEx.getInt(6);

                listaVenEx.add(new Venta(
                        _id2,
                        date2,
                        month2,
                        year2,
                        type2,
                        title2,
                        amount2
                ));
            } while(cursor_sumEx.moveToNext());
        } else {
            Toast.makeText(this, "NO HAY REGISTROS", Toast.LENGTH_SHORT).show();
        }

        mDb.close();

        chart.getDescription().setEnabled(false);

        BarDataSet barDataSet1 = new BarDataSet(barEn1(),"รายรับ");
        barDataSet1.setColor(Color.BLUE);
        BarDataSet barDataSet2 = new BarDataSet(barEn2(),"รายจ่าย");
        barDataSet2.setColor(Color.RED);

        BarData data = new BarData(barDataSet1,barDataSet2);
        chart.setData(data);

        String[] days = {" "," "};
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart.setDragEnabled(true);
        chart.setVisibleXRangeMaximum(3);

        float barS = 0.08f;
        float groupS = 0.44f;
        data.setBarWidth(0.10f);

        chart.getXAxis().setAxisMinimum(0);
        chart.getXAxis().setAxisMaximum(0+chart.getBarData().getGroupWidth(groupS,barS));
        chart.getAxisLeft().setAxisMinimum(0);

        chart.groupBars(0,groupS,barS);
//        barChart.setDescription("Set Bar Chart Description Here");  // set the description
        chart.animateY(800);
        chart.invalidate();
    }

    private List<BarEntry> barEn1(){
        List<BarEntry> entradas = new ArrayList<>();
        for(int i = 0 ; i < listaVen.size() ; i++) {
            entradas.add(new BarEntry(i, amount));
        }
        return entradas;
    }

    private List<BarEntry> barEn2(){
        List<BarEntry> entradasEx = new ArrayList<>();
        for(int i = 0 ; i < listaVenEx.size() ; i++) {
            entradasEx.add(new BarEntry(i, amount2));

        }
        return entradasEx;
    }

    private void setSpinnerDropdownHeight() {
        Startmonth.postDelayed(new Runnable() {
            @Override
            public void run() {
                Startmonth.setDropDownVerticalOffset(Startmonth.getHeight() + 5);
            }
        }, 500);
        Endmonth.postDelayed(new Runnable() {
            @Override
            public void run() {
                Endmonth.setDropDownVerticalOffset(Endmonth.getHeight() + 5);
            }
        }, 500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            startActivity(new Intent(GraphIncome.this, Mode2Trend.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
            startActivity(new Intent(GraphIncome.this,Mode2Trend.class));
    }
}
