package com.example.mfwis415a.food4life.AddFood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mfwis415a.food4life.AddMeal.AddMeal;
import com.example.mfwis415a.food4life.R;
import com.example.mfwis415a.food4life.StartScreen.MainActivity;

import database.TagebuchDataSource;

public class AddFood extends AppCompatActivity {

    private Button addFood;
    private EditText foodName, foodAmount, equivalent;
    private String unit;

    private TagebuchDataSource dataSource;
    public static final String LOG_TAG = AddFood.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        addFood = (Button) findViewById(R.id.addFood);

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        // dataSource.open();

        //TODO Fertigstellung
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodName = (EditText) findViewById(R.id.foodName);
                foodAmount = (EditText) findViewById(R.id.foodAmount);
                equivalent = (EditText) findViewById(R.id.equivalent);
                unit = "Stueck";

                dataSource.addFood(foodName.getText().toString());

            }
        });

        // dataSource.close();
    }
}
