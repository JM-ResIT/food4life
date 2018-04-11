package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import database.TagebuchDataSource;


public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Button addBreakfast, addLunch, addDinner, addSnack, foodList, menuList;
    private ListView breakfast, lunch, dinner, snacks;
    private ImageButton calendar, profile;
    private SimpleDateFormat showDate;
    private TextView tv;
    private TextView tv_verbraucht;
    private TextView tv_verfuegbar;
    private TagebuchDataSource dataSource;
    private ProgressBar calorieProgress;
    private String dateString;
    private int availableCal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new TagebuchDataSource(this);
        dataSource.open();
        dataSource.insertSampleDataIfEmpty();

        final long date = System.currentTimeMillis();
        tv = findViewById(R.id.Date);
        breakfast = (ListView) findViewById(R.id.ListViewBreakfast);
        lunch = (ListView) findViewById(R.id.ListViewLunch);
        dinner = (ListView) findViewById(R.id.ListViewDinner);
        snacks = (ListView) findViewById(R.id.ListViewSnacks);

        SimpleDateFormat showDate = new SimpleDateFormat("dd.MM.yyyy");
        dateString = showDate.format(date);
        tv.setText(dateString);

        tv_verbraucht = findViewById(R.id.verbraucht);
        tv_verbraucht.setText("Eingenommen: " + String.valueOf(dataSource.getConsumedCalories(dateString)) + " kcal");

        tv_verfuegbar = findViewById(R.id.verfuegbar);
        tv_verfuegbar.setText("Verfügbar: " + String.valueOf(availableCalories()) + " kcal");


        calendar = findViewById(R.id.goToCalendar);//ImageButton for opening Calendar Activity
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(Calendar.class);
            }
        });

        addBreakfast = findViewById(R.id.AddBreakfast);
        addBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMealActivity(dateString, 1);
            }
        });

        addLunch = findViewById(R.id.AddLunch);
        addLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMealActivity(dateString, 2);
            }
        });

        addDinner = findViewById(R.id.AddDinner);
        addDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMealActivity(dateString, 3);

            }
        });

        addSnack = findViewById(R.id.AddSnacks);
        addSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMealActivity(dateString, 0);

            }
        });


        foodList = findViewById(R.id.goToFoodList);
        foodList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(FoodList.class);
            }
        });

        menuList = findViewById(R.id.goToMenuList);
        menuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(MenuList.class);
            }
        });

        profile = findViewById(R.id.goToProfile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(Profile.class);
            }
        });

        breakfast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEditOrDeleteMealActivity(dateString, 1, position);
            }
        });


        lunch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEditOrDeleteMealActivity(dateString, 2, position);
            }
        });

        dinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEditOrDeleteMealActivity(dateString, 3, position);
            }
        });

        snacks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEditOrDeleteMealActivity(dateString, 0, position);
            }
        });

        populateListViews(); // Listview Method for Startscreen

        calorieProgress = findViewById(R.id.progressBar);
        calorieProgress.setScaleY(2f);

        calorieProgress.setMax(dataSource.getLimitFromProfile());
        calorieProgress.setProgress(dataSource.getConsumedCalories(dateString));

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

    private void openNewActivity(Class<?> activity){
        Intent myIntent = new Intent(MainActivity.this,activity);
        MainActivity.this.startActivity(myIntent);
    }

    private void populateListViews() {
        populateListView(R.id.ListViewBreakfast, 1);
        populateListView(R.id.ListViewLunch, 2);
        populateListView(R.id.ListViewDinner, 3);
        populateListView(R.id.ListViewSnacks, 0);
    }

    private void populateListView(@IdRes int id, int category) {
        //Create list of items
        List<String> meals = dataSource.getMealEntries(dateString, category);


        if (!meals.isEmpty()) {
            // Get a handle to the list view
            ListView list = (ListView) findViewById(id);

            list.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, meals));
        }
    }

    private void openMealActivity(String date, int category) {
        Intent myIntent = new Intent(MainActivity.this, AddMeal.class);
        myIntent.putExtra("date", date);
        myIntent.putExtra("category", category);
        myIntent.putExtra("fromMain", true);
        MainActivity.this.startActivity(myIntent);
    }

    private void openEditOrDeleteMealActivity(String date, int category, int position){
        Intent myIntent = new Intent(MainActivity.this, EditOrDeleteMeal.class);
        myIntent.putExtra("date", date);
        myIntent.putExtra("category", category);
        myIntent.putExtra("position", position);
        myIntent.putExtra("fromMain", true);
        MainActivity.this.startActivity(myIntent);
    }

    private int availableCalories(){
       int max = dataSource.getLimitFromProfile();
       int used = dataSource.getConsumedCalories(dateString);

        availableCal = max - used;
        return availableCal;
    }

}
