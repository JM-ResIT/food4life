package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
    private String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new TagebuchDataSource(this);
        dataSource.open();

        final long date = System.currentTimeMillis();
        tv = findViewById(R.id.Date);
        SimpleDateFormat showDate = new SimpleDateFormat("dd.MM.yyyy");
        dateString = showDate.format(date);
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


        foodList = findViewById(R.id.goToFoodList);
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

        dataSource.insertSampleDataIfEmpty();

        populateListViews(); // Listview Method for Startscreen

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

    private void populateListViews() {
        populateListView(R.id.ListViewBreakfast,1);
        populateListView(R.id.ListViewLunch,2);
        populateListView(R.id.ListViewDinner,3);
        populateListView(R.id.ListViewSnacks,0);
    }

    private void populateListView(@IdRes int id, int category) {
        //Create list of items
        List<String> breakfastMeals = dataSource.getMealEntries(dateString, category);


        if (!breakfastMeals.isEmpty()) {
            // Get a handle to the list view
            ListView list = (ListView) findViewById(id);

            list.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, breakfastMeals));
        }
    }

    private void openFoodAcivity(String date, int category) {
        Intent myIntent = new Intent(MainActivity.this, AddMeal.class);
        myIntent.putExtra("date", date);
        myIntent.putExtra("category", category);
        MainActivity.this.startActivity(myIntent);
    }


}
