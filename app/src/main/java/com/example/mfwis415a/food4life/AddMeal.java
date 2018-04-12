package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import database.TagebuchDataSource;
import database.TagebuchHelper;

public class AddMeal extends AppCompatActivity {

    // variables for this java class
    private TextView dateView, unit, calories;
    private EditText amount;
    private String date = "";
    private String originAmount, originCalories;
    private int category;
    private Spinner categories, foods, menus;
    private int menu_lm_id;
    private int is_lm = 1;
    private boolean fromMain;
    private int placeholderPosFood, placeholderPosMenu;

    private TagebuchDataSource dataSource;


    public static final String LOG_TAG = AddMeal.class.getSimpleName();

    // onCreate creates the Activity with the chosen Layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        // Database is opened
        dataSource = new TagebuchDataSource(this);

        // Intents are declared
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        category = intent.getIntExtra("category", 0);
        fromMain = intent.getBooleanExtra("fromMain", true);


        // Textviews Buttons and Spinners are now referencing Id's
        dateView = (TextView) findViewById(R.id.MealDate);
        categories = (Spinner) findViewById(R.id.MealCategory);
        foods = (Spinner) findViewById(R.id.Foods);
        menus = (Spinner) findViewById(R.id.Menus);
        unit = (TextView) findViewById(R.id.MealUnit);
        calories = (TextView) findViewById(R.id.MealCalories);
        amount = (EditText) findViewById(R.id.MealAmount);
        Button addMeal = (Button) findViewById(R.id.MealAdd);

        // Database is opened
        dataSource.open();

        // listener function for selected items
        foods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                if (placeholderPosFood != i) {
                    menu_lm_id = dataSource.getRealIdFromLM(i);
                    setSelectedFood();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                deleteSelectedMenu();
            }
        });

        // listener function for selected items
        menus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                if (placeholderPosMenu != i) {
                    menu_lm_id = dataSource.getRealIdFromMenu(i);
                    setSelectedMenu();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                deleteSelectedFood();
            }
        });


        // Listener Function for changed text
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

        // Button function to add a meal
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMeal();
            }
        });


        // function to load data
        loadData();

    }

    // function to add meal
    private void addMeal() {
        String foodAmountText = amount.getText().toString();
        int category = categories.getSelectedItemPosition();
        if (foodAmountText.length() > 0) {
            dataSource.addMealEntry(is_lm, menu_lm_id, date, category, Integer.parseInt(calories.getText().toString()), Float.parseFloat(foodAmountText));
            goBack();
        } else {
            Log.d(LOG_TAG, "Please fill all text fields!");
        }
    }

    // function for changed calories
    private void changeCaloriesOnUpdate(String text) {
        float currentAmount = Float.parseFloat(text);
        float relativeValuePerAmount = Integer.parseInt(originCalories) / Float.parseFloat(originAmount);
        float newCalories = currentAmount * relativeValuePerAmount;
        calories.setText(String.valueOf(Math.round(newCalories)));
    }

    // function to load data
    private void loadData() {
        dateView.setText(date);
        loadCategorySpinnerData();
        loadFoods();
        loadMenus();
    }

    // function for selected foods to be set from database
    private void setSelectedFood() {
        menus.setSelection(placeholderPosMenu);
        is_lm = 1;
        originAmount = dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.ANZAHL, TagebuchHelper.LEBENSMITTEL_ID, menu_lm_id);
        String originUnit = dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.EINHEIT, TagebuchHelper.LEBENSMITTEL_ID, menu_lm_id);
        originCalories = dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.ENTSPRECHUNG, TagebuchHelper.LEBENSMITTEL_ID, menu_lm_id);

        amount.setText(originAmount);
        unit.setText(originUnit);
        calories.setText(originCalories);
    }

    // function for selected menus from database
    private void setSelectedMenu() {
        foods.setSelection(placeholderPosFood);
        is_lm = 0;

        originAmount = "1";
        String originUnit = "Stück";
        originCalories = String.valueOf(dataSource.getCaloriesFromMenu(menu_lm_id));

        amount.setText(originAmount);
        unit.setText(originUnit);
        calories.setText(originCalories);
    }

    // function for fields of selected foods to be cleared
    private void deleteSelectedFood() {
        amount.setText("");
        unit.setText("");
        calories.setText("");
    }

    // function for fields of selected foods to be cleared
    private void deleteSelectedMenu() {
        amount.setText("");
        unit.setText("");
        calories.setText("");
    }


    // function for spinner data categories
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

    // function to load food and menu
    private void loadFoods() {
        // Spinner Drop down elements#
        List<String> labels = dataSource.getActiveFoods();
        labels.add(" "); //empty placeholder

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        foods.setAdapter(dataAdapter);

        placeholderPosFood = dataAdapter.getPosition(" ");

    }

    private void loadMenus() {
        // Spinner Drop down elements#
        List<String> labels = dataSource.getActiveMenus();
        labels.add(" "); //empty placeholder

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        menus.setAdapter(dataAdapter);

        placeholderPosMenu = dataAdapter.getPosition(" ");
        menus.setSelection(placeholderPosMenu);


    }

    // Function for back button to go back to the Meallist
    private void goBack() {
        Intent goBack;
        goBack = fromMain ? new Intent(AddMeal.this, MainActivity.class) : new Intent(AddMeal.this, Calendar.class);
        AddMeal.this.startActivity(goBack);
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
