package com.example.mfwis415a.food4life.statistics;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.mfwis415a.food4life.R;
import com.example.mfwis415a.food4life.profile.Profile;

import common.TagebuchDataSource;

public class Statistics extends AppCompatActivity {

    private TextView first, second, third;
    private TagebuchDataSource dataSource;
    private String dateString;

    // onCreate creates the Activity with the chosen Layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        first = (TextView) findViewById(R.id.ersterDurchschnitt); // 7 days
        second = (TextView) findViewById(R.id.zweiterDurchschnitt); // 14 days
        third = (TextView) findViewById(R.id.dritterDurchschnitt); // 30 days

        dataSource = new TagebuchDataSource(this);
        dataSource.open();

        setValues();
    }

    @SuppressLint("SetTextI18n")
    private void setValues(){
        first.setText(String.valueOf(dataSource.getAverageCalories(7)) + " kcal");
        second.setText(String.valueOf(dataSource.getAverageCalories(14)) + " kcal");
        third.setText(String.valueOf(dataSource.getAverageCalories(30)) + " kcal");
    }

    // Function for back button to go back to the previous activity
    private void goBack() {
        Intent myIntent = new Intent(Statistics.this, Profile.class);
        Statistics.this.startActivity(myIntent);
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
