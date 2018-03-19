package com.example.mfwis415a.food4life;

/**
 * Created by bburczek on 19.03.2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TagebuchHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = TagebuchHelper.class.getSimpleName();


    public TagebuchHelper(Context context) {
        super(context, "PLATZHALTER_DATENBANKNAME", null, 1);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
