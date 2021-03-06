package com.example.mfwis415a.food4life.addfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mfwis415a.food4life.R;
import com.example.mfwis415a.food4life.foodlist.FoodList;

import java.util.List;

import common.TagebuchDataSource;

public class AddFood extends AppCompatActivity {

    // variables for this java class
    public static final String LOG_TAG = AddFood.class.getSimpleName();
    private Spinner units;
    private TagebuchDataSource dataSource;

    // onCreate creates the Activity with the chosen Layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Database is opened
        dataSource = new TagebuchDataSource(this);
        // Function database is opened
        dataSource.open();

        // Spinner is now referencing Id
        units = (Spinner) findViewById(R.id.foodUnit);

        // Button to add food to database
        Button addFood = (Button) findViewById(R.id.addFood);

        loadSpinnerData();
        // Button for addFood
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFood();
            }
        });
    }

    // function to add food
    protected void addFood() {
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

            Intent myIntent = new Intent(AddFood.this, FoodList.class);
            AddFood.this.startActivity(myIntent);
        } else {
            Log.d(LOG_TAG, "Please fill all text fields!");
        }
    }

    // Function to load Spinnerdata
    protected void loadSpinnerData() {

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

    // Function for back button to go back to the foodlist
    private void goBack() {
        Intent myIntent = new Intent(AddFood.this, FoodList.class);
        AddFood.this.startActivity(myIntent);
    }

    // Listener function for back key
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
