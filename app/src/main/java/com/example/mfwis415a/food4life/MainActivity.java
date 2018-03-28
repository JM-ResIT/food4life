package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import database.TagebuchDataSource;

// List View: http://www.programmierenlernenhq.de/tutorial-android-listview-verwenden/

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Button addMeal;
    private Button foodList;
    private TagebuchDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new TagebuchDataSource(this);

       /* addMeal = (Button) findViewById(R.id.Hinzufuegen);
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");

    }
}
