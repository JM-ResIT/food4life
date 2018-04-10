package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

public class EditOrDeleteMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_menu);
    }

    private void goToMain() {
        Intent myIntent = new Intent(EditOrDeleteMenu.this, MainActivity.class);
        EditOrDeleteMenu.this.startActivity(myIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            goToMain();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
