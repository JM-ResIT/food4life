package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.List;

import database.TagebuchDataSource;
import database.TagebuchHelper;


public class Calendar extends AppCompatActivity {

    public static final String TAG = "Calendaractivity";

    private TagebuchDataSource dataSource;
    private ListView fruehstueck, mittagessen, abendessen, snacks;
    private String dateC;

    private static final String LOG_TAG = Calendar.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        CalendarView mCalendarView = (CalendarView) findViewById(R.id.calendarView3);

        Button addBreakfastC = (Button) findViewById(R.id.AddBreakfastC);
        Button addLunchC = (Button) findViewById(R.id.AddLunchC);
        Button addDinnerC = (Button) findViewById(R.id.AddDinnerC);
        Button addSnacksC = (Button) findViewById(R.id.AddSnacksC);

        fruehstueck = (ListView) findViewById(R.id.ListViewBreakfastC);
        mittagessen = (ListView) findViewById(R.id.ListViewLunchC);
        abendessen = (ListView) findViewById(R.id.ListViewDinnerC);
        snacks = (ListView) findViewById(R.id.ListViewSnacksC);

        final long date = System.currentTimeMillis();
        SimpleDateFormat showDate = new SimpleDateFormat("dd.MM.yyyy");
        final String dateString = showDate.format(date);

        dateC = dateString;
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                if (i1 + 1 < 10 && i2 < 10) {
                    dateC = "0" + i2 + ".0" + (i1 + 1) + "." + i;
                } else if (i1 + 1 < 10) {
                    dateC = i2 + ".0" + (i1 + 1) + "." + i;
                } else {
                    dateC = i2 + "." + (i1 + 1) + "." + i;
                }
                clearListViews();
                populateListViews();

                Log.d(TAG, "onSelectedDayChange: date: " + dateC);
            }
        });

        dataSource = new TagebuchDataSource(this);
        dataSource.open();

        populateListViews();

        addBreakfastC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMealActivity(dateC, 1);
            }
        });
        addLunchC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMealActivity(dateC, 2);
            }
        });
        addDinnerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMealActivity(dateC, 3);

            }
        });
        addSnacksC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMealActivity(dateC, 0);

            }
        });

        fruehstueck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Selected Item: " + fruehstueck.getItemAtPosition(position));
                openEditOrDeleteMealActivity(dateC, 1, position);
            }
        });
        mittagessen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEditOrDeleteMealActivity(dateC, 2, position);
            }
        });
        abendessen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEditOrDeleteMealActivity(dateC, 3, position);
            }
        });
        snacks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEditOrDeleteMealActivity(dateC, 0, position);
            }
        });

    }

    private void clearListViews(){
        fruehstueck.setAdapter(null);
        mittagessen.setAdapter(null);
        abendessen.setAdapter(null);
        snacks.setAdapter(null);
    }

    private void populateListViews() {
        populateListView(R.id.ListViewBreakfastC, 1);
        populateListView(R.id.ListViewLunchC, 2);
        populateListView(R.id.ListViewDinnerC, 3);
        populateListView(R.id.ListViewSnacksC, 0);
    }


    private void populateListView(@IdRes int id, int category) {
        //Create list of items
        List<String> meals = dataSource.getMealEntries(dateC, category);

        if (!meals.isEmpty()) {
            // Get a handle to the list view
            ListView list = (ListView) findViewById(id);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, meals);
            list.setAdapter(adapter);

        }
    }


    private void openMealActivity(String date, int category) {
        Intent myIntent = new Intent(Calendar.this, AddMeal.class);
        myIntent.putExtra("date", date);
        myIntent.putExtra("category", category);
        myIntent.putExtra("fromMain", false);
        Calendar.this.startActivity(myIntent);
    }

    private void openEditOrDeleteMealActivity(String date, int category, int position){
        Intent myIntent = new Intent(Calendar.this, EditOrDeleteMeal.class);
        myIntent.putExtra("date", date);
        myIntent.putExtra("category", category);
        myIntent.putExtra("position", position);
        myIntent.putExtra("fromMain", false);
        Calendar.this.startActivity(myIntent);
    }


    private void goBack() {
        Intent myIntent = new Intent(Calendar.this, MainActivity.class);
        Calendar.this.startActivity(myIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
