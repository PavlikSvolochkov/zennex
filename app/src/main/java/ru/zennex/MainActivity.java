package ru.zennex;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

import ru.zennex.activity.MapActivity;
import ru.zennex.fragments.CameraFragment;
import ru.zennex.fragments.MainListFragment;
import ru.zennex.fragments.ParsingFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String LOCALE_RU = "ru";
    private String LOCALE_EN = "en";

    private MainListFragment mainListFragment;
    private ParsingFragment parsingFragment;
    private CameraFragment cameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_main, new MainListFragment()).commit();

        mainListFragment = new MainListFragment();
//        if (mainListFragment == null) {
//            mainListFragment = (MainListFragment) fragmentManager.findFragmentByTag("MainListFragment");
//            mainListFragment = new MainListFragment();
//            fragmentManager.beginTransaction().add(R.id.content_main, mainListFragment, "MainListFragment").commit();
//        }
        parsingFragment = new ParsingFragment();
//        if (parsingFragment == null) {
//            parsingFragment = (ParsingFragment) fragmentManager.findFragmentByTag("MainListFragment");
//            parsingFragment = new ParsingFragment();
//            fragmentManager.beginTransaction().add(R.id.content_main, parsingFragment, "MainListFragment").commit();
//        }
        cameraFragment = new CameraFragment();
//        if (cameraFragment == null) {
//            cameraFragment = (CameraFragment) fragmentManager.findFragmentByTag("MainListFragment");
//            cameraFragment = new CameraFragment();
//            fragmentManager.beginTransaction().add(R.id.content_main, cameraFragment, "MainListFragment").commit();
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            return true;
        if (id == R.id.english) {
            setupLocale(LOCALE_EN);
        } else if (id == R.id.russian) {
            setupLocale(LOCALE_RU);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupLocale(String loc) {
        Locale locale = new Locale(loc);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        this.recreate();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.list) {
            fragmentTransaction.replace(R.id.content_main, mainListFragment);
        } else if (id == R.id.camera) {
            fragmentTransaction.replace(R.id.content_main, cameraFragment);
        } else if (id == R.id.parsing) {
            fragmentTransaction.replace(R.id.content_main, parsingFragment);
        } else if (id == R.id.map) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }

        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
