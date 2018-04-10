package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import database.TagebuchDataSource;

public class EditOrDeleteUnit extends AppCompatActivity {

    private TagebuchDataSource dataSource;
    private EditText UnitName;

    private static final String LOG_TAG = EditOrDeleteUnit.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_unit);

        Button deleteUnit = (Button) findViewById(R.id.deleteUnit;
        Button editUnit = (Button) findViewById(R.id.editUnit);

        dataSource = new TagebuchDataSource(this);

        Intent mIntent = getIntent();
        int position = mIntent.getIntExtra("position", 0);

        dataSource.open();
        lm_id = dataSource.getRealIdFromLM(position);

        loadUnitData();

        deleteUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSource.updateStatusOfLM(lm_id, 0);
                Intent myIntent = new Intent(EditOrDeleteUnit.this, UnitList.class);
                EditOrDeleteUnit.this.startActivity(myIntent);
            }
        });

        editUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUnit();
            }
        });
    }

    private void goBack() {
        Intent myIntent = new Intent(EditOrDeleteUnit.this, UnitList.class);
        EditOrDeleteUnit.this.startActivity(myIntent);
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
