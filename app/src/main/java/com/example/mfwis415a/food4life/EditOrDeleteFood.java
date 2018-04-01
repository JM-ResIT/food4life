package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import database.TagebuchDataSource;
import database.TagebuchHelper;

public class EditOrDeleteFood extends AppCompatActivity {

    private static final String LOG_TAG = TagebuchHelper.class.getSimpleName();

    private TagebuchDataSource dataSource;
    private Button deleteFood;
    private Integer lm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_food);

        dataSource = new TagebuchDataSource(this);

        deleteFood = (Button) findViewById(R.id.deleteFood);

        Intent mIntent = getIntent();
        int position = mIntent.getIntExtra("position", 0);

        dataSource.open();

        lm_id = dataSource.getRealIdFromLM(position);

        deleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(EditOrDeleteFood.this, FoodList.class);
                EditOrDeleteFood.this.startActivity(myIntent);

                //TODO change is_active to 0 where lmid = lm_id

            }
        });
    }

}
