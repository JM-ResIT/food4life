package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mfwis415a.food4life.R;

import java.util.ArrayList;
import java.util.List;

import database.TagebuchDataSource;

public class AddMeal extends AppCompatActivity {

    private TextView dateView;
    private String date;
    private int category;
    private Spinner categories;

    private TagebuchDataSource dataSource;

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

        dataSource.open();

        loadData();

    }

    public void loadData(){
        if(date.length() > 0){
            dateView.setText(date);
        }
        loadCategorySpinnerData();
    }

    public void loadCategorySpinnerData(){
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

    public void loadFoodUnitData(){

    }

}
