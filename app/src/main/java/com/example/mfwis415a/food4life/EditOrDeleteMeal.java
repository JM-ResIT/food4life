package com.example.mfwis415a.food4life;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import database.TagebuchDataSource;
import database.TagebuchHelper;

public class EditOrDeleteMeal extends AppCompatActivity {

    // variables for this java class
    private TextView unit, calories;
    private EditText dateView;
    private Button deleteMeal, editMeal;
    private EditText amount;
    private String date = "";
    private String originAmount, originCalories;
    private int category;
    private Spinner categories, foods;
    private int menu_lm_id;
    private int meal_id;
    private int is_lm = 1;
    private boolean fromMain;
    private boolean spinnerAlreadyClicked = false;
    private DatePickerDialog.OnDateSetListener DateSetListener;

    private TagebuchDataSource dataSource;


    public static final String LOG_TAG = EditOrDeleteMeal.class.getSimpleName();

    // onCreate creates the Activity with the chosen Layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_meal);

        // database is opened
        dataSource = new TagebuchDataSource(this);
        dataSource.open();

        // intents are set
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        category = intent.getIntExtra("category", 0);
        int position = intent.getIntExtra("position", 0);
        meal_id = dataSource.getRealIdFromMeal(date, category, position);
        fromMain = intent.getBooleanExtra("fromMain", true);


        // textviews, buttons and edittexts are now referencing Id's
        dateView = (EditText) findViewById(R.id.MealDate);
        categories = (Spinner) findViewById(R.id.MealCategory);
        foods = (Spinner) findViewById(R.id.Foods);
        unit = (TextView) findViewById(R.id.MealUnit);
        calories = (TextView) findViewById(R.id.MealCalories);
        amount = (EditText) findViewById(R.id.MealAmount);
        deleteMeal = (Button) findViewById(R.id.deleteMeal);
        editMeal = (Button) findViewById(R.id.editMeal);

        // button to delete meals
        deleteMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSource.deleteMeal(meal_id);
                goBack();
            }
        });

        // button to open edit meals
        editMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMeal();
            }
        });


        // function to open the spinner for food
        foods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                menu_lm_id = dataSource.getRealIdFromLM(i);
                if(!spinnerAlreadyClicked){
                    setPreSelectedFood();
                } else {
                    setSelectedFood();
                }
                spinnerAlreadyClicked = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                deleteSelectedFood();
            }
        });

        // function to edit the amount
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

        // function for calendar
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                int year = cal.get(java.util.Calendar.YEAR);
                int month = cal.get(java.util.Calendar.MONTH);
                int day = cal.get(java.util.Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditOrDeleteMeal.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        // function to set a date with a specific format
        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                if (month < 10 && day < 10) {
                    date = "0" + day + "." + "0" + month + "." + year;
                }else if (day < 10 && month > 10) {
                    date = "0" + day + "." + month +"." + year;
                }else if (month < 10 && day > 10){
                    date = day + ".0" + month + "." + year;
                }else date = day + "." + month + "." + year;
                dateView.setText(date);
            }
        };


        loadData();

    }

    // function to change a meal
    private void changeMeal() {
        String foodAmountText = amount.getText().toString();
        int category = categories.getSelectedItemPosition();
        if (foodAmountText.length() > 0) {
            dataSource.editMealEntry(meal_id, is_lm, menu_lm_id, date, category, Integer.parseInt(calories.getText().toString()), Float.parseFloat(foodAmountText));
            goBack();
        } else {
            Log.d(LOG_TAG, "Please fill all text fields!");
        }
    }

    // function to change the calories if the food has been updated
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
        loadFoodsAndMenus();
    }

    //function to add preselected food
    private void setPreSelectedFood() {
        is_lm = 1;
        originAmount = dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_TBTABLE, TagebuchHelper.ANZAHL, TagebuchHelper.TAGEBUCHEINTRAG_ID, meal_id);
        String originUnit = dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.EINHEIT, TagebuchHelper.LEBENSMITTEL_ID, menu_lm_id);
        originCalories = dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_TBTABLE, TagebuchHelper.KALORIEN, TagebuchHelper.TAGEBUCHEINTRAG_ID, meal_id);

        amount.setText(originAmount);
        unit.setText(originUnit);
        calories.setText(originCalories);
    }

    // function to add food
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

    // function to deselect food
    private void deleteSelectedFood() {
        amount.setText("");
        unit.setText("");
        calories.setText("");
    }

    // function to load spinner data
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

    // function to load food and menus
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
        foods.setSelection(getIdPos());
    }

    // function to get position by Id
    private int getIdPos() {
        int menu_lm_id = Integer.parseInt(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_TBTABLE, TagebuchHelper.MENU_LM_ID, TagebuchHelper.TAGEBUCHEINTRAG_ID, meal_id));
        return dataSource.getPositionFromFood(menu_lm_id);
    }

    private void loadFoodUnitData() {

    }

    // Function for back button to go back to the previous activity
    private void goBack() {
        Intent goBack;
        goBack = fromMain ? new Intent(EditOrDeleteMeal.this, MainActivity.class) : new Intent(EditOrDeleteMeal.this, Calendar.class);
        EditOrDeleteMeal.this.startActivity(goBack);
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
