package com.example.mfwis415a.food4life;

/**
 * Created by bburczek on 19.03.2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class TagebuchDataSource {

    private static final String LOG_TAG = TagebuchDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private TagebuchDataSource dbHelper;


    public TagebuchDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new TagebuchDataSource(context);
    }
}