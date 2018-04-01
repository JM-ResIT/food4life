package database;

/**
 * Created by bburczek on 19.03.2018.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TagebuchHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = TagebuchHelper.class.getSimpleName();

    private static final String DB_NAME = "tagebuch.db";
    private static final int DB_VERSION = 1;

    public static final String DATABASE_TBTABLE = "TAGEBUCHEINTRAG";
    public static final String TAGEBUCHEINTRAG_ID = "TB_ID";
    public static final String LEBENSMITTEL_ID = "LM_ID";
    public static final String MENU_ID = "MENU_ID";
    public static final String ZEIT = "ZEIT";
    public static final String LIMIT = "TAGESLIMIT";

    public static final String DATABASE_LMTABLE = "LEBENSMITTEL";
    public static final String TITEL = "TITEL";

    public static final String DATABASE_EINTABLE = "EINHEIT";
    public static final String EINHEIT_ID = "EN_ID";

    public static final String DATABASE_MENUTABLE = "MENU";

    public static final String DATABASE_ENTSPTABLE = "ENTSPRECHUNG";
    public static final String ENTSPRECHUNG_ID = "ENTSP_ID";
    public static final String ANZAHL = "ANZAHL";
    public static final String ENTSPRECHUNG = "ENTSPRECHUNG";

    public static final String DATABASE_MENU_LM_TABLE = "MENU_LM";

    public static final String EINHEIT = "EINHEIT";
    public static final String BESCHREIBUNG = "BESCHREIBUNG";
    public static final String IS_ACTIVE = "IS_ACTIVE";

    //TODO create missing tables
    private static final String SQL_CREATE_TBTABLE =
            "CREATE TABLE " +
                    DATABASE_TBTABLE + "(" +
                    TAGEBUCHEINTRAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MENU_ID + " INTEGER, " +
                    LEBENSMITTEL_ID + " INTEGER, " +
                    LIMIT + " INTEGER, " +
                    ZEIT + " DATETIME DEFAULT CURRENT_TIMESTAMP); ";

    private static final String SQL_CREATE_LMTABLE =
            "CREATE TABLE " +
                    DATABASE_LMTABLE + "(" +
                    LEBENSMITTEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITEL + " STRING, " +
                    BESCHREIBUNG + " STRING, " +
                    IS_ACTIVE + " BOOLEAN NOT NULL default 1);"; //flag=cursor.getString(0).equals("1")

    private static final String SQL_CREATE_EINTABLE =
            "CREATE TABLE " +
                    DATABASE_EINTABLE + "(" +
                    EINHEIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITEL + " STRING);";

    private static final String SQL_CREATE_MENUTABLE =
            "CREATE TABLE " +
                    DATABASE_MENUTABLE + "(" +
                    MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITEL + " STRING);";

    private static final String SQL_CREATE_ENTSPTABLE =
            "CREATE TABLE " +
                    DATABASE_ENTSPTABLE + "(" +
                    ENTSPRECHUNG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LEBENSMITTEL_ID + " INTEGER, " +
                    ANZAHL + " FLOAT, " +
                    EINHEIT + " STRING, " +
                    ENTSPRECHUNG + " INTEGER);";

    private static final String SQL_CREATE_MENU_LM_TABLE =
            "CREATE TABLE " +
                    DATABASE_MENU_LM_TABLE + "(" +
                    LEBENSMITTEL_ID + " INTEGER, " +
                    MENU_ID + " INTEGER);";

    public TagebuchHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @SuppressLint("SQLiteString")
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_TBTABLE);
            db.execSQL(SQL_CREATE_LMTABLE);
            db.execSQL(SQL_CREATE_EINTABLE);
            db.execSQL(SQL_CREATE_ENTSPTABLE);
            db.execSQL(SQL_CREATE_MENUTABLE);
            db.execSQL(SQL_CREATE_MENU_LM_TABLE);
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Error creating tables: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
