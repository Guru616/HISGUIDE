package com.application.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.application.DBHelper;
import com.application.MainActivity;
import com.application.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CityFragment extends Fragment {

    // TODO: Переменная для работы с БД
    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: Объявление переменной для нахождения в разметке
        View v = inflater.inflate(R.layout.fragment_city, container, false);
        // TODO: Вывод списка городов
        ListView list = (ListView) v.findViewById(R.id.city_list);
        List<String> item = city();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.city_list_item,item);
        list.setAdapter(adapter);
        // TODO: Установка слушателя
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Инициализация объекта для отправки id города
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("id",position);
                startActivity(intent);
            }
        });
        return v;
    }
    private List<String> city(){
        mDBHelper = new DBHelper(getActivity());
        List<String> list = new ArrayList<String>();
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
        // TODO: Вывод списка городов
        Cursor cursor = mDb.rawQuery("SELECT * FROM city", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            name = cursor.getString(1);
            list.add(name);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
