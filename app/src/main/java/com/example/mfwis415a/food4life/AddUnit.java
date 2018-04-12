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

    // variables for this java class
    private TagebuchDataSource dataSource;
    public static final String LOG_TAG = AddUnit.class.getSimpleName();

    // onCreate creates the Activity with the chosen Layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit);

        // database function is used
        dataSource = new TagebuchDataSource(this);

        // button to add unit is now referencing Id
        Button addUnit = (Button) findViewById(R.id.UnitAdd);

        // database is opened and
        dataSource.open();

        // button to add unit is used
        addUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUnit();
            }
        });
    }

    // function to add a unit to database
    private void addUnit(){
        EditText unitName = (EditText) findViewById(R.id.UnitName);

        String unitNameText = unitName.getText().toString();

        if (unitNameText.length() > 0) {
            dataSource.addUnitEntry(unitNameText);

            Intent myIntent = new Intent(AddUnit.this, UnitList.class);
            AddUnit.this.startActivity(myIntent);
        } else {
            Log.d(LOG_TAG, "Please fill all text fields!");
        }
    }

    // Function for back button to go back to the unitlist
    private void goBack() {
        Intent myIntent = new Intent(AddUnit.this, UnitList.class);
        AddUnit.this.startActivity(myIntent);
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

    // Resume database
    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    // Pause database
    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }
}
