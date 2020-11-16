package com.example.pjexpense;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Mode2Trend extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    //private LineChart chart;
    private SQLiteHelper mSqlite;
    private SQLiteDatabase mDb;
//    private Spinner mIncomeExpense;
//    private String[] mThaiInEx = {
//            "รายรับ", "รายจ่าย"
//    };
//
//    BarChart grafica;
//
//
//    List<Venta> listaVentas = new ArrayList<>();
//    List<BarEntry> entradas = new ArrayList<>();
//    List listX1 = new ArrayList();

    private String myName,myEmail;
//    /************************** graph ***************/
//    private Spinner Startmonth;
//    private Spinner Endmonth;
//    private Spinner Syear;
//    private Spinner Eyear;
//
//    private int date = 0;
//    private int month = 0;
//    private int year = 0;
//    private String type = "";
//    private String title = "";
//    private int amount = 0;
//    private int _id = 0;
//
//    private int date2 = 0;
//    private int month2 = 0;
//    private int year2 = 0;
//    private String type2 = "";
//    private String title2 = "";
//    private int amount2 = 0;
//    private int _id2 = 0;
//
//    private String[] mThaiMonths = {
//            "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน",
//            "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"
//    };
//
//    /***************************** month year ***********************/
//    private int currentYear;
//    private int yearSelected;
//    private String monthText;
//    private int monthSelected;
//    private TextView textView;
//    private CheckBox shortMonthsCheckBox;
//
//    private String textget;
//    private int monthNumber=0;
//    private int yearNumber=0;
//    //////////////////////////////////////////////////////////////////////////////
//    private int currentYear2;
//    private int yearSelected2;
//    private String monthText2;
//    private int monthSelected2;
//    private TextView textView2;
//    private CheckBox shortMonthsCheckBox2;
//
//    private String textget2;
//    private int monthNumber2=0;
//    private int yearNumber2=0;
//    /***************************** month year ***********************/
//
//    BarChart chart;
//
//    List<Venta> listaVen = new ArrayList<>();
//    //    List<BarEntry> entradas = new ArrayList<>();
//    List<Venta> listaVenEx = new ArrayList<>();
////    List<BarEntry> entradasEx = new ArrayList<>();

    private int flagNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode2_trend);

        findViewById(R.id.test_toolbar_graph_income2).setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar_trend2);
        setSupportActionBar(toolbar);

        show_profile();

        DrawerLayout drawer = findViewById(R.id.drawer_layout_trend2);
        NavigationView navigationView = findViewById(R.id.nav_view_trend2);
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

        if (flagNumber == 0) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.graph_container_main, new PlaceholderFragment())
                    .commit();
        }


