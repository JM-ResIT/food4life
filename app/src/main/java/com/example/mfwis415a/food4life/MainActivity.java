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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import database.TagebuchDataSource;


public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Button addBreakfast;
    private Button addLunch;
    private Button addDinner;
    private Button addSnack;
    private Button foodList;
    private Button menuList;
    private ImageButton calendar;
    private ImageButton profile;
    private SimpleDateFormat showDate;
    private TextView tv;
    private TagebuchDataSource dataSource;
    private ProgressBar calorieProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new TagebuchDataSource(this);

        populatelistview(); // Listview Method for Startscreen


        final long date = System.currentTimeMillis();

        tv = findViewById(R.id.Date);
        SimpleDateFormat showDate = new SimpleDateFormat("dd.MM.yyyy");
        final String dateString = showDate.format(date);
        tv.setText(dateString);

        calendar = findViewById(R.id.goToCalendar);//ImageButton for opening Calendar Activity
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Calendar.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        addBreakfast = findViewById(R.id.AddBreakfast);
        addBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFoodAcivity(dateString, 1);
            }
        });

        addLunch = findViewById(R.id.AddLunch);
        addLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFoodAcivity(dateString, 2);
            }
        });

        addDinner = findViewById(R.id.AddDinner);
        addDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFoodAcivity(dateString, 3);

            }
        });

        addSnack = findViewById(R.id.AddSnacks);
        addSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFoodAcivity(dateString, 0);

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



        calorieProgress = findViewById(R.id.progressBar);
        calorieProgress.setScaleY(2f);

        calorieProgress.setMax(dataSource.getLimitFromProfile());
        calorieProgress.setProgress(500);

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

    private void openFoodAcivity(String date, int category){
        Intent myIntent = new Intent(MainActivity.this, AddMeal.class);
        myIntent.putExtra("date", date);
        myIntent.putExtra("category", category);
        MainActivity.this.startActivity(myIntent);
    }


}
