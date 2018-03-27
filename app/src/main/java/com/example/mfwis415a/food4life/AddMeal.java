package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mfwis415a.food4life.R;

public class AddMeal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.
    }

}
