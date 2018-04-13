package com.example.mfwis415a.food4life.addfood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mfwis415a.food4life.R;
import com.example.mfwis415a.food4life.foodlist.FoodList;

public class AddFood extends AppCompatActivity {

    // variables for this java class
    public static final String LOG_TAG = AddFood.class.getSimpleName();
    private Spinner units;
    private ApplicationLogic mApplicationLogic;

    // onCreate creates the Activity with the chosen Layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        // Spinner is now referencing Id
        units = (Spinner) findViewById(R.id.foodUnit);

        // Button to add food to database
        Button addFood = (Button) findViewById(R.id.addFood);

        initApplicationLogic();

        // Button for addFood
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApplicationLogic.addFood(units);
            }
        });
    }

    private void initApplicationLogic () {
        mApplicationLogic = new ApplicationLogic(this);
    }


    // Function for back button to go back to the foodlist
    private void goBack() {
        Intent myIntent = new Intent(AddFood.this, FoodList.class);
        AddFood.this.startActivity(myIntent);
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
