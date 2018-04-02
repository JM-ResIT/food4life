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
                // TODO change values in database!
                Intent myIntent = new Intent(EditOrDeleteFood.this, FoodList.class);
                EditOrDeleteFood.this.startActivity(myIntent);
            }
        });
    }

    private void loadFoodData() {
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

        //TODO set unit
        // dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.EINHEIT,  TagebuchHelper.LEBENSMITTEL_ID, lm_id

        //set preselected values

        foodName.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_LMTABLE, TagebuchHelper.TITEL, TagebuchHelper.LEBENSMITTEL_ID, lm_id));
        foodDescription.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_LMTABLE, TagebuchHelper.BESCHREIBUNG, TagebuchHelper.LEBENSMITTEL_ID, lm_id));
        foodAmount.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.ANZAHL, TagebuchHelper.LEBENSMITTEL_ID, lm_id));
        equivalent.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.ENTSPRECHUNG,  TagebuchHelper.LEBENSMITTEL_ID, lm_id));
    }

}
