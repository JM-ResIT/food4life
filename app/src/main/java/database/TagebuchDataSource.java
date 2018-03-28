package database;

/**
 * Created by bburczek on 19.03.2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mfwis415a.food4life.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TagebuchDataSource {

    public static final String LOG_TAG = TagebuchDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private SQLiteDatabase databaseRead;
    private TagebuchHelper dbHelper;

    public TagebuchDataSource(Context context) {
        dbHelper = new TagebuchHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        database = dbHelper.getWritableDatabase();
        databaseRead = dbHelper.getReadableDatabase();
    }

    public void close() {
        if (database != null && database.isOpen()) {
            Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
            dbHelper.close();
        }
    }



    public List<String> getAllUnits() {
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TagebuchHelper.DATABASE_EINTABLE;

        Cursor cursor = databaseRead.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        while (cursor.moveToNext()) {
            labels.add(cursor.getString(1));
        }

        // closing connection
        cursor.close();
        databaseRead.close();

        // returning lables
        return labels;
    }

    public void addFoodEntry(String name, int amount, String unit, int equivalent) {
        if(!name.isEmpty()){
            ContentValues lmValues = new ContentValues();
            lmValues.put(TagebuchHelper.TITEL, name);

            long idLM = database.insert(TagebuchHelper.DATABASE_LMTABLE, null, lmValues);

            ContentValues entspValues = new ContentValues();
            entspValues.put(TagebuchHelper.LEBENSMITTEL_ID, idLM);
            entspValues.put(TagebuchHelper.ANZAHL, amount);
            entspValues.put(TagebuchHelper.EINHEIT, unit);
            entspValues.put(TagebuchHelper.ENTSPRECHUNG, equivalent);

            database.insert(TagebuchHelper.DATABASE_ENTSPTABLE, null, entspValues);
        } else {
            Log.d(LOG_TAG, "Error inserting food entry, name is empty!");
        }

    }

    public void listFood() {
        Cursor cursor = databaseRead.rawQuery("SELECT * FROM " + TagebuchHelper.DATABASE_ENTSPTABLE, null);

        while (cursor.moveToNext()) {
            Log.d(LOG_TAG, "Lebensmittel ID:" + cursor.getString(1));
            Log.d(LOG_TAG, "Anzahl: " + cursor.getString(2));
            Log.d(LOG_TAG, "Einheit:" + cursor.getString(3));
            Log.d(LOG_TAG, "ENTSPRECHUNG:" + cursor.getString(4));
        }

        cursor.close();
    }

    public void insertSampleData(){
        ContentValues contentValues = new ContentValues();

        // TODO simplify
        contentValues.put(TagebuchHelper.TITEL, "Stück");
        database.insert(TagebuchHelper.DATABASE_EINTABLE, null, contentValues);
        contentValues.put(TagebuchHelper.TITEL, "g");
        database.insert(TagebuchHelper.DATABASE_EINTABLE, null, contentValues);
        contentValues.put(TagebuchHelper.TITEL, "L");
        database.insert(TagebuchHelper.DATABASE_EINTABLE, null, contentValues);
        contentValues.put(TagebuchHelper.TITEL, "ml");
        database.insert(TagebuchHelper.DATABASE_EINTABLE, null, contentValues);
    }

    public void insertSampleDataIfEmpty() {
        String count = "SELECT count(*) FROM " + TagebuchHelper.DATABASE_EINTABLE;
        Cursor mcursor = database.rawQuery(count, null);

        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
            Log.d(LOG_TAG, "Data already inserted, proceeding...");
        } else {
            insertSampleData();
        }
    }

}