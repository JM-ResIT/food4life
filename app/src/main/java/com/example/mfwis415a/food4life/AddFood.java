package com.example.mfwis415a.food4life;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import database.TagebuchDataSource;

public class AddFood extends AppCompatActivity {

    private Button addFood;
    private Spinner units;
    private EditText foodName, foodAmount, equivalent;
    private String unit;

    private TagebuchDataSource dataSource;
    public static final String LOG_TAG = AddFood.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        dataSource = new TagebuchDataSource(this);

        addFood = (Button) findViewById(R.id.addFood);
        units = (Spinner) findViewById(R.id.foodUnit);

        dataSource.open();
        loadSpinnerData();

        //TODO Fertigstellung
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodName = (EditText) findViewById(R.id.foodName);
                foodAmount = (EditText) findViewById(R.id.foodAmount);
                equivalent = (EditText) findViewById(R.id.equivalent);
                unit = units.getSelectedItem().toString();

                dataSource.addFoodEntry(foodName.getText().toString(), Integer.parseInt(foodAmount.getText().toString()), unit, Integer.parseInt(equivalent.getText().toString()));

                dataSource.listFood();


            }
        });

    }

    private void loadSpinnerData() {

        // Spinner Drop down elements
        List<String> lables = dataSource.getAllUnits();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        units.setAdapter(dataAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }
}
