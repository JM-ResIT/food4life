package com.example.mfwis415a.food4life;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        dataSource = new TagebuchDataSource(this);

        addFood = (Button) findViewById(R.id.addFood);

        dataSource.open();

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

    }
}
