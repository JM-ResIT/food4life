package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import database.TagebuchDataSource;
import database.TagebuchHelper;

public class EditOrDeleteFood extends AppCompatActivity {

    private TagebuchDataSource dataSource;
    private Integer lm_id;
    private TextView unit;
    private EditText foodName, foodAmount, equivalent, foodDescription;

    private static final String LOG_TAG = EditOrDeleteFood.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_food);

        Button deleteFood = (Button) findViewById(R.id.deleteFood);
        Button editFood = (Button) findViewById(R.id.editFood);
        foodName = (EditText) findViewById(R.id.foodName);
        foodAmount = (EditText) findViewById(R.id.foodAmount);
        equivalent = (EditText) findViewById(R.id.equivalent);
        foodDescription = (EditText) findViewById(R.id.foodDescription);
        unit = (TextView) findViewById(R.id.foodUnit);

        dataSource = new TagebuchDataSource(this);

        Intent mIntent = getIntent();
        int position = mIntent.getIntExtra("position", 0);

        dataSource.open();
        lm_id = dataSource.getRealIdFromLM(position);

        loadFoodData();

        deleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSource.updateStatusOfLM(lm_id, 0);
                Intent myIntent = new Intent(EditOrDeleteFood.this, FoodList.class);
                EditOrDeleteFood.this.startActivity(myIntent);
            }
        });

        editFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFood();
            }
        });
    }

    private void editFood() {
        String foodNameText = foodName.getText().toString();
        String foodDescriptionText = foodDescription.getText().toString();
        String foodAmountText = foodAmount.getText().toString();
        String equivalentText = equivalent.getText().toString();
        String unitText = unit.getText().toString();

        if (foodNameText.length() > 0 && foodDescriptionText.length() > 0 && foodAmountText.length() > 0 && unitText.length() > 0 && equivalentText.length() > 0) {
            dataSource.editFoodEntry(foodNameText, foodDescriptionText, Float.parseFloat(foodAmountText), unitText, Integer.parseInt(equivalentText), lm_id);

            Intent myIntent = new Intent(EditOrDeleteFood.this, FoodList.class);
            EditOrDeleteFood.this.startActivity(myIntent);
        } else {
            Log.d(LOG_TAG, "Please fill all text fields!");
        }
    }

    //set preselected values
    private void loadFoodData() {
        unit.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.EINHEIT, TagebuchHelper.LEBENSMITTEL_ID, lm_id));
        foodName.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_LMTABLE, TagebuchHelper.TITEL, TagebuchHelper.LEBENSMITTEL_ID, lm_id));
        foodDescription.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_LMTABLE, TagebuchHelper.BESCHREIBUNG, TagebuchHelper.LEBENSMITTEL_ID, lm_id));
        foodAmount.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.ANZAHL, TagebuchHelper.LEBENSMITTEL_ID, lm_id));
        equivalent.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.ENTSPRECHUNG, TagebuchHelper.LEBENSMITTEL_ID, lm_id));
    }

    private void goBack() {
        Intent myIntent = new Intent(EditOrDeleteFood.this, FoodList.class);
        EditOrDeleteFood.this.startActivity(myIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
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
}
