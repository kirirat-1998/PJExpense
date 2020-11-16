package com.example.pjexpense;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class TestGraphYear222 extends AppCompatActivity {
    private SQLiteDatabase mDb;
    private SQLiteHelper mSqlite;

    private View mView;
    private GraphicalView mGraphView;

    private String text_year_start;
    private String text_year_end;
    private EditText edit_year_start;
    private EditText edit_year_end;
    private Button submit_yearG;
    private int year_gggg_start;
    private int year_gggg_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_graph_year222);

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        year_gggg_start = calendar.get(Calendar.YEAR)+543;
        year_gggg_end = calendar2.get(Calendar.YEAR)+543;

        edit_year_start = (EditText) findViewById(R.id.edit_graph_start);
        edit_year_start.setText(String.valueOf(year_gggg_start));

        edit_year_end = (EditText) findViewById(R.id.edit_graph_end);
        edit_year_end.setText(String.valueOf(year_gggg_end));

        /***************** console.log **********************/
        Log.i(String.valueOf(year_gggg_start), "CalendarCalendar "+year_gggg_end);

        initData();
    }

    public void submit_startToEnd(View view) {
        text_year_start = edit_year_start.getText().toString();
        text_year_end = edit_year_end.getText().toString();
//        year_gggg = Integer.parseInt(text_year1);
        edit_year_start.setText(text_year_start);
        edit_year_end.setText(text_year_end);
        showToast1(text_year_start+" ถึง "+text_year_end);
        initData();
        closeKeyboard();
    }

    private void showToast1(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void initData() {

        mSqlite = SQLiteHelper.getInstance(this);
        mDb = mSqlite.getWritableDatabase();

        String sql =
                "SELECT SUM(amount), type, year, title FROM table_income_expense " +
                        "WHERE year BETWEEN ? AND ? AND type = 'รายรับ' GROUP BY year";

        String sql_ex =
                "SELECT SUM(amount), type, year, title FROM table_income_expense " +
                        "WHERE year BETWEEN ? AND ? AND type = 'รายจ่าย' GROUP BY year";

//        text_year1 = edit_yearG.getText().toString();
        text_year_start = edit_year_start.getText().toString();
        text_year_end = edit_year_end.getText().toString();

        int y1 = Integer.parseInt(text_year_start);
        int y2 = Integer.parseInt(text_year_end);
        y1 = y1-543;
        y2 = y2-543;
        String[] args = {y1 + "", y2 + ""};
        String[] args_ex = {y1 + "", y2 + ""};

        Cursor cursor = mDb.rawQuery(sql, args);
        Cursor cursor_ex = mDb.rawQuery(sql_ex, args_ex);

        int[] index = {};
        int[] allYear = {};
        double[] jjj = {};
        Log.i("TAG", "initDatayyyyyyyy: "+y1+"    "+y2);
        for (int i=0; i<=(y2-y1); i++){
            allYear = addIntYear(allYear,y1+i);
            jjj = addIncomeYearFinal(jjj,0);

        }
        for (int iin=0; iin<=(y2-y1); iin++){
            index = addIndex(index,iin+1);
        }

        int[] allYear_ex = {};
        double[] jjj_ex = {};
        Log.i("TAG", "initDatayyyyyyyy: "+y1+"    "+y2);
        for (int i_ex=0; i_ex<=(y2-y1); i_ex++){
            allYear_ex = addIntYear(allYear_ex,y1+i_ex);
            jjj_ex = addIncomeYearFinal(jjj_ex,0);
        }

        int g_id = 0;
        int date_day = 0;
        int date_month = 0;
        int date_year = 0;
        String type ="";
        String title ="";
        int amount = 0;

        double income = 0;
        double[] income_year = {};
        int[] year_year = {};

        int date_year_ex = 0;
        String type_ex ="";
        String title_ex ="";
        int amount_ex = 0;

        double expense = 0;
        double[] income_year_ex = {};
        int[] year_year_ex = {};

        while(cursor.moveToNext()) {

            amount = cursor.getInt(0);
            type = cursor.getString(1);
            date_year = cursor.getInt(2);
            title = cursor.getString(3);

            income = amount;

            Log.i(String.valueOf(income), "dataincomeee: ["+income+"  year = "+date_year+"  type: "+type+"  title: "+title);


            income_year = addIncomeYear(income_year,income);
            year_year = addYearYear(year_year,date_year);
            Log.i("TAG", "initData:dddd "+income_year[0]);

            expense = 0;
            income = 0;
        }
        int[] check_year={2019};
        int qqq=0;
        Log.v("CursorObject", DatabaseUtils.dumpCursorToString(cursor));
        Log.i("TAG", "initDatalength: "+year_year.length+" == "+allYear.length);
        if (year_year.length != allYear.length) {
            for (int j = 0; j < year_year.length; j++) {
                for (int k = 0; k < allYear.length; k++) {
                    if (year_year[j] == allYear[k]) {
                        jjj[k] = income_year[j];
                        Log.i("if", "year_year[ "+j+" "+k+" ]"+jjj[k]+"/"+year_year[j]+"/"+income_year[j]);
                        check_year = addYearYear(check_year,allYear[k]);
                        break;
                    }

                }
            }
            for (int a=0; a<jjj.length; a++){
                Log.i("TAG", "income_year_final0nIncome[ "+a+" ]: "+jjj[a]);
            }

        }else{
            for (int ii=0; ii<=(y2-y1); ii++){
                jjj[ii] = income_year[ii];
            }
        }


        /***********************************************************************************************/

        while(cursor_ex.moveToNext()) {

            amount_ex = cursor_ex.getInt(0);
            type_ex = cursor_ex.getString(1);
            date_year_ex = cursor_ex.getInt(2);
            title_ex = cursor_ex.getString(3);

            expense = amount_ex;

            Log.i(String.valueOf(expense), "datassdfsdfincomeee: ["+expense+"  year = "+date_year_ex+"  type: "+type_ex+"  title: "+title_ex);


            income_year_ex = addIncomeYear(income_year_ex,expense);
            year_year_ex = addYearYear(year_year_ex,date_year_ex);
            Log.i("TAG", "initData:dddd "+income_year_ex[0]);

            expense = 0;
            income = 0;
        }
        int[] check_year_ex={};
        int qqq_ex=0;
        Log.v("CursorObject", DatabaseUtils.dumpCursorToString(cursor_ex));
        Log.i("TAG", "initDatalength: "+year_year_ex.length+" == "+allYear_ex.length);
        if (year_year_ex.length != allYear_ex.length) {
            for (int j_ex = 0; j_ex < year_year_ex.length; j_ex++) {
                for (int k_ex = 0; k_ex < allYear_ex.length; k_ex++) {
                    if (year_year_ex[j_ex] == allYear_ex[k_ex]) {
                        Log.i("ifExpense", "year_year[ "+j_ex+" ] == allYear[ "+k_ex+" ] : "+year_year_ex[j_ex]+" == "+allYear[k_ex]+"   /");
                        jjj_ex[k_ex] = income_year_ex[j_ex];
                        check_year_ex = addYearYear(check_year_ex,allYear[k_ex]);
                        break;
                    }


                }
            }
        }else{
            for (int ii_ex=0; ii_ex<=(y2-y1); ii_ex++){
                jjj_ex[ii_ex] = income_year_ex[ii_ex];
            }
        }


        Log.i("TAGqqqqq", "aaaaaaaaa: "+qqq);

        for (int a=0; a<jjj.length; a++){
            Log.i("TAG", "income_year_final Income[ "+a+" ]: "+jjj[a]);
        }
        for (int a_ex=0; a_ex<jjj_ex.length; a_ex++){
            Log.i("TAG", "income_year_finalexx Expense[ "+a_ex+" ]: "+jjj_ex[a_ex]);
        }
        for (int asd=0; asd<check_year.length; asd++){
            Log.i("TAG", "check_yearcheck_year: "+check_year[asd]);
        }



        XYSeries seriesA = new XYSeries("รายรับ");
        XYSeries seriesB = new XYSeries("รายจ่าย");
//        XYSeries seriesC = new XYSeries("คงเหลือ");
//
        int length = allYear.length;
        for (int i_final = 0; i_final < length; i_final++) {
            seriesA.add(index[i_final], jjj[i_final]);
            seriesB.add(index[i_final], jjj_ex[i_final]);

        }
//
        XYSeriesRenderer rendererA = new XYSeriesRenderer();
        rendererA.setPointStyle(PointStyle.CIRCLE);
        rendererA.setColor(Color.BLUE);
        rendererA.setLineWidth(2);

        XYSeriesRenderer rendererB = new XYSeriesRenderer();
        rendererB.setPointStyle(PointStyle.X);
        rendererB.setColor(Color.RED);
        rendererB.setLineWidth(2);
//
//        XYSeriesRenderer rendererC = new XYSeriesRenderer();
//        rendererC.setPointStyle(PointStyle.DIAMOND);
//        rendererC.setColor(Color.GREEN);
//        rendererC.setLineWidth(2);
//
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(seriesA);
        dataset.addSeries(seriesB);
//        dataset.addSeries(seriesC);
//
//
//
        XYMultipleSeriesRenderer multipleSeriesRenderer
                = new XYMultipleSeriesRenderer();

        for (int i = 0; i < length; i++) {
            multipleSeriesRenderer.addXTextLabel(i + 1, String.valueOf(allYear[i]+543));
        }
        multipleSeriesRenderer.setChartTitle("กราฟเส้นแสดงข้อมูลรายรับ - รายจ่าย");
        multipleSeriesRenderer.setYTitle("จำนวนเงิน (บาท)");
        multipleSeriesRenderer.setXTitle("ปี พ.ศ. "+text_year_start+" - "+text_year_end);
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
//        multipleSeriesRenderer.addSeriesRenderer(rendererC);
//
//
        drawChart(dataset, multipleSeriesRenderer);

        cursor.close();

    }

    private void drawChart(XYMultipleSeriesDataset dataset,
                           XYMultipleSeriesRenderer renderer) {

//        if (null == mGraphView) {
            mGraphView =
//                    ChartFactory.getBarChartView(this, dataset, renderer, BarChart.Type.DEFAULT);
                ChartFactory.getLineChartView(this, dataset, renderer);

            RelativeLayout container = (RelativeLayout) findViewById(R.id.graph_container2);

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


}
