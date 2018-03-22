package com.example.mfwis415a.food4life;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private TagebuchDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new TagebuchDataSource(this);

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        //add db data for testing
        dataSource.addTagebuchEintrag();

        dataSource.listAllFromTagebuchEintrag();

        dataSource.close();

    }

    // now just for testing
    private void addTagebucheintrag(){

    }
}
