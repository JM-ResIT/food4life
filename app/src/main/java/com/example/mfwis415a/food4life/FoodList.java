package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import database.TagebuchDataSource;
import database.TagebuchHelper;

public class FoodList extends AppCompatActivity {

    // variables for this java class
    private TagebuchDataSource dataSource;

    private static final String LOG_TAG = FoodList.class.getSimpleName();

    // onCreate creates the Activity with the chosen Layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        // database is implemented
        dataSource = new TagebuchDataSource(this);

        // buttons and listview is now referencing Id's
        Button addFood = (Button) findViewById(R.id.addFood);
        ListView foodList = (ListView) findViewById(R.id.foodList);

        // database is opened
        dataSource.open();

        // loadfoods function is used
        loadFoods();

        // addfoods function is
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(FoodList.this, AddFood.class);
                FoodList.this.startActivity(myIntent);

            }
        });

        // function for item click is implemented
        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(FoodList.this, EditOrDeleteFood.class);
                myIntent.putExtra("position", position);
                FoodList.this.startActivity(myIntent);
            }
        });

    }


    // function to load food
    private void loadFoods() {
        List<String> lables = dataSource.getAllFoods();

        if (!lables.isEmpty()) {
            // Get a handle to the list view
            ListView lv = (ListView) findViewById(R.id.foodList);

            lv.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, lables));
        }

    }

    // Function for back button to go back to the previous activity
    private void goBack() {
        Intent myIntent = new Intent(FoodList.this, MainActivity.class);
        FoodList.this.startActivity(myIntent);
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

