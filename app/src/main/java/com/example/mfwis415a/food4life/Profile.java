package com.example.mfwis415a.food4life;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;



import database.TagebuchDataSource;
import database.TagebuchHelper;

public class Profile extends AppCompatActivity {

    private TagebuchDataSource dataSource;
    private EditText limit;
    private Button updateProfile, statistics, unitList;
    private EditText displayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    private String date;

    private static final String LOG_TAG = TagebuchHelper.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dataSource = new TagebuchDataSource(this);
        limit = (EditText) findViewById(R.id.ProfileCalories);
        updateProfile = (Button) findViewById(R.id.UpdateProfile);
        statistics = (Button) findViewById(R.id.goToStatistics);
        unitList = (Button) findViewById(R.id.goToUnitList);

        dataSource.open();

        loadData();

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Profile.this, Statistics.class);
                Profile.this.startActivity(myIntent);
            }
        });

        unitList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Profile.this, UnitList.class);
                Profile.this.startActivity(myIntent);
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String limitText = limit.getText().toString();
                dataSource.updateProfile(Integer.parseInt(limitText));
                Intent myIntent = new Intent(Profile.this, MainActivity.class);
                Profile.this.startActivity(myIntent);
            }
        });

        displayDate = findViewById(R.id.ProfileBday);

        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Profile.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

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

                displayDate.setText(date);
            }
        };
            }

    public void loadData(){
        limit.setText(String.valueOf(dataSource.getLimitFromProfile()));
    }

    private void goBack() {
        Intent myIntent = new Intent(Profile.this, MainActivity.class);
        Profile.this.startActivity(myIntent);
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
