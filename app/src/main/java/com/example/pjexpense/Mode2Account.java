package com.example.pjexpense;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

public class Mode2Account extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode2_account);

        Toolbar toolbar = findViewById(R.id.toolbar_account);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_account);
        NavigationView navigationView = findViewById(R.id.nav_view_account);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_account);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(Mode2Account.this,Mode2.class));
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
                Intent home2 = new Intent(Mode2Account.this, Mode2.class);
                startActivity(home2);
                break;
//            case R.id.nav_account2:
//                Intent account2 = new Intent(Mode2Account.this, Mode2Account.class);
//                startActivity(account2);
//                break;
//            case R.id.nav_category2:
//                Intent category2 = new Intent(Mode2Account.this, Mode2Category.class);
//                startActivity(category2);
//                break;
            case R.id.nav_trend2:
                Intent trend2 = new Intent(Mode2Account.this, Mode2Trend.class);
                startActivity(trend2);
                break;
            case R.id.nav_saving2:
                Intent saving2 = new Intent(Mode2Account.this, Mode2Saving.class);
                startActivity(saving2);
                break;
            case R.id.nav_tools2:
                Intent tools2 = new Intent(Mode2Account.this, Mode2Tools.class);
                startActivity(tools2);
                break;
//            case R.id.nav_help2:
//                Intent help2 = new Intent(Mode2Account.this, Mode2Help.class);
//                startActivity(help2);
//                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_account);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
