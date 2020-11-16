package com.example.pjexpense;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Arrays;
import java.util.Calendar;

public class GraphMonthYear extends AppCompatActivity {
    private SQLiteDatabase mDb;
    private SQLiteHelper mSqlite;

    private Spinner mSpinnerMonthStart;
    private Spinner mSpinnerMonthEnd;
    private String[] mThaiMonths = {
            "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน",
            "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"
    };
    private String[] mThaiMonths2 = {
            "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน",
            "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"
    };
    private String[] months = {"",
            "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.",
            "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."
    };

    private EditText edit_year_month;
    private GraphicalView mGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_month_year);

        //ใช้คลาส Calendar เพื่ออ่านค่าเดือนและปีปัจจุบัน
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR) + 543;   //แปลงเป็น พ.ศ.

        edit_year_month = findViewById(R.id.edit_graph_year_month);
        edit_year_month.setText(String.valueOf(currentYear));
        //สร้างรายชื่อเดือนเติมลงใน Spinner อันแรก
        mSpinnerMonthStart = (Spinner) findViewById(R.id.spinner_monthsss);
        ArrayAdapter<String> adapter_start = new ArrayAdapter<>(this, R.layout.spinner_item, mThaiMonths);
        mSpinnerMonthStart.setAdapter(adapter_start);
        mSpinnerMonthStart.setSelection(currentMonth);

        mSpinnerMonthEnd = (Spinner) findViewById(R.id.spinner_montheeee);
        ArrayAdapter<String> adapter_end = new ArrayAdapter<>(this, R.layout.spinner_item, mThaiMonths2);
        mSpinnerMonthEnd.setAdapter(adapter_end);
        mSpinnerMonthEnd.setSelection(currentMonth);

        //ดักอีเวนต์การเลือกรายการที่ Spinner แต่ละอัน เพื่อให้นำรายการที่ถูกเลือก
        //ไปเป็นเงื่อนไขในการอ่านข้อมูลจากตารางแล้วนำมาแสดงผลใหม่ทันที
