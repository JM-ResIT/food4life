package database;

/**
 * Created by bburczek on 19.03.2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class TagebuchDataSource {

    private static final String LOG_TAG = TagebuchDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private SQLiteDatabase databaseRead;
    private TagebuchHelper dbHelper;

    public TagebuchDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new TagebuchHelper(context);
    }

    public void open() {
            database = dbHelper.getWritableDatabase();
            databaseRead = dbHelper.getReadableDatabase();
    }

    public void close() {
        if (database != null && database.isOpen()) {
            dbHelper.close();
        }
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public void addTagebuchEintrag(){

        Log.d(LOG_TAG, "Testdaten werden eingepflegt");
        ContentValues contentValues = new ContentValues();

        contentValues.put(TagebuchHelper.MENU_ID, 14232);
        contentValues.put(TagebuchHelper.LEBENSMITTEL_ID, 13);
        contentValues.put(TagebuchHelper.LIMIT, 2500);

        database.insert(TagebuchHelper.DATABASE_TBTABLE, null, contentValues);
    }

    public void addFood(String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TagebuchHelper.TITEL, name);

        database.insert(TagebuchHelper.DATABASE_LMTABLE, null, contentValues);
    }

    public void listAllFromTagebuchEintrag(){
        Cursor cursor = databaseRead.rawQuery("SELECT * FROM " + TagebuchHelper.DATABASE_TBTABLE , null);

        while(cursor.moveToNext()){
            Log.d(LOG_TAG, "EINTRAG");
            Log.d(LOG_TAG, cursor.getString(1) + " "+ cursor.getString(2)+ " " + cursor.getString(3)+ " " + cursor.getString(4));
        }
    }
}