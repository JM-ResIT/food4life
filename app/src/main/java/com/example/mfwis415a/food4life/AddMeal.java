package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import database.TagebuchDataSource;

public class AddMeal extends AppCompatActivity {

    private TextView dateView;
    private String date;
    private int category;
    private Spinner categories, foodsandmenus;

    private TagebuchDataSource dataSource;

    private String selecteditem;
    public static final String LOG_TAG = AddMeal.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        dataSource = new TagebuchDataSource(this);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        category = intent.getIntExtra("category", 0);

        dateView = (TextView) findViewById(R.id.MealDate);
        categories = (Spinner) findViewById(R.id.MealCategory);
        foodsandmenus = (Spinner) findViewById(R.id.FoodsAndMenus);

        dataSource.open();


        foodsandmenus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                selecteditem = adapter.getItemAtPosition(i).toString();
                Log.d(LOG_TAG, " SELECTED ITEM: " + selecteditem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        loadData();

    }

    public void loadData() {
        if (date.length() > 0) {
            dateView.setText(date);
        }
        loadCategorySpinnerData();
        loadFoodsAndMenus();
    }


    public void loadCategorySpinnerData() {
        // Spinner Drop down elements#
        List<String> labels = new ArrayList<String>();

        labels.add("Snack");
        labels.add("Frühstück");
        labels.add("Mittagessen");
        labels.add("Abendessen");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        categories.setAdapter(dataAdapter);

        categories.setSelection(category);
    }

    public void loadFoodsAndMenus() {
        // Spinner Drop down elements#
        List<String> labels = dataSource.getAllFoods();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        foodsandmenus.setAdapter(dataAdapter);

    }

    public void loadFoodUnitData() {

    }


}
