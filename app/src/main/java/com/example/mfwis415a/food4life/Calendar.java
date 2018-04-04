package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mfwis415a.food4life.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import database.TagebuchDataSource;
import database.TagebuchHelper;




public class Calendar extends AppCompatActivity {

    public static final String TAG = "Calendaractivity";

    private CalendarView mCalendarView;
    private TagebuchDataSource dataSource;
    private Button addBreakfastC;
    private Button addDinnerC;
    private Button addLunchC;
    private Button addSnacksC;
    private ListView fruehstueck;
    private ListView mittagessen;
    private ListView abendessen;
    private ListView snacks;
    private SimpleDateFormat showDate;
    private String dateC;
    private TextView tv;
    private static final String LOG_TAG = TagebuchHelper.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //populatelistview(); // Listview Method for Startscreen

        mCalendarView = (CalendarView) findViewById(R.id.calendarView3);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                dateC = i2 + "." + (i1 + 1) + "." + i;
                Log.d(TAG, "onSelectedDayChange: date: " + dateC);

            }
        });

        /*tv = findViewById(R.id.Date);
        SimpleDateFormat showDate = new SimpleDateFormat("dd.MM.yyyy");
        final String dateString = showDate.format(dateC);
        tv.setText(dateString);*/

        dataSource = new TagebuchDataSource(this);

        addBreakfastC = (Button) findViewById(R.id.AddBreakfastC);
        addLunchC = (Button) findViewById(R.id.AddLunchC);
        addDinnerC = (Button) findViewById(R.id.AddDinnerC);
        addSnacksC = (Button) findViewById(R.id.AddSnacksC);

        fruehstueck = (ListView) findViewById(R.id.ListViewBreakfastC);
        mittagessen = (ListView) findViewById(R.id.ListViewLunchC);
        abendessen= (ListView) findViewById(R.id.ListViewDinnerC);
        snacks = (ListView) findViewById(R.id.ListViewSnacksC);

        dataSource.open();

        loadFoods();

        /*addBreakfastC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Calendar.this, AddFood.class);
                // myIntent.putExtra("key", "test"); //Optional parameters
                Calendar.this.startActivity(myIntent);

            }
        });*/
        addBreakfastC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFoodAcivity(dateC, 1);
            }
        });
        addLunchC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFoodAcivity(dateC, 2);
            }
        });
        addDinnerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFoodAcivity(dateC, 3);

            }
        });
        addSnacksC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFoodAcivity(dateC, 0);

            }
        });
        /*addLunchC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Calendar.this, AddFood.class);
                // myIntent.putExtra("key", "test"); //Optional parameters
                Calendar.this.startActivity(myIntent);

            }
        });
        addDinnerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Calendar.this, AddFood.class);
                // myIntent.putExtra("key", "test"); //Optional parameters
                Calendar.this.startActivity(myIntent);

            }
        });
        addSnacksC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Calendar.this, AddFood.class);
                // myIntent.putExtra("key", "test"); //Optional parameters
                Calendar.this.startActivity(myIntent);

            }
        });*/

        fruehstueck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Selected Item: " + fruehstueck.getItemAtPosition(position));
                Intent myIntent = new Intent(Calendar.this, EditOrDeleteFood.class);
                // myIntent.putExtra("key", "test"); //Optional parameters
                Calendar.this.startActivity(myIntent);
            }
        });
        mittagessen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Selected Item: " + mittagessen.getItemAtPosition(position));
                Intent myIntent = new Intent(Calendar.this, EditOrDeleteFood.class);
                // myIntent.putExtra("key", "test"); //Optional parameters
                Calendar.this.startActivity(myIntent);
            }
        });
        abendessen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Selected Item: " + abendessen.getItemAtPosition(position));
                Intent myIntent = new Intent(Calendar.this, EditOrDeleteFood.class);
                // myIntent.putExtra("key", "test"); //Optional parameters
                Calendar.this.startActivity(myIntent);
            }
        });
        snacks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Selected Item: " + snacks.getItemAtPosition(position));
                Intent myIntent = new Intent(Calendar.this, EditOrDeleteFood.class);
                // myIntent.putExtra("key", "test"); //Optional parameters
                Calendar.this.startActivity(myIntent);
            }
        });


        //dataSource.insertSampleDataIfEmpty();




    }




    public void loadFoods() {
        List<String> lables = dataSource.getAllFoods();

        if (!lables.isEmpty()) {
            // Get a handle to the list view
            ListView f = (ListView) findViewById(R.id.ListViewBreakfastC);
            ListView m = (ListView) findViewById(R.id.ListViewLunchC);
            ListView a = (ListView) findViewById(R.id.ListViewDinnerC);
            ListView s = (ListView) findViewById(R.id.ListViewSnacksC);

            f.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, lables));
            m.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, lables));
            a.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, lables));
            s.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, lables));
        }

    }

    /*private void populatelistview() {
        //Create list of items
        String[] myItems ={"Banane 150 kcal","Apfel", "Müsli", "Knäckebrot", "Toast", "Salami", "Käse"};


        // Get a handle to the list view
        ListView list = (ListView) findViewById(R.id.ListViewBreakfast);

        list.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, myItems));
    }*/

    private void openFoodAcivity(String date, int category){
        Intent myIntent = new Intent(Calendar.this, AddMeal.class);
        myIntent.putExtra("date", date);
        myIntent.putExtra("category", category);
        Calendar.this.startActivity(myIntent);
    }
}
