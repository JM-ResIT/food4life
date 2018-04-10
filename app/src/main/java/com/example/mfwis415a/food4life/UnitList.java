package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import database.TagebuchDataSource;

public class UnitList extends AppCompatActivity {

    private TagebuchDataSource dataSource;

    private static final String LOG_TAG = FoodList.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_list);

        dataSource = new TagebuchDataSource(this);
        Button addUnit = (Button) findViewById(R.id.AddUnit);
        ListView unitList = (ListView) findViewById(R.id.UnitList);

        dataSource.open();

        loadUnits();

        addUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(UnitList.this, AddUnit.class);
                UnitList.this.startActivity(myIntent);

            }
        });

        unitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(UnitList.this, EditOrDeleteUnit.class);
                myIntent.putExtra("position", position); //Optional parameters
                UnitList.this.startActivity(myIntent);
            }
        });
    }

    public void loadUnits() {
        List<String> lables = dataSource.getAllUnits();

        if (!lables.isEmpty()) {
            // Get a handle to the list view
            ListView lv = (ListView) findViewById(R.id.UnitList);

            lv.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, lables));
        }

    }


    private void goBack() {
        Intent myIntent = new Intent(UnitList.this, MainActivity.class);
        UnitList.this.startActivity(myIntent);
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
