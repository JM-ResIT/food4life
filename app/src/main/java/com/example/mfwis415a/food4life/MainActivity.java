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
import android.widget.TextView;

import java.text.SimpleDateFormat;

import database.TagebuchDataSource;

// List View: http://www.programmierenlernenhq.de/tutorial-android-listview-verwenden/

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Button addBreakfast;
    private Button foodList;
    private Button menuList;
    private ImageButton calendar;
    private ImageButton profile;
    private SimpleDateFormat showDate;
    private TextView tv;
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



        addBreakfast = (Button) findViewById(R.id.AddBreakfast);


        addBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, AddMeal.class);
                MainActivity.this.startActivity(myIntent);

            }
        });

        foodList =  findViewById(R.id.goToFoodList);
        foodList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, FoodList.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        menuList = findViewById(R.id.goToMenuList);
        menuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, MenuList.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        profile = findViewById(R.id.goToProfile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Profile.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        dataSource.open();

        dataSource.insertSampleDataIfEmpty();


        long date = System.currentTimeMillis();

        tv = findViewById(R.id.Date);
        SimpleDateFormat showDate = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = showDate.format(date);
        tv.setText(dateString);
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


        // Get a handle to the list view
        ListView list = (ListView) findViewById(R.id.ListViewBreakfast);

        list.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, myItems));
    }


}
