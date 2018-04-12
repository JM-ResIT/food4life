package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import database.TagebuchDataSource;

public class EditOrDeleteMenu extends AppCompatActivity {

    private ListView foods;
    private EditText menuTitel, menuDesc;
    private Button editMenu, deleteMenu;
    private TagebuchDataSource dataSource;

    public static final String LOG_TAG = AddMenu.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_menu);

        foods = (ListView) findViewById(R.id.ListViewMenu);
        menuTitel = (EditText) findViewById(R.id.MenuTitel);
        menuDesc = (EditText) findViewById(R.id.MenuDesc);

        dataSource = new TagebuchDataSource(this);
        dataSource.open();



        loadFoods();
    }

    private void addMenu() {
        String titel = menuTitel.getText().toString();
        String desc = menuDesc.getText().toString();
        List<Integer> items = getSelectedItems();

        if (titel.length() > 0 && desc.length() > 0 && items != null) {
            dataSource.addMenu(titel, desc, getSelectedItems());
            Intent myIntent = new Intent(EditOrDeleteMenu.this, MenuList.class);
            EditOrDeleteMenu.this.startActivity(myIntent);
        } else {
            Log.d(LOG_TAG, "Bitte alle Felder und mindestens eine Checkbox befüllen!");
        }

    }

    private List<Integer> getSelectedItems() {
        List<Integer> positions = new ArrayList<Integer>();
        SparseBooleanArray checked = foods.getCheckedItemPositions();

        for (int i = 0; i < foods.getAdapter().getCount(); i++) {
            if (checked.get(i)) {
                positions.add(i);
            }
        }

        return positions;
    }

    private void loadFoods() {
        List<String> lables = dataSource.getAllFoods();

        if (!lables.isEmpty()) {

            foods.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_multiple_choice, lables));

            foods.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            foods.setItemsCanFocus(false);
        }


    }

    private void goBack() {
        Intent myIntent = new Intent(EditOrDeleteMenu.this, MenuList.class);
        EditOrDeleteMenu.this.startActivity(myIntent);
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
