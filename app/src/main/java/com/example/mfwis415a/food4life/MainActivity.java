package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import database.TagebuchDataSource;

// List View: http://www.programmierenlernenhq.de/tutorial-android-listview-verwenden/

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Button addMeal;
    private Button foodList;
    private ImageButton calendar;
    private TagebuchDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new TagebuchDataSource(this);

        populatelistview(); // Listview Method for Startscreen

        calendar = findViewById(R.id.goToCalendar);//ImageButton for opening Calendar Activity
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Calendar.class);
                MainActivity.this.startActivity(myIntent);
            }
        });



       /* addMeal = (Button) findViewById(R.id.AddFood);
        foodList = (Button) findViewById(R.id.goToFoodList);

        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, AddMeal.class);
                myIntent.putExtra("key", "test"); //Optional parameters
                MainActivity.this.startActivity(myIntent);

            }
        });

        foodList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, FoodList.class);
                myIntent.putExtra("key", "test"); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        })*/

        dataSource.open();

        dataSource.insertSampleDataIfEmpty();
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

    private void populatelistview() {
        //Create list of items
        String[] myItems ={"Banane 150 kcal","Apfel", "Müsli", "Knäckebrot", "Toast", "Salami", "Käse"};

        //Build Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.items, myItems);

        //Configure List View
        ListView list = (ListView) findViewById(R.id.ListViewBreakfast);
        list.setAdapter(adapter);
    }


}
