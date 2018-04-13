package com.example.mfwis415a.food4life.calendar;

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

import com.example.mfwis415a.food4life.editordeletemeal.EditOrDeleteMeal;
import com.example.mfwis415a.food4life.main.MainActivity;
import com.example.mfwis415a.food4life.R;
import com.example.mfwis415a.food4life.addmeal.AddMeal;

import java.text.SimpleDateFormat;
import java.util.List;

import common.TagebuchDataSource;


public class Calendar extends AppCompatActivity {

    // variables for this java class
    public static final String TAG = "Calendaractivity";

    private TagebuchDataSource dataSource;
    private ListView fruehstueck, mittagessen, abendessen, snacks;
    private String dateC;

    private static final String LOG_TAG = Calendar.class.getSimpleName();

    // onCreate creates the Activity with the chosen Layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        // calendarview is declared and is now referencing Id
        CalendarView mCalendarView = (CalendarView) findViewById(R.id.calendarView3);

        // buttons for entries are declared and are now referencing Id's
        Button addBreakfastC = (Button) findViewById(R.id.AddBreakfastC);
        Button addLunchC = (Button) findViewById(R.id.AddLunchC);
        Button addDinnerC = (Button) findViewById(R.id.AddDinnerC);
        Button addSnacksC = (Button) findViewById(R.id.AddSnacksC);

        // listviews are declared and are now referencing Id's
        fruehstueck = (ListView) findViewById(R.id.ListViewBreakfastC);
        mittagessen = (ListView) findViewById(R.id.ListViewLunchC);
        abendessen = (ListView) findViewById(R.id.ListViewDinnerC);
        snacks = (ListView) findViewById(R.id.ListViewSnacksC);

        // format for string "Date" is implemented
        final long date = System.currentTimeMillis();
        SimpleDateFormat showDate = new SimpleDateFormat("dd.MM.yyyy");
        final String dateString = showDate.format(date);

        dateC = dateString;

        // datechange listener function for calendarview, give back selected date
        // and also clears and then writes the listviews
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

        // database is opened
        dataSource = new TagebuchDataSource(this);
        dataSource.open();

        // listviews are written
        populateListViews();

        // buttons to add food for each field
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

        // functions for clicking an item in the listview
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

    // function to clear all listviews
    private void clearListViews(){
        fruehstueck.setAdapter(null);
        mittagessen.setAdapter(null);
        abendessen.setAdapter(null);
        snacks.setAdapter(null);
    }

    // function to fill all listviews
    private void populateListViews() {
        populateListView(R.id.ListViewBreakfastC, 1);
        populateListView(R.id.ListViewLunchC, 2);
        populateListView(R.id.ListViewDinnerC, 3);
        populateListView(R.id.ListViewSnacksC, 0);
    }


    // function to fill a single listview
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


    // function to open the activity meal
    private void openMealActivity(String date, int category) {
        Intent myIntent = new Intent(Calendar.this, AddMeal.class);
        myIntent.putExtra("date", date);
        myIntent.putExtra("category", category);
        myIntent.putExtra("fromMain", false);
        Calendar.this.startActivity(myIntent);
    }

    // function to open the activity editordeletemeal
    private void openEditOrDeleteMealActivity(String date, int category, int position){
        Intent myIntent = new Intent(Calendar.this, EditOrDeleteMeal.class);
        myIntent.putExtra("date", date);
        myIntent.putExtra("category", category);
        myIntent.putExtra("position", position);
        myIntent.putExtra("fromMain", false);
        Calendar.this.startActivity(myIntent);
    }


    // Function for back button to go back to the main activity
    private void goBack() {
        Intent myIntent = new Intent(Calendar.this, MainActivity.class);
        Calendar.this.startActivity(myIntent);
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
