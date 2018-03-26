package com.example.mfwis415a.food4life.StartScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mfwis415a.food4life.AddMeal.AddMeal;
import com.example.mfwis415a.food4life.R;

import database.TagebuchDataSource;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Button addMeal;
    private TagebuchDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addMeal = (Button) findViewById(R.id.Hinzufuegen);

        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, AddMeal.class);
                myIntent.putExtra("key", "test"); //Optional parameters
                MainActivity.this.startActivity(myIntent);

            }
        });

        dataSource = new TagebuchDataSource(this);

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        //add db data for testing
        dataSource.addTagebuchEintrag();

        dataSource.listAllFromTagebuchEintrag();

        dataSource.close();

    }

    // now just for testing
    private void addTagebucheintrag(){

    }



}
