package com.example.mfwis415a.food4life;

import android.content.Intent;
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
    private EditText foodName, foodAmount, equivalent, foodDescription;
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

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFood();
            }
        });

    }

    private void addFood(){
        foodName = (EditText) findViewById(R.id.foodName);
        foodAmount = (EditText) findViewById(R.id.foodAmount);
        equivalent = (EditText) findViewById(R.id.equivalent);
        foodDescription = (EditText) findViewById(R.id.foodDescription);

        String foodNameText= foodName.getText().toString();
        String foodDescriptionText =  foodDescription.getText().toString();
        String foodAmountText = foodAmount.getText().toString();
        String equivalentText = equivalent.getText().toString();
        unit = units.getSelectedItem().toString();

        if(foodNameText.length() > 0 && foodDescriptionText.length() > 0 && foodAmountText.length() > 0 && unit.length() > 0 &&  equivalentText.length() > 0){
            dataSource.addFoodEntry(foodNameText, foodDescriptionText , Float.parseFloat(foodAmountText) , unit, Integer.parseInt(equivalentText));

            Intent myIntent = new Intent(AddFood.this, FoodList.class);
            AddFood.this.startActivity(myIntent);
        } else {
            Log.d(LOG_TAG, "Please fill all text fields!");
        }
    }

    private void loadSpinnerData() {

        // Spinner Drop down elements
        List<String> labels = dataSource.getAllUnits();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

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
