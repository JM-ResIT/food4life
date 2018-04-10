package com.example.mfwis415a.food4life;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mfwis415a.food4life.R;

import database.TagebuchDataSource;
import database.TagebuchHelper;

public class Profile extends AppCompatActivity {

    private TagebuchDataSource dataSource;
    private EditText limit;
    private Button updateProfile;

    private static final String LOG_TAG = TagebuchHelper.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dataSource = new TagebuchDataSource(this);
        limit = (EditText) findViewById(R.id.ProfileCalories);
        updateProfile = (Button) findViewById(R.id.UpdateProfile);

        dataSource.open();

        loadData();

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String limitText = limit.getText().toString();
                dataSource.updateProfile(Integer.parseInt(limitText));
                Intent myIntent = new Intent(Profile.this, MainActivity.class);
                Profile.this.startActivity(myIntent);
            }
        });
    }

    public void loadData(){
        limit.setText(String.valueOf(dataSource.getLimitFromProfile()));
    }

    private void goToMain() {
        Intent myIntent = new Intent(Profile.this, MainActivity.class);
        Profile.this.startActivity(myIntent);
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
