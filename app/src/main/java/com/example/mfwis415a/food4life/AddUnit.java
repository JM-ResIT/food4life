package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import database.TagebuchDataSource;

public class AddUnit extends AppCompatActivity {

    private TagebuchDataSource dataSource;


    public static final String LOG_TAG = AddUnit.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit);
        dataSource = new TagebuchDataSource(this);

        Button addUnit = (Button) findViewById(R.id.UnitAdd);

        dataSource.open();

        addUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUnit();
            }
        });
    }

    private void addUnit(){
        EditText unitName = (EditText) findViewById(R.id.UnitName);

        String unitNameText = unitName.getText().toString();

        if (unitNameText.length() > 0{
            dataSource.addFoodEntry(unitNameText);

            Intent myIntent = new Intent(AddUnit.this, UnitList.class);
            AddUnit.this.startActivity(myIntent);
        } else {
            Log.d(LOG_TAG, "Please fill all text fields!");
        }
    }

    private void goBack() {
        Intent myIntent = new Intent(AddUnit.this, UnitList.class);
        AddUnit.this.startActivity(myIntent);
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
