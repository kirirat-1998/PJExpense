package com.example.pjexpense;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Calendar;

public class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() {}

    private SQLiteDatabase mDb;
    private SQLiteHelper mSqlite;

    private View mView;
    private GraphicalView mGraphView;

    private String text_year;
    private EditText edit_yearG;
    private Button submit_yearG;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph_year, container, false);
        mView = rootView;

        Calendar calendar = Calendar.getInstance();
        int yest = calendar.get(Calendar.YEAR)+543;

        edit_yearG = (EditText) rootView.findViewById(R.id.edit_graph_year);
        edit_yearG.setText(String.valueOf(yest));

        Log.i(String.valueOf(yest), "CalendarCalendar "+yest);

        submit_yearG = rootView.findViewById(R.id.submit_graph_year);
        submit_yearG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_year = edit_yearG.getText().toString();
                edit_yearG.setText(text_year);
//                    Log.i(String.valueOf(text_year), "gettexttttt "+text_year);
                showToast1(String.valueOf(text_year));
                initData();
                closeKeyboard();
            }
        });
        initData();
        return rootView;
    }

    private void showToast1(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private void initData() {
        String[] months = {"",
                "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.",
                "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."
        };

        int[] index = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        int yyyyy;

//            int[] dataIncomeA = {4000, 5500, 2300, 2100, 2500, 2900, 3200, 2400, 1800, 2100, 3500, 5900};
//            int[] dataIncomeB = {3600, 4500, 3200, 3600, 2800, 1800, 2100, 2900, 2200, 2500, 4000, 3500};
//            int[] incomeC = {0, 4000, 3000, 3200, 2400, 2500, 2600, 3400, 3900, 4500, 5000, 4500, 544, 5454 ,644};

        mSqlite = SQLiteHelper.getInstance(getActivity());
        mDb = mSqlite.getWritableDatabase();

        String sql =
                "SELECT * FROM table_income_expense " +
                        "WHERE year = ? " +
                        "ORDER BY month ASC";

        text_year = edit_yearG.getText().toString();

        int y = Integer.parseInt(text_year);
        y = y-543;
        String[] args = {y + ""};
        Cursor cursor = mDb.rawQuery(sql, args);

        Log.i(String.valueOf(text_year), "gettexttttt "+text_year);
        Log.i(String.valueOf(y), "gettexttttt "+y);

        int g_id = 0;
        int date_day = 0;
        int date_month = 0;
        int date_year = 0;
        String type ="";
        String title ="";
        int amount = 0;



        double income = 0;
        double expense = 0;
        double month_income1 = 0;
        double month_expense1 = 0;
        double month_income2 = 0;
        double month_expense2 = 0;
        double month_income3 = 0;
        double month_expense3 = 0;
        double month_income4 = 0;
        double month_expense4 = 0;
        double month_income5 = 0;
        double month_expense5 = 0;
        double month_income6 = 0;
        double month_expense6 = 0;
        double month_income7 = 0;
        double month_expense7 = 0;
        double month_income8 = 0;
        double month_expense8 = 0;
        double month_income9 = 0;
        double month_expense9 = 0;
        double month_income10 = 0;
        double month_expense10 = 0;
        double month_income11 = 0;
        double month_expense11 = 0;
        double month_income12 = 0;
        double month_expense12 = 0;

        double month_total1 = 0;
        double month_total2 = 0;
        double month_total3 = 0;
        double month_total4 = 0;
        double month_total5 = 0;
        double month_total6 = 0;
        double month_total7 = 0;
        double month_total8 = 0;
        double month_total9 = 0;
        double month_total10 = 0;
        double month_total11 = 0;
        double month_total12 = 0;

        int date_length = 0;
        int aaa=1;


        while(cursor.moveToNext()) {
            g_id = cursor.getInt(0);
            date_day = cursor.getInt(1);
            date_month = cursor.getInt(2);
            date_year = cursor.getInt(3);
            type = cursor.getString(4);
            title = cursor.getString(5);
            amount = cursor.getInt(6);

            if(type.equals("รายรับ")) {
                income = amount;
            } else if(type.equals("รายจ่าย")) {
                expense = amount;
            }

            Log.i(String.valueOf(amount), "asdasscsafsdsa "+date_month+" "+type+" : "+type+">>"+amount+"<<"+income+" : "+expense);

            if(date_month == 1){
                month_income1 = month_income1 + income;
                month_expense1 = month_expense1 + expense;
            }else if (date_month == 2){
                month_income2 = month_income2 + income;
                month_expense2 = month_expense2 + expense;
            }else if (date_month == 3){
                month_income3 = month_income3 + income;
                month_expense3 = month_expense3 + expense;
            }else if (date_month == 4){
                month_income4 = month_income4 + income;
                month_expense4 = month_expense4 + expense;
            }else if (date_month == 5){
                month_income5 = month_income5 + income;
                month_expense5 = month_expense5 + expense;
            }else if (date_month == 6){
                month_income6 = month_income6 + income;
                month_expense6 = month_expense6 + expense;
            }else if (date_month == 7){
                month_income7 = month_income7 + income;
                month_expense7 = month_expense7 + expense;
            }else if (date_month == 8){
                month_income8 = month_income8 + income;
                month_expense8 =+ expense;
            }else if (date_month == 9){
                month_income9 = month_income9 + income;
                month_expense9 = month_expense9 + expense;
            }else if (date_month == 10){
                month_income10 = month_income10 + income;
                month_expense10 = month_expense10 + expense;
            }else if (date_month == 11){
                month_income11 = month_income11 + income;
                month_expense11 = month_expense11 + expense;
            }else if (date_month == 12){
                month_income12 = month_income12 + income;
                month_expense12 = month_expense12 + expense;
            }else {
//                    Log.i(String.valueOf(index), "errorincomeexpense!!!");
            }
//                index = addIndex(index,aaa++);
            Log.i(String.valueOf(amount), "showTotal "+month_income6+" : "+month_expense6+" : "+type+" : "+title);

            expense = 0;
            income = 0;
//                date_length++;
        }
        month_total1 = month_income1 - month_expense1;
        month_total2 = month_income2 - month_expense2;
        month_total3 = month_income3 - month_expense3;
        month_total4 = month_income4 - month_expense4;
        month_total5 = month_income5 - month_expense5;
        month_total6 = month_income6 - month_expense6;
        month_total7 = month_income7 - month_expense7;
        month_total8 = month_income8 - month_expense8;
        month_total9 = month_income9 - month_expense9;
        month_total10 = month_income10 - month_expense10;
        month_total11 = month_income11 - month_expense11;
        month_total12 = month_income12 - month_expense12;


        Log.i(String.valueOf(amount), "showTotalmonth_total6 "+month_total6+"= ( "+month_income6+"-"+month_expense6+" )");
        double[] dataIncomeA = {0,month_income1,month_income2,month_income3,month_income4,
                month_income5,month_income6,month_income7,month_income8,
                month_income9,month_income10,month_income11,month_income12};
        double[] dataIncomeB = {0,month_expense1,month_expense2,month_expense3,month_expense4,
                month_expense5,month_expense6,month_expense7,month_expense8,
                month_expense9,month_expense10,month_expense11,month_expense12};
        double[] dataIncomeC = {0,month_total1,month_total2,month_total3,month_total4,
                month_total5,month_total6,month_total7,month_total8,
                month_total9,month_total10,month_total11,month_total12};
        double[] dataNull = {0,0,0,0,0,0,0,0,0,0,0,0};

        XYSeries seriesA = new XYSeries("รายรับ");
        XYSeries seriesB = new XYSeries("รายจ่าย");
//        XYSeries seriesC = new XYSeries("คงเหลือ");

        int length = index.length;
        for (int i = 0; i < length; i++) {
            seriesA.add(index[i], dataIncomeA[i]);
            seriesB.add(index[i], dataIncomeB[i]);
//            seriesC.add(index[i], dataIncomeC[i]);


            Log.i(String.valueOf(dataIncomeA.length), "aaaaadataIncomeA: "+dataIncomeA[i]);
            Log.i(String.valueOf(dataIncomeB.length), "aaaaaaaaadataIncomeB: "+dataIncomeB[i]);
//            Log.i(String.valueOf(dataIncomeC.length), "aaaaaaaaincomeC: "+dataIncomeC[i]);
        }

        XYSeriesRenderer rendererA = new XYSeriesRenderer();
        rendererA.setPointStyle(PointStyle.CIRCLE);
        rendererA.setColor(Color.BLUE);
        rendererA.setLineWidth(2);

        XYSeriesRenderer rendererB = new XYSeriesRenderer();
        rendererB.setPointStyle(PointStyle.X);
        rendererB.setColor(Color.RED);
        rendererB.setLineWidth(2);

//        XYSeriesRenderer rendererC = new XYSeriesRenderer();
//        rendererC.setPointStyle(PointStyle.DIAMOND);
//        rendererC.setColor(Color.GREEN);
//        rendererC.setLineWidth(2);

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(seriesA);
        dataset.addSeries(seriesB);
//        dataset.addSeries(seriesC);



        XYMultipleSeriesRenderer multipleSeriesRenderer
                = new XYMultipleSeriesRenderer();

        for (int i = 0; i < length; i++) {
            multipleSeriesRenderer.addXTextLabel(i + 1, months[i]);
        }
        multipleSeriesRenderer.setChartTitle("กราฟเส้นแสดงข้อมูลรายรับ - รายจ่าย");
        multipleSeriesRenderer.setYTitle("จำนวนเงิน (บาท)");
        multipleSeriesRenderer.setXTitle("ปี พ.ศ. "+text_year);
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


        drawChart(dataset, multipleSeriesRenderer);

        cursor.close();

    }

    private void drawChart(XYMultipleSeriesDataset dataset,
                           XYMultipleSeriesRenderer renderer) {

//        if (null == mGraphView) {
            mGraphView =
//                    ChartFactory.getBarChartView(getActivity(), dataset, renderer, BarChart.Type.DEFAULT);
                ChartFactory.getLineChartView(getActivity(), dataset, renderer);

//                mBarChartView = ChartFactory.getBarChartView(getContext(), data, barRenderer, BarChart.Type.DEFAULT);

            RelativeLayout container =
                    (RelativeLayout) mView.findViewById(R.id.graph_container);

            container.addView(mGraphView);
//        } else {
//            mGraphView.repaint();
//        }
    }
    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

//    static float[] addElement(float[] a, float e) {
//        a  = Arrays.copyOf(a, a.length + 1);
//        a[a.length - 1] = e;
//        return a;
//    }
//
//    static float[] addExpense(float[] a, float e) {
//        a  = Arrays.copyOf(a, a.length + 1);
//        a[a.length - 1] = e;
//        return a;
//    }
//    static int[] addIndex(int[] a, int e) {
//        a  = Arrays.copyOf(a, a.length + 1);
//        a[a.length - 1] = e;
//        return a;
//    }

}