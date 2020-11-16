package com.example.pjexpense;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mode2Saving extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String myName,myEmail;

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
    private SQLiteHelper mSqlite;
    private SQLiteDatabase mDb;
    private Spinner mSpinnerMonth;
    private Spinner mSpinnerYear;
    static String[] words;
    private String[] mThaiMonths = {
            "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน",
            "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"
    };
    //******************************************************************
    ListView listView;
    int id;
    public static ListView mListView, checkListView;
    public static List<Item> items = new ArrayList<>();
    public static List<Item> tmp = new ArrayList<>();
    public static ArrayList<Categorie> cat = new ArrayList<>();
    TextView nb_tasks;
    public static boolean aff_done, aff_todo, aff_passed, aff_ondate;
    //******************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode2_saving);
        Toolbar toolbar = findViewById(R.id.toolbar_saving2);
        setSupportActionBar(toolbar);

        show_profile();

        FloatingActionButton fab = findViewById(R.id.fab_saving);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(Mode2Saving.this,AddItem.class);
                startActivityForResult(intentMain,1);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout_saving2);
        NavigationView navigationView = findViewById(R.id.nav_view_saving2);
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

/////////////////        ///**********************************************************************************
        mListView = (ListView) findViewById(R.id.listView);
//        nb_tasks = (TextView) findViewById(R.id.nb_tasks);
        aff_done = true;
        aff_todo = true;
        aff_passed = true;
        aff_ondate = true;
        id = 0;

        CheckBox checkToDo = (CheckBox) findViewById(R.id.switch_todo);
        checkToDo.setChecked(true);
        checkToDo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    aff_todo = true;
                else
                    aff_todo = false;
                affListCorresponding();
            }
        });
        CheckBox checkDone = (CheckBox) findViewById(R.id.switch_done);
        checkDone.setChecked(true);
        checkDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    aff_done = true;
                else
                    aff_done = false;
                affListCorresponding();
            }
        });

        getData();
        getCatData();
        if (cat.size() == 0)
//            cat.add(new Categorie("none", Color.parseColor("#262D3B")));
            cat.add(new Categorie("none", Color.parseColor("#F44336")));
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intentMain = new Intent(Mode2Saving.this, EditItem.class);
                Item item = (Item) mListView.getAdapter().getItem(position);
                String title = item.getTitle();
                String time = item.getTime();
//                String txt = item.getText();
                String date = item.getDate();
                String categorie = item.getCategorie();
                String txt[];
                txt = item.getText().split(" ");

                intentMain.putExtra("position", String.valueOf(position));
                intentMain.putExtra("title", title);
                intentMain.putExtra("txt", txt[0]);
                intentMain.putExtra("date", date);
                intentMain.putExtra("time", time);
                intentMain.putExtra("categorie", categorie);
                startActivityForResult(intentMain, 1);
            }
        });

        ItemAdapter adapter = new ItemAdapter(Mode2Saving.this, items);
        checkAdapter adapter1 = new checkAdapter(Mode2Saving.this, cat);
        mListView.setAdapter(adapter);
        navEmail.setVisibility(View.GONE);

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_saving2);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(Mode2Saving.this,Mode2.class));;
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
                Intent home2 = new Intent(Mode2Saving.this, Mode2.class);
                startActivity(home2);
                break;
//            case R.id.nav_account2:
//                Intent account2 = new Intent(Mode2Saving.this, Mode2Account.class);
//                startActivity(account2);
//                break;
//            case R.id.nav_category2:
//                Intent category2 = new Intent(Mode2Saving.this, Mode2Category.class);
//                startActivity(category2);
//                break;
            case R.id.nav_trend2:
                Intent trend2 = new Intent(Mode2Saving.this, Mode2Trend.class);
                startActivity(trend2);
                break;
            case R.id.nav_saving2:
                Intent saving2 = new Intent(Mode2Saving.this, Mode2Saving.class);
                startActivity(saving2);
                break;
            case R.id.nav_tools2:
                Intent tools2 = new Intent(Mode2Saving.this, Mode2Tools.class);
                startActivity(tools2);
                break;
