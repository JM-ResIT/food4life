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

import java.util.List;

import database.TagebuchDataSource;
import database.TagebuchHelper;

public class EditOrDeleteUnit extends AppCompatActivity {

    // variables for this java class
    private TagebuchDataSource dataSource;
    private EditText UnitName;
    private Integer unit_id;

    private static final String LOG_TAG = EditOrDeleteUnit.class.getSimpleName();

    // onCreate creates the Activity with the chosen Layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_unit);

        // buttons and edittext are now referencing Id's
        Button deleteUnit = (Button) findViewById(R.id.deleteUnit);
        Button editUnit = (Button) findViewById(R.id.editUnit);
        UnitName = (EditText) findViewById(R.id.UnitName);

        // database is implemented
        dataSource = new TagebuchDataSource(this);

        // intents are set
        Intent mIntent = getIntent();
        int position = mIntent.getIntExtra("position", 0);

        // database is opened
        dataSource.open();
        unit_id = dataSource.getRealIdFromUnit(position);

        // function to load unit data is used
        loadUnitData();

        // function to delete unit is used
       deleteUnit.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) { dataSource.updateStatusOfUnit(unit_id, 0);
                Intent myIntent = new Intent(EditOrDeleteUnit.this, UnitList.class);
                EditOrDeleteUnit.this.startActivity(myIntent);
            }
        });

       // function to edit unit is used
        editUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUnit();
            }
        });
    }
    // function to edit unit
    private void editUnit(){
        String unit = UnitName.getText().toString();

        if(unit.length() > 0 ) {
            dataSource.editUnitEntry(unit, unit_id);

            Intent myIntent = new Intent(EditOrDeleteUnit.this, UnitList.class);
            EditOrDeleteUnit.this.startActivity(myIntent);
        } else {
            Log.d(LOG_TAG, "Please fill all text fields!");
        }
    }

    // function to loadunit
    private void loadUnitData() {

        UnitName.setText(dataSource.getEntryFromDBTable(TagebuchHelper.DATABASE_EINTABLE, TagebuchHelper.TITEL, TagebuchHelper.EINHEIT_ID, unit_id));

    }

    // Function for back button to go back to the previous activity
    private void goBack() {
        Intent myIntent = new Intent(EditOrDeleteUnit.this, UnitList.class);
        EditOrDeleteUnit.this.startActivity(myIntent);
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
