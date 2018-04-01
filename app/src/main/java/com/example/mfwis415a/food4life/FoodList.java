package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import database.TagebuchDataSource;
import database.TagebuchHelper;

public class FoodList extends AppCompatActivity {

    private Button addFood;
    private ListView foodList;

    private TagebuchDataSource dataSource;

    private static final String LOG_TAG = TagebuchHelper.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        dataSource = new TagebuchDataSource(this);
        addFood = (Button) findViewById(R.id.addFood);
        foodList = (ListView) findViewById(R.id.foodList);

        dataSource.open();

        loadFoods();
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(FoodList.this, AddFood.class);
                FoodList.this.startActivity(myIntent);

            }
        });

        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(FoodList.this, EditOrDeleteFood.class);
                myIntent.putExtra("position", position); //Optional parameters
                FoodList.this.startActivity(myIntent);
            }
        });

    }


    public void loadFoods() {
        List<String> lables = dataSource.getAllFoods();

        if (!lables.isEmpty()) {
            // Get a handle to the list view
            ListView lv = (ListView) findViewById(R.id.foodList);

            lv.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, lables));
        }

    }


}

