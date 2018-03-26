package com.example.mfwis415a.food4life.FoodList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mfwis415a.food4life.AddFood.AddFood;
import com.example.mfwis415a.food4life.AddMeal.AddMeal;
import com.example.mfwis415a.food4life.R;
import com.example.mfwis415a.food4life.StartScreen.MainActivity;

public class FoodList extends AppCompatActivity {

    private Button addFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        addFood = (Button) findViewById(R.id.addFood);

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(FoodList.this, AddFood.class);
                myIntent.putExtra("key", "test"); //Optional parameters
                FoodList.this.startActivity(myIntent);

            }
        });
    }
}
