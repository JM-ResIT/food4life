package com.example.mfwis415a.food4life.menulist;

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
import com.example.mfwis415a.food4life.addmenu.AddMenu;
import com.example.mfwis415a.food4life.editordeletemenu.EditOrDeleteMenu;
import com.example.mfwis415a.food4life.main.MainActivity;

import java.util.List;

import common.TagebuchDataSource;

public class MenuList extends AppCompatActivity {

    // variables for this java class
    private Button addMenu;
    private ListView menuList;
    private TagebuchDataSource dataSource;

    // onCreate creates the Activity with the chosen Layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        // listviews and buttons are now referencing Id's
        addMenu = (Button) findViewById(R.id.addMenu);
        menuList = (ListView) findViewById(R.id.MenuList);

        // database is opened
        dataSource = new TagebuchDataSource(this);
        dataSource.open();

        // loadmenus function is used
        loadMenus();

        // click on item function is used
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(MenuList.this, EditOrDeleteMenu.class);
                myIntent.putExtra("position", position);
                MenuList.this.startActivity(myIntent);
            }
        });


        // function to add menu is used
        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MenuList.this, AddMenu.class);
                MenuList.this.startActivity(myIntent);
            }
        });

    }

    // function to loas menus
    private void loadMenus() {
        List<String> lables = dataSource.getActiveMenus();

        if (!lables.isEmpty()) {

            menuList.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, lables));
        }

    }

    // function to get position by Id
    private void goBack() {
        Intent myIntent = new Intent(MenuList.this, MainActivity.class);
        MenuList.this.startActivity(myIntent);
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
