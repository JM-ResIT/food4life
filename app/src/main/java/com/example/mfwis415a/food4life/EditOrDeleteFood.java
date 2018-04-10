package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import database.TagebuchDataSource;
import database.TagebuchHelper;

public class EditOrDeleteFood extends AppCompatActivity {

    private static final String LOG_TAG = TagebuchHelper.class.getSimpleName();

    private TagebuchDataSource dataSource;
    private Button deleteFood;
    private Button editFood;
    private Integer lm_id;

    private Spinner units;
    private EditText foodName, foodAmount, equivalent, foodDescription;
    private String unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_food);

        deleteFood = (Button) findViewById(R.id.deleteFood);
        editFood = (Button) findViewById(R.id.editFood);
        foodName = (EditText) findViewById(R.id.foodName);
        foodAmount = (EditText) findViewById(R.id.foodAmount);
        equivalent = (EditText) findViewById(R.id.equivalent);
        foodDescription = (EditText) findViewById(R.id.foodDescription);
        units = (Spinner) findViewById(R.id.foodUnit);

        dataSource = new TagebuchDataSource(this);

        Intent mIntent = getIntent();
        int position = mIntent.getIntExtra("position", 0);

        dataSource.open();
        lm_id = dataSource.getRealIdFromLM(position);

        loadFoodData();

        deleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSource.updateStatusOfLM(lm_id, 0);
                Intent myIntent = new Intent(EditOrDeleteFood.this, FoodList.class);
                EditOrDeleteFood.this.startActivity(myIntent);
            }
        });

        editFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFood();
            }
        });
    }

    private void editFood(){
        unit = units.getSelectedItem().toString();
        String foodNameText= foodName.getText().toString();
        String foodDescriptionText =  foodDescription.getText().toString();
        String foodAmountText = foodAmount.getText().toString();
        String equivalentText = equivalent.getText().toString();

        if(foodNameText.length() > 0 && foodDescriptionText.length() > 0 && foodAmountText.length() > 0 && unit.length() > 0 &&  equivalentText.length() > 0){
            dataSource.editFoodEntry(foodNameText, foodDescriptionText , Integer.parseInt(foodAmountText) , unit, Integer.parseInt(equivalentText), lm_id);

            Intent myIntent = new Intent(EditOrDeleteFood.this, FoodList.class);
            EditOrDeleteFood.this.startActivity(myIntent);
        } else {
            Log.d(LOG_TAG, "Please fill all text fields!");
        }
    }

    //set preselected values
    private void loadFoodData() {
        //TODO set unit
        // dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.EINHEIT,  TagebuchHelper.LEBENSMITTEL_ID, lm_id

        foodName.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_LMTABLE, TagebuchHelper.TITEL, TagebuchHelper.LEBENSMITTEL_ID, lm_id));
        foodDescription.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_LMTABLE, TagebuchHelper.BESCHREIBUNG, TagebuchHelper.LEBENSMITTEL_ID, lm_id));
        foodAmount.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.ANZAHL, TagebuchHelper.LEBENSMITTEL_ID, lm_id));
        equivalent.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.ENTSPRECHUNG,  TagebuchHelper.LEBENSMITTEL_ID, lm_id));

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

    private void goToMain() {
        Intent myIntent = new Intent(EditOrDeleteFood.this, MainActivity.class);
        EditOrDeleteFood.this.startActivity(myIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            goToMain();
            return true;
        }

        return super.onKeyDown(keyCode, event);
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
