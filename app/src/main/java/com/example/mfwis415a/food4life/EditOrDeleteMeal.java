package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import database.TagebuchDataSource;
import database.TagebuchHelper;

public class EditOrDeleteMeal extends AppCompatActivity {

    private TextView dateView, unit, calories;
    private EditText amount;
    private String date = "";
    private String originAmount, originCalories;
    private int category;
    private Spinner categories, foods;
    private int menu_lm_id;
    private int meal_id;
    private int is_lm = 1;
    private boolean fromMain;

    private TagebuchDataSource dataSource;


    public static final String LOG_TAG = EditOrDeleteMeal.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_meal);

        dataSource = new TagebuchDataSource(this);
        dataSource.open();
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        category = intent.getIntExtra("category", 0);
        int position = intent.getIntExtra("position", 0);
        meal_id = dataSource.getRealIdFromMeal(date, category, position);
        fromMain = intent.getBooleanExtra("fromMain", true);


        dateView = (TextView) findViewById(R.id.MealDate);
        categories = (Spinner) findViewById(R.id.MealCategory);
        foods = (Spinner) findViewById(R.id.Foods);
        unit = (TextView) findViewById(R.id.MealUnit);
        calories = (TextView) findViewById(R.id.MealCalories);
        amount = (EditText) findViewById(R.id.MealAmount);



        foods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                menu_lm_id = dataSource.getRealIdFromLM(i);
                setSelectedFood();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                deleteSelectedFood();
            }
        });

        amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() > 0) {
                    changeCaloriesOnUpdate(s.toString());
                } else {
                    calories.setText("");
                }
            }
        });


        loadData();

    }

    private void changeMeal() {
        String foodAmountText = amount.getText().toString();
        int category = categories.getSelectedItemPosition();
        if (foodAmountText.length() > 0) {
            dataSource.addMealEntry(is_lm, menu_lm_id, date, category, Integer.parseInt(calories.getText().toString()));
            goBack();
        } else {
            Log.d(LOG_TAG, "Please fill all text fields!");
        }
    }

    private void changeCaloriesOnUpdate(String text) {
        float currentAmount = Float.parseFloat(text);
        float relativeValuePerAmount = Integer.parseInt(originCalories) / Float.parseFloat(originAmount);
        float newCalories = currentAmount * relativeValuePerAmount;
        calories.setText(String.valueOf(Math.round(newCalories)));
    }

    private void loadData() {
        dateView.setText(date);
        loadCategorySpinnerData();
        loadFoodsAndMenus();
    }

    private void setSelectedFood() {
        is_lm = 1;
        originAmount = dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.ANZAHL, TagebuchHelper.LEBENSMITTEL_ID, menu_lm_id);
        String originUnit = dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.EINHEIT, TagebuchHelper.LEBENSMITTEL_ID, menu_lm_id);
        originCalories = dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.ENTSPRECHUNG, TagebuchHelper.LEBENSMITTEL_ID, menu_lm_id);

        amount.setText(originAmount);
        unit.setText(originUnit);
        calories.setText(originCalories);
    }

    private void setSelectedMenu() {
        is_lm = 0;
    }

    private void deleteSelectedFood() {
        amount.setText("");
        unit.setText("");
        calories.setText("");
    }

    private void loadCategorySpinnerData() {
        // Spinner Drop down elements#
        List<String> labels = new ArrayList<String>();

        labels.add("Snack");
        labels.add("Frühstück");
        labels.add("Mittagessen");
        labels.add("Abendessen");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        categories.setAdapter(dataAdapter);

        categories.setSelection(category);
    }

    private void loadFoodsAndMenus() {
        // Spinner Drop down elements#
        List<String> labels = dataSource.getAllFoods();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        foods.setAdapter(dataAdapter);

    }

    private void loadFoodUnitData() {

    }

    private void goBack() {
        Intent goBack;
        goBack = fromMain ? new Intent(EditOrDeleteMeal.this, MainActivity.class) : new Intent(EditOrDeleteMeal.this, Calendar.class);
        EditOrDeleteMeal.this.startActivity(goBack);
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