//        mSpinnerMonthStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                initData();
//            }
//
//            @Override public void onNothingSelected(AdapterView<?> parent) { }
//        });
//
//        mSpinnerMonthEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                initData();
//            }
//
//            @Override public void onNothingSelected(AdapterView<?> parent) { }
//        });


        initData();
        setSpinnerDropdownHeight();
    }

    public void goMonthToMonth(View view) {
        initData();
    }

    private void initData() {

        mSqlite = SQLiteHelper.getInstance(this);
        mDb = mSqlite.getWritableDatabase();

        String sql =
                "SELECT SUM(amount), type, month, year, title FROM table_income_expense " +
                        "WHERE year = ? " +
                        "AND month BETWEEN ? AND ? " +
                        "AND type = 'รายรับ' GROUP BY month";

        String sql_ex =
                "SELECT SUM(amount), type, month, year, title FROM table_income_expense " +
                        "WHERE year = ? " +
                        "AND month BETWEEN ? AND ? " +
                        "AND type = 'รายจ่าย' GROUP BY month";

        //อ่านค่าเดือนและปีที่ถูกเลือกจาก Spinner เพื่อเติมลงในเงื่อนไขของ SQL
        int y;
        int mStart = mSpinnerMonthStart.getSelectedItemPosition() + 1;
        int mEnd = mSpinnerMonthEnd.getSelectedItemPosition() + 1;

        String ysss = edit_year_month.getText().toString();
        y = Integer.parseInt(ysss)-543;

        String[] args = {y + "", mStart + "", mEnd + ""};
        String[] args_ex = {y + "", mStart + "", mEnd + ""};

        Cursor cursor = mDb.rawQuery(sql, args);
        Cursor cursor_ex = mDb.rawQuery(sql_ex, args_ex);

        int[] index = {};
        int[] allMonth = {};
//        double[] jjj = {};
        Log.i("TAG", "initDataymonth: "+mStart+"    "+mEnd);
        for (int i=0; i<=(mEnd-mStart); i++){
            allMonth = addIntYear(allMonth,mStart);
//            index = addIndex(index,i);
//            jjj = addIncomeYearFinal(jjj,0);

        }
        for (int iin=0; iin<=((mEnd-mStart)+1); iin++){
            index = addIndex(index,iin);
        }
//
//        int[] allYear_ex = {};
//        double[] jjj_ex = {};
//        Log.i("TAG", "initDatayyyyyyyy: "+y1+"    "+y2);
//        for (int i_ex=0; i_ex<=(y2-y1); i_ex++){
//            allYear_ex = addIntYear(allYear_ex,y1+i_ex);
//            jjj_ex = addIncomeYearFinal(jjj_ex,0);
//        }

        int g_id = 0;
        int date_day = 0;
        int date_month = 0;
        int date_year = 0;
        String type ="";
        String title ="";
        int amount = 0;

        double income = 0;
        double[] income_year = {0};
        int[] year_year = {};

        int date_month_ex = 0;
        int date_year_ex = 0;
        String type_ex ="";
        String title_ex ="";
        int amount_ex = 0;

        double expense = 0;
        double[] income_year_ex = {0};
        int[] year_year_ex = {};

        while(cursor.moveToNext()) {

            amount = cursor.getInt(0);
            type = cursor.getString(1);
            date_month = cursor.getInt(2);
            date_year = cursor.getInt(3);
            title = cursor.getString(4);

            income = amount;

            Log.i(String.valueOf(income), "monthadegdhf: ["+income+"   "+date_month+" / "+date_year+"  type: "+type+"  title: "+title);

            income_year = addIncomeYear(income_year,income);
            year_year = addYearYear(year_year,date_month);
//            Log.i("TAG", "initData:dddd "+income_year[0]);

            expense = 0;
            income = 0;
        }



        /***********************************************************************************************/

        while(cursor_ex.moveToNext()) {

            amount_ex = cursor_ex.getInt(0);
            type_ex = cursor_ex.getString(1);
            date_month_ex = cursor_ex.getInt(2);
            date_year_ex = cursor_ex.getInt(3);
            title_ex = cursor_ex.getString(4);

            expense = amount_ex;

            Log.i(String.valueOf(expense), "monthyetatat: ["+expense+"   "+date_month_ex+" / "+date_year_ex+
                    "  type: "+type_ex+"  title: "+title_ex);


            income_year_ex = addIncomeYear(income_year_ex,expense);
//            year_year_ex = addYearYear(year_year_ex,date_year_ex);
//            Log.i("TAG", "initData:dddd "+income_year_ex[0]);

            expense = 0;
            income = 0;
        }
        String[] month_length ={};
//        String origiMon = "";
//        for (int monin=1; monin<=allMonth.length; monin++){
//            if (monin == 1){
//                origiMon = "ม.ค.";
//            }else if (monin == 2){
//                origiMon = "ก.พ.";
//            }else if (monin == 3){
//                origiMon = "มี.ค.";
//            }else if (monin == 4){
//                origiMon = "เม.ย.";
//            }else if (monin == 5){
//                origiMon = "พ.ค.";
//            }else if (monin == 6){
//                origiMon = "มิ.ย.";
//            }else if (monin == 7){
//                origiMon = "ก.ค.";
//            }else if (monin == 8){
//                origiMon = "ส.ค.";
//            }else if (monin == 9){
//                origiMon = "ก.ย.";
//            }else if (monin == 10){
//                origiMon = "ต.ค.";
//            }else if (monin == 11){
//                origiMon = "พ.ย.";
//            }else if (monin == 12){
//                origiMon = "ธ.ค.";
//            }else {
//                showToast("error");
//            }
//            month_length = addString(month_length,origiMon);
//        }
        for (int fiM=year_year[0]; fiM<=mEnd; fiM++){
            month_length = addString(month_length,String.valueOf(months[fiM]));
            Log.i("TAG", "months[fiM] "+fiM+" / "+months[fiM]);
        }


        XYSeries seriesA = new XYSeries("รายรับ");
        XYSeries seriesB = new XYSeries("รายจ่าย");
////        XYSeries seriesC = new XYSeries("คงเหลือ");
////
        int length = allMonth.length;
        for (int i_final = 0; i_final <= length; i_final++) {
            seriesA.add(index[i_final], income_year[i_final]);
            seriesB.add(index[i_final], income_year_ex[i_final]);

        }
////
        XYSeriesRenderer rendererA = new XYSeriesRenderer();
        rendererA.setPointStyle(PointStyle.CIRCLE);
        rendererA.setColor(Color.BLUE);
        rendererA.setLineWidth(2);

        XYSeriesRenderer rendererB = new XYSeriesRenderer();
        rendererB.setPointStyle(PointStyle.X);
        rendererB.setColor(Color.RED);
        rendererB.setLineWidth(2);
////
////        XYSeriesRenderer rendererC = new XYSeriesRenderer();
////        rendererC.setPointStyle(PointStyle.DIAMOND);
////        rendererC.setColor(Color.GREEN);
////        rendererC.setLineWidth(2);
////
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(seriesA);
        dataset.addSeries(seriesB);
////        dataset.addSeries(seriesC);
////
////
////
        XYMultipleSeriesRenderer multipleSeriesRenderer
                = new XYMultipleSeriesRenderer();

        for (int i = 0; i < month_length.length; i++) {
            multipleSeriesRenderer.addXTextLabel(i + 1, month_length[i]);
            Log.i("TAG", "month_lengthmonth_length: "+month_length[i]);
        }
        multipleSeriesRenderer.setChartTitle("กราฟเส้นแสดงข้อมูลรายรับ - รายจ่าย");
        multipleSeriesRenderer.setYTitle("จำนวนเงิน (บาท)");
        multipleSeriesRenderer.setXTitle("ปี พ.ศ. "+(y+543));
        multipleSeriesRenderer.setZoomButtonsVisible(true);

        multipleSeriesRenderer.setXLabels(0);
        multipleSeriesRenderer.setBackgroundColor(Color.WHITE);
        multipleSeriesRenderer.setApplyBackgroundColor(true);
        multipleSeriesRenderer.setMarginsColor(Color.WHITE);
        multipleSeriesRenderer.setLabelsColor(Color.BLACK);
        multipleSeriesRenderer.setAxesColor(Color.GRAY);
        multipleSeriesRenderer.setYLabelsColor(0, Color.BLACK);
        multipleSeriesRenderer.setXLabelsColor(Color.BLACK);
        multipleSeriesRenderer.setMargins(new int[] { 50, 50, 25, 22 });


        multipleSeriesRenderer.addSeriesRenderer(rendererA);
        multipleSeriesRenderer.addSeriesRenderer(rendererB);
////        multipleSeriesRenderer.addSeriesRenderer(rendererC);
////
////
        drawChart(dataset, multipleSeriesRenderer);

        cursor.close();

    }

    private void drawChart(XYMultipleSeriesDataset dataset,
                           XYMultipleSeriesRenderer renderer) {

//        if (null == mGraphView) {
        mGraphView =
//                    ChartFactory.getBarChartView(this, dataset, renderer, BarChart.Type.DEFAULT);
                ChartFactory.getLineChartView(this, dataset, renderer);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.graph_month_year);

        container.addView(mGraphView);
//        } else {
//            mGraphView.repaint();
//        }
    }



    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
    private String[] addString(String[] a, String e){
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }

    private int[] addIndex(int[] a, int e) {
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }

    private int[] addIntYear(int[] a, int e) {
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }

    private double[] addIncomeYear(double[] a, double e) {
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }
    private int[] addYearYear(int[] a, int e) {
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }
    private double[] addIncomeYearFinal(double[] a, double e) {
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }

    private void setSpinnerDropdownHeight() {
        mSpinnerMonthStart.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSpinnerMonthStart.setDropDownVerticalOffset(mSpinnerMonthStart.getHeight() + 5);
            }
        }, 500);

        mSpinnerMonthEnd.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSpinnerMonthEnd.setDropDownVerticalOffset(mSpinnerMonthEnd.getHeight() + 5);
            }
        }, 500);

    }

    private void showToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }


}
