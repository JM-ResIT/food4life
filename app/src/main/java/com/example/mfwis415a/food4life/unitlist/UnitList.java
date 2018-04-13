package com.example.mfwis415a.food4life.unitlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.mfwis415a.food4life.R;
import com.example.mfwis415a.food4life.addunit.AddUnit;
import com.example.mfwis415a.food4life.editordeleteunit.EditOrDeleteUnit;
import com.example.mfwis415a.food4life.foodlist.FoodList;
import com.example.mfwis415a.food4life.profile.Profile;

import java.util.List;

import common.TagebuchDataSource;

public class UnitList extends AppCompatActivity {

    // variables for this java class
    private TagebuchDataSource dataSource;

    private static final String LOG_TAG = FoodList.class.getSimpleName();

    // onCreate creates the Activity with the chosen Layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_list);

        // database is implemented
        dataSource = new TagebuchDataSource(this);

        // listview and button is now referencing Id's
        Button addUnit = (Button) findViewById(R.id.AddUnit);
        ListView unitList = (ListView) findViewById(R.id.UnitList);

        // database is opened
        dataSource.open();

        // function to load units is used
        loadUnits();

        // function to go to add unit is used
        addUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(UnitList.this, AddUnit.class);
                UnitList.this.startActivity(myIntent);

            }
        });

        // function for a click on an item is used
        unitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(UnitList.this, EditOrDeleteUnit.class);
                myIntent.putExtra("position", position); //Optional parameters
                UnitList.this.startActivity(myIntent);
            }
        });
    }

    // function to load units
    public void loadUnits() {
        List<String> lables = dataSource.getActiveUnits();

        if (!lables.isEmpty()) {
            // Get a handle to the list view
            ListView lv = (ListView) findViewById(R.id.UnitList);

            lv.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, lables));
        }

    }


    // Function for back button to go back to the previous activity
    private void goBack() {
        Intent myIntent = new Intent(UnitList.this, Profile.class);
        UnitList.this.startActivity(myIntent);
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