//            case R.id.nav_help2:
//                Intent help2 = new Intent(Mode2Saving.this, Mode2Help.class);
//                startActivity(help2);
//                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_saving2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**********************************************************************************/
    /**
     * Recup les données des tasks dans la db
     */
    public void getData() {
        List<Item> list = new ArrayList<>();
        Item tmp;
        SQLiteDatabase mydatabase = openOrCreateDatabase("todolist", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tasks(" +
                "Titre VARCHAR, " +
                "Date VARCHAR, " +
                "Status VARCHAR" +
                ", Txt VARCHAR, " +
                "Cat VARCHAR);");
        Cursor resultSet = mydatabase.rawQuery("Select * from tasks", null);
        resultSet.moveToFirst();
        int count = 0;
        while (count < resultSet.getCount())
        {
            String title = resultSet.getString(resultSet.getColumnIndex("Titre"));
            String date = resultSet.getString(resultSet.getColumnIndex("Date"));
            String status = resultSet.getString(resultSet.getColumnIndex("Status"));
            String txt = resultSet.getString(resultSet.getColumnIndex("Txt"));
            String cat = resultSet.getString(resultSet.getColumnIndex("Cat"));
            Date d = new Date();
            SimpleDateFormat newDateFormat = new SimpleDateFormat("EE d MMM yyyyHH:mm");
            try {
                d = newDateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tmp = new Item(title, txt, d);
            if (status.equals(Item.Status.DONE.toString()))
                tmp.setStatus(Item.Status.DONE);
            else
                tmp.setStatus(Item.Status.TODO);
            tmp.setCategorie(cat);
            list.add(tmp);
            count++;
            resultSet.moveToNext();
        }
        items = list;
    }

    /**
     * Recup les données des catégories dans la db
     */
    public void getCatData() {
        ArrayList<Categorie> list = new ArrayList<>();
        Categorie tmp;
        SQLiteDatabase mydatabase = openOrCreateDatabase("todolist", MODE_PRIVATE, null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS cats(Name VARCHAR, Color VARCHAR);");
        Cursor resultSet = mydatabase.rawQuery("Select * from cats", null);
        resultSet.moveToFirst();
        int count = 0;
        while (count < resultSet.getCount()) {
            String name = resultSet.getString(resultSet.getColumnIndex("Name"));
            String color = resultSet.getString(resultSet.getColumnIndex("Color"));
            tmp = new Categorie(name, Integer.parseInt(color));
            list.add(tmp);
            count++;
            resultSet.moveToNext();
        }
        cat = list;
    }

    /**
     * Returns value to insert in db
     *
     * @return
     */
    public String addToDataBase(int i) {
        Item tmp = items.get(i);
        String query = "'";
        query += tmp.getTitle() + "','";
        query += tmp.getDate() + tmp.getTime() + "','";
        query += tmp.getStatus().toString() + "','";
        query += tmp.getText() + "','";
        query += tmp.getCategorie() + "'";
        return query;
    }

    /**
     * Sauvegarde les tasks dans la db
     */
    public void saveData() {
        String query;
        SQLiteDatabase mydatabase = openOrCreateDatabase("todolist", MODE_PRIVATE, null);
        mydatabase.execSQL("DROP TABLE IF EXISTS tasks");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS tasks(Titre VARCHAR, Date VARCHAR, Status VARCHAR, Txt VARCHAR, Cat VARCHAR);");

        for (int i = 0; i < items.size(); i++) {
            query = addToDataBase(i);
            mydatabase.execSQL("INSERT INTO tasks VALUES(" + query + ");");
        }
    }

    /**
     * Sauvegarde les catégories dans la db
     */
    public void saveCategory() {
        String query;
        SQLiteDatabase mydatabase = openOrCreateDatabase("todolist", MODE_PRIVATE, null);
        mydatabase.execSQL("DROP TABLE IF EXISTS cats");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS cats(Name VARCHAR, Color VARCHAR);");
        for (int i = 0; i < cat.size(); i++) {
            query = "'" + cat.get(i).getName() + "','" +  String.valueOf(cat.get(i).getColor()) + "'";
            mydatabase.execSQL("INSERT INTO cats VALUES(" + query + ");");
        }
    }

    /**
     * Ouvre le menu settings pour l'affichage et les catégories
     *
     * @param V
     */
    public void settings(View V) {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    /**
     * Ouvre l'activité pour ajouter un Item
     *
     * @param v
     */
    public void add(View v) {
        Intent intentMain = new Intent(Mode2Saving.this, AddItem.class);
        startActivityForResult(intentMain, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String title = data.getStringExtra("title");
                String txt = data.getStringExtra("txt");
                String date = data.getStringExtra("date");
                String delete = data.getStringExtra("delete");
                String category = data.getStringExtra("categorie");
                SimpleDateFormat newDateFormat = new SimpleDateFormat("EE d MMM yyyy k:m");
                Date d = null;
                try {
                    d = newDateFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (data.getStringExtra("edit").equals("true")) {
                    int position = Integer.parseInt(data.getStringExtra("position"));
                    try {
                        modifyItem(position, title, txt, d, delete, category);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Item newItem = new Item(title, txt, d);
                    newItem.setCategorie(category);
                    try {
                        addToList(newItem);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //here goes nothing
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                saveCategory();
                checkCategories();
                affListCorresponding();
                ((checkAdapter) checkListView.getAdapter()).notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                saveCategory();
                checkCategories();
                affListCorresponding();
                ((checkAdapter) checkListView.getAdapter()).notifyDataSetChanged();
            }
        }
    }

    /**
     * Affiche en rouge la date de la l'item si la date est déjà passée
     */
    public void checkDate() {
        int i = 0;
        Date d;

        d = new Date();
//        nb_tasks.setText(String.valueOf(items.size()) + " Tasks");
        while (i < items.size()) {
            if (!(items.get(i).getRealDate().after(d))) {
                items.get(i).setPassed(true);
                items.get(i).setDateColor("#FF0000");
            } else {
                items.get(i).setPassed(false);
                items.get(i).setDateColor("#121212");
            }
            i++;
        }
    }

    /**
     * Ajoute un item à la liste des items
     *
     * @param item
     * @throws ParseException
     */
    public void addToList(Item item) throws ParseException {
        items.add(item);
        checkDate();
        saveData();
        Date f = new Date();
        int c = 0;
        int color = Color.BLUE;
        while (c < cat.size()) {
            if (item.getCategorie().equals(cat.get(c).getName())) {
                color = cat.get(c).getColor();
            }
            c++;
        }
        int delay = (int) (item.getRealDate().getTime() - f.getTime());
        if (delay > 0)
            scheduleNotification(getNotification(item.getTitle(), item.getText(), color), delay);
        affListCorresponding();
    }

    /**
     * Modifie un item déja existant
     *
     * @param position
     * @param title
     * @param txt
     * @param d
     * @param delete
     * @param cate
     * @throws ParseException
     */
    public void modifyItem(int position, String title, String txt, Date d, String delete, String cate) throws ParseException {
        Item item = items.get(position);
        if (delete.equals("false")) {
            item.setTitle(title);
            item.setText(txt);
            item.setDueDate(d);
            item.setCategorie(cate);
        } else
            items.remove(item);
        checkDate();
        saveData();
        Date f = new Date();
        int delay = (int) (d.getTime() - f.getTime());
        int color = Color.BLUE;
        int c = 0;
        while (c < cat.size()) {
            if (item.getCategorie().equals(cat.get(c).getName())) {
                color = cat.get(c).getColor();
            }
            c++;
        }
        if (delay > 0)
            scheduleNotification(getNotification(title, txt, color), delay);
        affListCorresponding();
    }


    /**
     * Permet de savoir si l'item doit être affiché en fonction de sa catégorie
     *
     * @param item
     * @return
     */
    public boolean showCatForItem(Item item) {
        int i = 0;
        while (i < cat.size()) {
            if (cat.get(i).getName().equals(item.getCategorie())) {
                return cat.get(i).getShow();
            }
            i++;
        }
        return false;
    }

    /**
     * Permet d'afficher les tasks en fonctions des restrictions de l'utilisateur (statut, date, catégories etc...)
     */
    public void affListCorresponding() {
        int nb_items = items.size();
        boolean t, p;
        int i = 0;
        tmp.clear();
        while (i < nb_items) {
            t = false;
            p = false;
            if (aff_done && items.get(i).getStatus() == Item.Status.DONE)
                t = true;
            if (aff_todo && items.get(i).getStatus() == Item.Status.TODO)
                t = true;
            if ((aff_passed && items.get(i).getPassed()))
                p = true;
            if ((aff_ondate && !items.get(i).getPassed()))
                p = true;
            if (t && p && showCatForItem(items.get(i)))
                tmp.add(items.get(i));
            i++;
        }
        ItemAdapter adapter = new ItemAdapter(Mode2Saving.this, tmp);
        mListView.setAdapter(adapter);
//        if (tmp.size() > 1)
//            ((TextView) findViewById(R.id.nb_tasks)).setText(String.valueOf(tmp.size()) + " Tasks");
//        else
//            ((TextView) findViewById(R.id.nb_tasks)).setText(String.valueOf(tmp.size()) + " Task");
        adapter.notifyDataSetChanged();
    }

    /**
     * Passe le status d'une task en to do
     *
     * @param v
     */
    public void todoClick(View v) {
        final int position = mListView.getPositionForView((View) v.getParent());
//        cat.add(new Categorie("none", Color.parseColor("#F44336")));
        SwipeLayout s = (SwipeLayout) mListView.getChildAt(position);
        Item a = items.get(position);
        a.setStatus(Item.Status.TODO);


//        Categorie item_color2 = cat.get(position);
//        item_color2.setColor(Color.parseColor("#ffffff"));

        affListCorresponding();
        saveData();
        s.close(true);
    }

    /**
     * Passe une catégorie en visible ou invisible
     *
     * @param v
     */
    public void catCheck(View v) {
        final int position = checkListView.getPositionForView((View) v.getParent());
        CheckBox checkBox = (CheckBox) v;
        if (checkBox.isChecked())
            cat.get(position).setShow(true);
        else
            cat.get(position).setShow(false);
        affListCorresponding();
    }

    /**
     * Change le status de la task à Done
     *
     * @param v
     */
    public void doneClick(View v) {
        final int position = mListView.getPositionForView((View) v.getParent());
//        cat.add(new Categorie("none", Color.parseColor("#409E15")));
        SwipeLayout s = (SwipeLayout) mListView.getChildAt(position);
        Item a = items.get(position);
        a.setStatus(Item.Status.DONE);

        s.close(true);
        ItemAdapter b = (ItemAdapter) mListView.getAdapter();
        affListCorresponding();
        b.notifyDataSetChanged();
        saveData();
    }

    /**
     * Permet de preparer une notification
     *
     * @param notification
     * @param delay
     */
    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    /**
     * Permet à l'utilisateur de recevoir des notifications concernant ses taches
     *
     * @param content le contenu de la notification
     * @return un builder
     */
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification getNotification(String Title, String content, int color) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(Title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_notification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(color);
        }
        Log.d(Title, "getNotification333: ");
        affListCorresponding();
        return builder.build();
    }

    /**
     * Renvoies l'arraylist contenant les catégries
     *
     * @return l'array list contenant la ou les catégories
     */
    public static ArrayList<Categorie> getCatA() {
        ArrayList<Categorie> tmp = new ArrayList<Categorie>();
        int i = 0;
        while (i < cat.size())
            tmp.add(cat.get(i++));
        return tmp;
    }

    /**
     * Ferme le drawer menu
     *
     * @param v
     */
    public void closeMenu(View v) {
        DrawerLayout d = ((DrawerLayout) findViewById(R.id.drawer_layout));
        d.closeDrawers();
    }

    public static ArrayList<Categorie> getCat() {
        return (getCatA());
    }

    /**
     * Verifie si une catégorie à été supprimé et update les tasks si c'est le cas.
     */

    public void checkCategories() {
        int i = 0;

        while (i < items.size()) {
            int c = 0;
            boolean found = false;
            while (c < cat.size()) {
                if (items.get(i).getCategorie().equals(cat.get(c).getName()))
                    found = true;
                c++;
            }
            if (!found)
                items.get(i).setCategorie("none");
            i++;
        }
        affListCorresponding();
    }

    /**
     * Permet d'ajouter une catégorie via un menu
     *
     * @param v
     */
    public void addCategorie(View v) {
        Intent intentMain = new Intent(Mode2Saving.this, addCategory.class);
        startActivityForResult(intentMain, 2);
        checkCategories();
    }

    /**********************************************************************************/

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