///********************** grarph ************************************/
//        Calendar cal = Calendar.getInstance();
//        int currentMonth = cal.get(Calendar.MONTH);
//        int currentYear = cal.get(Calendar.YEAR);
//
//        chart = findViewById(R.id.test_chart_income);
//
//        setUI();
//        createItem();
        navEmail.setVisibility(View.GONE);

    }

        @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_trend2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(Mode2Trend.this,Mode2.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting_graph, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.graph_to_month) {
            Intent intent = new Intent(Mode2Trend.this,GraphMonthYear.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.graph_to_year2){
            Intent intent = new Intent(Mode2Trend.this,TestGraphYear222.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {

            case R.id.nav_home2:
                Intent home2 = new Intent(Mode2Trend.this, Mode2.class);
                startActivity(home2);
                break;
//            case R.id.nav_account2:
//                Intent account2 = new Intent(Mode2Trend.this, Mode2Account.class);
//                startActivity(account2);
//                break;
//            case R.id.nav_category2:
//                Intent category2 = new Intent(Mode2Trend.this, Mode2Category.class);
//                startActivity(category2);
//                break;
            case R.id.nav_trend2:
                Intent trend2 = new Intent(Mode2Trend.this, Mode2Trend.class);
                startActivity(trend2);
                break;
            case R.id.nav_saving2:
                Intent saving2 = new Intent(Mode2Trend.this, Mode2Saving.class);
                startActivity(saving2);
                break;
            case R.id.nav_tools2:
                Intent tools2 = new Intent(Mode2Trend.this, Mode2Tools.class);
                startActivity(tools2);
                break;
//            case R.id.nav_help2:
//                Intent help2 = new Intent(Mode2Trend.this, Mode2Help.class);
//                startActivity(help2);
//                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_trend2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

//    private void createItem() {
//        mSqlite = SQLiteHelper.getInstance(this);
//        mDb = mSqlite.getWritableDatabase();
//
//        String sql =
//                "SELECT _id,date, month, year, type, title,SUM(amount) " +
//                        "FROM table_income_expense " +
//                        "WHERE type = 'รายรับ' " +
//                        "AND month BETWEEN ? AND ?" +
//                        "AND year BETWEEN ? AND ?";
//
//        String sql2 =
//                "SELECT _id,date, month, year, type, title,SUM(amount) " +
//                        "FROM table_income_expense " +
//                        "WHERE type = 'รายจ่าย' " +
//                        "AND month BETWEEN ? AND ?" +
//                        "AND year BETWEEN ? AND ?";
//
//        int m = monthNumber;
//        int y = yearNumber - 543;
//        int me = monthNumber2;
//        int ye = yearNumber2 - 543;
//
//        String[] args = {m + "", me + "", y + "", ye + ""};
//        Cursor cursor = mDb.rawQuery(sql, args);
//        String[] args2 = {m + "", me + "", y + "", ye + ""};
//        Cursor cursor_sumEx = mDb.rawQuery(sql2,args);
//
//        //_id(0), date(1), month(2), year(3), type(4), title(5), amount(6)
//
//
//        if(cursor != null && cursor.getCount() != 0) {
//            cursor.moveToFirst();
//            do {
//                _id = cursor.getInt(0);
//                date = cursor.getInt(1);
//                month = cursor.getInt(2);
//                year = cursor.getInt(3);
//                type = cursor.getString(4);
//                title = cursor.getString(5);
//                amount = cursor.getInt(6);
//
//                listaVen.add(new Venta(
//                        _id,
//                        date,
//                        month,
//                        year,
//                        type,
//                        title,
//                        amount
//                ));
//            } while(cursor.moveToNext());
//        } else {
//            Toast.makeText(this, "NO HAY REGISTROS", Toast.LENGTH_SHORT).show();
//        }
//
//        if(cursor_sumEx != null && cursor_sumEx.getCount() != 0) {
//            cursor_sumEx.moveToFirst();
//            do {
//                _id2 = cursor_sumEx.getInt(0);
//                date2 = cursor_sumEx.getInt(1);
//                month2 = cursor_sumEx.getInt(2);
//                year2 = cursor_sumEx.getInt(3);
//                type2 = cursor_sumEx.getString(4);
//                title2 = cursor_sumEx.getString(5);
//                amount2 = cursor_sumEx.getInt(6);
//
//                listaVenEx.add(new Venta(
//                        _id2,
//                        date2,
//                        month2,
//                        year2,
//                        type2,
//                        title2,
//                        amount2
//                ));
//            } while(cursor_sumEx.moveToNext());
//        } else {
//            Toast.makeText(this, "NO HAY REGISTROS", Toast.LENGTH_SHORT).show();
//        }
//
//        mDb.close();
//
//        chart.getDescription().setEnabled(false);
//
//        BarDataSet barDataSet1 = new BarDataSet(barEn1(),"รายรับ");
//        barDataSet1.setColor(Color.BLUE);
//        BarDataSet barDataSet2 = new BarDataSet(barEn2(),"รายจ่าย");
//        barDataSet2.setColor(Color.RED);
//
//        BarData data = new BarData(barDataSet1,barDataSet2);
//        chart.setData(data);
//
//        String[] days = {" "," "};
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(1);
//        xAxis.setGranularityEnabled(true);
//
//        chart.setPinchZoom(false);
//        chart.setDrawBarShadow(false);
//        chart.setDrawGridBackground(false);
//        chart.setDragEnabled(true);
//        chart.setVisibleXRangeMaximum(3);
//
//        float barS = 0.08f;
//        float groupS = 0.44f;
//        data.setBarWidth(0.10f);
//
//        chart.getXAxis().setAxisMinimum(0);
//        chart.getXAxis().setAxisMaximum(0+chart.getBarData().getGroupWidth(groupS,barS));
//        chart.getAxisLeft().setAxisMinimum(0);
//
//        chart.groupBars(0,groupS,barS);
////        barChart.setDescription("Set Bar Chart Description Here");  // set the description
//        chart.animateY(800);
//        chart.invalidate();
//    }
//
//    private List<BarEntry> barEn1(){
//        List<BarEntry> entradas = new ArrayList<>();
//        for(int i = 0 ; i < listaVen.size() ; i++) {
//            entradas.add(new BarEntry(i, amount));
//        }
//        return entradas;
//    }
//
//    private List<BarEntry> barEn2(){
//        List<BarEntry> entradasEx = new ArrayList<>();
//        for(int i = 0 ; i < listaVenEx.size() ; i++) {
//            entradasEx.add(new BarEntry(i, amount2));
//
//        }
//        return entradasEx;
//    }
//
//    private void setUI() {
//        textView = findViewById(R.id.test_date_start);
//        final CheckBox dateRangeCheckBox = findViewById(R.id.cbDateRange);
//        final CheckBox CustomTitleCheckBox = findViewById(R.id.cbCustomTitle);
//        shortMonthsCheckBox = findViewById(R.id.cbShortMonth);
//
//        dateRangeCheckBox.setVisibility(View.GONE);
//        CustomTitleCheckBox.setVisibility(View.GONE);
//        shortMonthsCheckBox.setVisibility(View.GONE);
////        dateRangeCheckBox.setVisibility(View.VISIBLE);
//
//        findViewById(R.id.test_date_start).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                displayMonthYearPickerDialogFragment(
//                        dateRangeCheckBox.isChecked(),
//                        CustomTitleCheckBox.isChecked()
//                );
//            }
//        });
//
//        textView2 = findViewById(R.id.test_date_to);
//        findViewById(R.id.test_date_to).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                displayMonthYearPickerDialogFragment2(
//                        dateRangeCheckBox.isChecked(),
//                        CustomTitleCheckBox.isChecked()
//                );
//            }
//        });
//
//        Calendar calendar = Calendar.getInstance();
//        currentYear = calendar.get(Calendar.YEAR)+543;
//        yearSelected = currentYear;
//        monthSelected = calendar.get(Calendar.MONTH);
///*********************  date2  **************/
//        currentYear2 = calendar.get(Calendar.YEAR)+543;
//        yearSelected2 = currentYear2;
//        monthSelected2 = calendar.get(Calendar.MONTH);
//
//        updateViews();
//        updateViews2();
//    }
//
//    private MonthYearPickerDialogFragment createDialog(boolean customTitle) {
//        return MonthYearPickerDialogFragment
//                .getInstance(monthSelected,
//                        yearSelected,
//                        customTitle ? getString(R.string.custom_title).toUpperCase() : null,
//                        shortMonthsCheckBox.isChecked() ? MonthFormat.SHORT : MonthFormat.LONG);
//    }
//
//    private MonthYearPickerDialogFragment createDialogWithRanges(boolean customTitle) {
//        final int minYear = 2010;
//        final int maxYear = currentYear;
//        final int maxMoth = 11;
//        final int minMoth = 0;
//        final int minDay = 1;
//        final int maxDay = 31;
//        long minDate;
//        long maxDate;
//
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.clear();
//        calendar.set(minYear, minMoth, minDay);
//        minDate = calendar.getTimeInMillis();
//
//        calendar.clear();
//        calendar.set(maxYear, maxMoth, maxDay);
//        maxDate = calendar.getTimeInMillis();
//
//        return MonthYearPickerDialogFragment
//                .getInstance(monthSelected,
//                        yearSelected,
//                        minDate,
//                        maxDate,
//                        customTitle ? getString(R.string.custom_title).toUpperCase() : null,
//                        shortMonthsCheckBox.isChecked() ? MonthFormat.SHORT : MonthFormat.LONG);
//    }
//
//    private void displayMonthYearPickerDialogFragment(boolean withRanges,
//                                                      boolean customTitle) {
//        MonthYearPickerDialogFragment dialogFragment = withRanges ?
//                createDialogWithRanges(customTitle) :
//                createDialog(customTitle);
//
//        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(int year, int monthOfYear) {
//                monthSelected = monthOfYear;
//                yearSelected = year;
//                updateViews();
//            }
//        });
//
//        dialogFragment.show(getSupportFragmentManager(), null);
//    }
//    /*****************    date2     *************/
//    private void displayMonthYearPickerDialogFragment2(boolean withRanges,
//                                                       boolean customTitle) {
//        MonthYearPickerDialogFragment dialogFragment = withRanges ?
//                createDialogWithRanges(customTitle) :
//                createDialog(customTitle);
//
//        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(int year, int monthOfYear) {
//                monthSelected2 = monthOfYear;
//                yearSelected2 = year;
//                updateViews2();
//            }
//        });
//
//        dialogFragment.show(getSupportFragmentManager(), null);
//    }
//    /******************************************************/
//    private void updateViews() {
//        monthText = new DateFormatSymbols().getMonths()[monthSelected];
//        textView.setText(String.format("%s %s", monthText, yearSelected));
//        textget = textView.getText().toString();
//
//        String textttt[] = textget.split(" ");
//
//        if (textttt[0].equals("มกราคม")){
//            monthNumber = 1;
//        }else if (textttt[0].equals("กุมภาพันธ์")){
//            monthNumber = 2;
//        }else if (textttt[0].equals("มีนาคม")){
//            monthNumber = 3;
//        }else if (textttt[0].equals("เมษายน")){
//            monthNumber = 4;
//        }else if (textttt[0].equals("พฤษภาคม")){
//            monthNumber = 5;
//        }else if (textttt[0].equals("มิถุนายน")){
//            monthNumber = 6;
//        }else if (textttt[0].equals("กรกฎาคม")){
//            monthNumber = 7;
//        }else if (textttt[0].equals("สิงหาคม")){
//            monthNumber = 8;
//        }else if (textttt[0].equals("กันยายน")){
//            monthNumber = 9;
//        }else if (textttt[0].equals("ตุลาคม")){
//            monthNumber = 10;
//        }else if (textttt[0].equals("พฤศจิกายน")){
//            monthNumber = 11;
//        }else if (textttt[0].equals("ธันวาคม")){
//            monthNumber = 12;
//        }else {
//            Log.i(String.valueOf("error"), "error error error error: ");
//        }
//        yearNumber = Integer.valueOf(textttt[1]);
//
//        Log.i(String.valueOf(monthNumber), "sfdsdfsdfds: "+yearNumber);
//
//        Log.i(String.valueOf(textttt.length), "textget: "+textttt[0]+
//                "\n 1 "+textttt[1]);
//        Log.i(String.valueOf(textget), "textget: "+textget);
//
//        createItem();
//    }
//    /********************  date2 *****************/
//    private void updateViews2() {
//        monthText2 = new DateFormatSymbols().getMonths()[monthSelected2];
//        textView2.setText(String.format("%s %s", monthText2, yearSelected2));
//        textget2 = textView2.getText().toString();
//
//        String textttt[] = textget2.split(" ");
//
//        if (textttt[0].equals("มกราคม")){
//            monthNumber2 = 1;
//        }else if (textttt[0].equals("กุมภาพันธ์")){
//            monthNumber2 = 2;
//        }else if (textttt[0].equals("มีนาคม")){
//            monthNumber2 = 3;
//        }else if (textttt[0].equals("เมษายน")){
//            monthNumber2 = 4;
//        }else if (textttt[0].equals("พฤษภาคม")){
//            monthNumber2 = 5;
//        }else if (textttt[0].equals("มิถุนายน")){
//            monthNumber2 = 6;
//        }else if (textttt[0].equals("กรกฎาคม")){
//            monthNumber2 = 7;
//        }else if (textttt[0].equals("สิงหาคม")){
//            monthNumber = 8;
//        }else if (textttt[0].equals("กันยายน")){
//            monthNumber2 = 9;
//        }else if (textttt[0].equals("ตุลาคม")){
//            monthNumber2 = 10;
//        }else if (textttt[0].equals("พฤศจิกายน")){
//            monthNumber2 = 11;
//        }else if (textttt[0].equals("ธันวาคม")){
//            monthNumber2 = 12;
//        }else {
//            Log.i(String.valueOf("error"), "error error error error: ");
//        }
//        yearNumber2 = Integer.valueOf(textttt[1]);
//
//        Log.i(String.valueOf(monthNumber2), "sfdsdfsdfds: "+yearNumber2);
//
//        Log.i(String.valueOf(textttt.length), "textget: "+textttt[0]+
//                "\n 1 "+textttt[1]);
//        Log.i(String.valueOf(textget2), "textget: "+textget2);
//
//        createItem();
//    }
}
