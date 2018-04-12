package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import database.TagebuchDataSource;

public class MenuList extends AppCompatActivity {

    private Button addMenu;
    private ListView menuList;

    private TagebuchDataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        addMenu = (Button) findViewById(R.id.addMenu);
        menuList = (ListView) findViewById(R.id.MenuList);

        dataSource = new TagebuchDataSource(this);
        dataSource.open();

        loadMenus();
        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MenuList.this, AddMenu.class);
                MenuList.this.startActivity(myIntent);
            }
        });

    }

    private void loadMenus() {
        List<String> lables = dataSource.getAllMenus();

        if (!lables.isEmpty()) {

            menuList.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, lables));
        }

    }


    private void goBack() {
        Intent myIntent = new Intent(MenuList.this, MainActivity.class);
        MenuList.this.startActivity(myIntent);
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
