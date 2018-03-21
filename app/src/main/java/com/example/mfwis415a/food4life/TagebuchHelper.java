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

    public static final String DB_NAME = "tagebuch.db";
    public static final int DB_VERSION = 1;

    public static final String SQL_CREATE =
            "CREATE TABLE Lebensmittel (ID_LM integer PRIMARY KEY AUTOINCREMENT, Titel string);" +

                    "CREATE TABLE Tagebucheintrag (ID_TBEINTRAG integer PRIMARY KEY AUTOINCREMENT, ID_MENU integer, ID_LM integer, Uhrzeit timestamp, Tageshöchstwert integer);" +

                    "CREATE TABLE Einheiten (ID_EINHEIT integer PRIMARY KEY AUTOINCREMENT, Name string);" +

                    "CREATE TABLE Menü (ID_MENU integer PRIMARY KEY AUTOINCREMENT, Menübezeichnung string);" +

                    "CREATE TABLE Entsprechungen (ID_ENTSPRECHUNG integer PRIMARY KEY AUTOINCREMENT, ID_LM integer, Anzahl float, ID_EINHEIT integer, Entsprechung integer);" +

                    "CREATE TABLE Menü_LM (ID_LM integer, ID_MENU integer);";

    public TagebuchHelper(Context context) {
        //super(context, "PLATZHALTER_DATENBANKNAME", null, 1);
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    // Die onCreate-Methode wird nur aufgerufen, falls die Datenbank noch nicht existiert
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
