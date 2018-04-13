package com.example.mfwis415a.food4life.addfood;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mfwis415a.food4life.R;
import com.example.mfwis415a.food4life.foodlist.FoodList;

import java.util.List;

import common.TagebuchDataSource;

public class ApplicationLogic extends AddFood {
    public static final String LOG_TAG = ApplicationLogic.class.getSimpleName();
    private TagebuchDataSource dataSource;

    ApplicationLogic(Context context) {
        // Database is opened
        dataSource = new TagebuchDataSource(context);
        // Function database is opened
        dataSource.open();
    }

    // function to add food
    protected void addFood(Spinner units) {
        // EditText fields are now referencing Id
        EditText foodName = (EditText) findViewById(R.id.foodName);
        EditText foodAmount = (EditText) findViewById(R.id.foodAmount);
        EditText calories = (EditText) findViewById(R.id.equivalent);
        EditText foodDescription = (EditText) findViewById(R.id.foodDescription);

        // Strings are declared
        String foodNameText = foodName.getText().toString();
        String foodDescriptionText = foodDescription.getText().toString();
        String foodAmountText = foodAmount.getText().toString();
        String caloriesText = calories.getText().toString();
        String unit = units.getSelectedItem().toString();

        // Check if the Strings are empty, if not  write back to database
        if (foodNameText.length() > 0 && foodDescriptionText.length() > 0 && foodAmountText.length() > 0 && unit.length() > 0 && caloriesText.length() > 0) {
            dataSource.addFoodEntry(foodNameText, foodDescriptionText, Float.parseFloat(foodAmountText), unit, Integer.parseInt(caloriesText));

            Intent myIntent = new Intent(ApplicationLogic.this, FoodList.class);
            ApplicationLogic.this.startActivity(myIntent);
        } else {
            Log.d(LOG_TAG, "Please fill all text fields!");
        }
    }

    // Function to load Spinnerdata
    protected void loadSpinnerData(Spinner units) {

        // Spinner Drop down elements
        List<String> labels = dataSource.getActiveUnits();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        units.setAdapter(dataAdapter);
    }


}
