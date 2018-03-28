package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import database.TagebuchDataSource;

public class FoodList extends AppCompatActivity {

    private Button addFood;

    private TagebuchDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        dataSource = new TagebuchDataSource(this);
        addFood = (Button) findViewById(R.id.addFood);

        dataSource.open();

        loadFoods();
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(FoodList.this, AddFood.class);
                myIntent.putExtra("key", "test"); //Optional parameters
                FoodList.this.startActivity(myIntent);

            }
        });

    }


    public void loadFoods(){
        List<String> lables = dataSource.getAllFoods();

        if(!lables.isEmpty()) {
            // Get a handle to the list view
            ListView lv = (ListView) findViewById(R.id.lebensmittel);

            lv.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, lables));
        }

    }

}

