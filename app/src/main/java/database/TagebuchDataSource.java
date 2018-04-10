package database;

/**
 * Created by bburczek on 19.03.2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
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
        Log.d(LOG_TAG, "Open Database.");
        database = dbHelper.getWritableDatabase();
        databaseRead = dbHelper.getReadableDatabase();
    }

    public void close() {
        if (database != null && database.isOpen()) {
            Log.d(LOG_TAG, "Close Database.");
            dbHelper.close();
        }
    }


    public List<String> getAllUnits() {
        List<String> labels = new ArrayList<String>();

        // Select All Query
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_EINTABLE, null, null, null, null, null, null);

        // looping through all rows and adding to list

        while (cursor.moveToNext()) {
            labels.add(cursor.getString(1));
        }

        // closing cursor
        cursor.close();

        // returning lables
        return labels;
    }

    public List<String> getAllFoods() {
        List<String> labels = new ArrayList<String>();

        // Select Query
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_LMTABLE, null, TagebuchHelper.IS_ACTIVE + "=?", new String[]{String.valueOf(1)}, null, null, TagebuchHelper.TITEL);

        // looping through all rows and adding to list

        while (cursor.moveToNext()) {
            labels.add(cursor.getString(1) + " (" + cursor.getString(2) + ")");
        }

        // closing cursor
        cursor.close();

        // returning lables
        return labels;
    }

    public int getRealIdFromLM(int position) {
        int pos;
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_LMTABLE, null, TagebuchHelper.IS_ACTIVE + "=?", new String[]{String.valueOf(1)}, null, null, TagebuchHelper.TITEL, position + ",1");

        cursor.moveToFirst();
        pos = Integer.parseInt(cursor.getString(0));

        return pos;
    }


    public void updateStatusOfLM(int id, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TagebuchHelper.IS_ACTIVE, status);
        database.update(TagebuchHelper.DATABASE_LMTABLE, contentValues, TagebuchHelper.LEBENSMITTEL_ID + "= ?", new String[]{String.valueOf(id)});
    }

    public void addFoodEntry(String name, String foodDescription, float amount, String unit, int equivalent) {
        ContentValues lmValues = new ContentValues();
        lmValues.put(TagebuchHelper.TITEL, name);
        lmValues.put(TagebuchHelper.BESCHREIBUNG, foodDescription);

        long idLM = database.insert(TagebuchHelper.DATABASE_LMTABLE, null, lmValues);

        ContentValues entspValues = new ContentValues();
        entspValues.put(TagebuchHelper.LEBENSMITTEL_ID, idLM);
        entspValues.put(TagebuchHelper.ANZAHL, amount);
        entspValues.put(TagebuchHelper.EINHEIT, unit);
        entspValues.put(TagebuchHelper.ENTSPRECHUNG, equivalent);

        database.insert(TagebuchHelper.DATABASE_ENTSPTABLE, null, entspValues);
    }


    public void addMealEntry(int is_lm, int id, String date, int category, int calories) {
        ContentValues mealValues = new ContentValues();
        mealValues.put(TagebuchHelper.IS_LM, is_lm);
        mealValues.put(TagebuchHelper.MENU_LM_ID, id);
        mealValues.put(TagebuchHelper.ZEIT, date);
        mealValues.put(TagebuchHelper.KATEGORIE, category);
        mealValues.put(TagebuchHelper.KALORIEN, calories);

        database.insert(TagebuchHelper.DATABASE_TBTABLE, null, mealValues);
    }

    public List<String> getMealEntries(String date, int category) {
        List<String> meals = new ArrayList<String>();
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_TBTABLE, null, TagebuchHelper.ZEIT + "=? and " + TagebuchHelper.KATEGORIE + "=?", new String[]{date, String.valueOf(category)}, null, null, null);

        while (cursor.moveToNext()) {
            //(String table, String column, String key, int id)
            String mealName = getEntryFromDBTable(TagebuchHelper.DATABASE_LMTABLE, TagebuchHelper.TITEL, TagebuchHelper.LEBENSMITTEL_ID, Integer.parseInt(cursor.getString(2)));
            meals.add(mealName + " (" + cursor.getString(5) + " kcal)");
        }
        // closing cursor
        cursor.close();
        return meals;

    }


    public void editFoodEntry(String name, String foodDescription, int amount, String unit, int equivalent, int id) {
        updateStatusOfLM(id, 0);
        addFoodEntry(name, foodDescription, amount, unit, equivalent);
    }

    public void insertSampleData() {
        ContentValues units = new ContentValues();

        units.put(TagebuchHelper.TITEL, "Stück");
        database.insert(TagebuchHelper.DATABASE_EINTABLE, null, units);
        units.put(TagebuchHelper.TITEL, "g");
        database.insert(TagebuchHelper.DATABASE_EINTABLE, null, units);
        units.put(TagebuchHelper.TITEL, "L");
        database.insert(TagebuchHelper.DATABASE_EINTABLE, null, units);
        units.put(TagebuchHelper.TITEL, "ml");
        database.insert(TagebuchHelper.DATABASE_EINTABLE, null, units);

        ContentValues limit = new ContentValues();
        limit.put(TagebuchHelper.LIMIT, 2000);
        database.insert(TagebuchHelper.DATABASE_PROFIL_TABLE, null, limit);

        addFoodEntry("Banane", "1 Stück", 1, "Stück", 80);
        addFoodEntry("Schokolade", "100g", 100, "g", 456);
        addFoodEntry("Pizza Dr. Oetker", "1 Stück", 1, "Stück", 750);
        addFoodEntry("Reis", "100g", 100, "g", 400);
        addFoodEntry("Milch", "100ml", 100, "ml", 40);
    }

    public void insertSampleDataIfEmpty() {
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_EINTABLE, new String[]{"count(*)"}, null, null, null, null, null);

        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        if (icount > 0) {
            Log.d(LOG_TAG, "Data already inserted, proceeding...");
        } else {
            insertSampleData();
        }
    }


    public String getEntryFromDBTable(String table, String column, String key, int id) {
        String entry = "";
        // Select Query
        Cursor cursor = databaseRead.query(table, new String[]{column}, key + "=?", new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        entry = cursor.getString(0);

        return entry;
    }

    public void updateProfile(int limit) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TagebuchHelper.LIMIT, limit);
        database.update(TagebuchHelper.DATABASE_PROFIL_TABLE, contentValues, TagebuchHelper.PROFIL_ID + "= ?", new String[]{String.valueOf(1)});
    }

    public int getLimitFromProfile() {
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_PROFIL_TABLE, new String[]{TagebuchHelper.LIMIT}, TagebuchHelper.PROFIL_ID + "=?", new String[]{String.valueOf(1)}, null, null, null);

        cursor.moveToFirst();
        return Integer.parseInt(cursor.getString(0));
    }
}