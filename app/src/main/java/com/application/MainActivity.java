package com.application;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.application.fragments.CityFragment;
import com.application.fragments.InformationFragment;
import com.application.fragments.SettingsFragment;
import com.application.monument.Adapter;
import com.application.monument.ItemMonument;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // TODO: Объявление фрагментов
    SettingsFragment fset;
    CityFragment fcity;
    InformationFragment finf;
    // TODO: Переменная для работы с БД
    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: Создание объекта фрагмента
        fset = new SettingsFragment();
        fcity = new CityFragment();
        finf = new InformationFragment();

        RecyclerView recyclerView = findViewById(R.id.rv_list_monuments);
        ArrayList<ItemMonument> list = monuments();
        final Adapter adapter = new Adapter(this,list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                Toast toast = Toast.makeText(getApplicationContext(),
                        newText , Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // TODO: Функция заполнения и вывода списка
    private ArrayList<ItemMonument> monuments(){
        mDBHelper = new DBHelper(this);
        ArrayList<ItemMonument> list = new ArrayList<ItemMonument>();

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        String name = "";
        String description = "";
        String img = "";
        // TODO: "SELECT * FROM monument WHERE idcity=" + idCity + ";"
        int idCity =0;
        try{
            Bundle arguments = getIntent().getExtras();
            idCity = arguments.getInt("id");
        }catch (java.lang.NullPointerException e){
            FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
            ftrans.replace(R.id.container,fcity);
            ftrans.commit();
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Сначала выберите город!" , Toast.LENGTH_SHORT);
            toast.show();

        }
        int oldid = idCity;
        /// TODO: Цикл запроса и вывод данных в список
        Cursor cursor = mDb.rawQuery("SELECT * FROM monument WHERE idcity=" + oldid + ";", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            img = cursor.getString(4);
            name = cursor.getString(2);
            description = cursor.getString(3);
            list.add(new ItemMonument(img, name, description));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // TODO: Объявление объекта для управления Фрагментами
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        switch(item.getItemId()) {
            case R.id.nav_city:
                ftrans.replace(R.id.container,fcity);
                break;

            case R.id.nav_monuments:
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                break;

            case R.id.nav_settings:
                ftrans.replace(R.id.container,fset);
                break;

            case R.id.nav_info:
                ftrans.replace(R.id.container,finf);
                break;
        }ftrans.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
