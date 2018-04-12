package database;

/**
 * Created by bburczek on 19.03.2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public List<String> getActiveFoods() {
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

    public List<String> getActiveMenus() {
        List<String> labels = new ArrayList<String>();

        // Select Query
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_MENUTABLE, null, TagebuchHelper.IS_ACTIVE + "=?", new String[]{String.valueOf(1)}, null, null, TagebuchHelper.TITEL);

        // looping through all rows and adding to list

        while (cursor.moveToNext()) {
            labels.add(cursor.getString(1) + " (" + cursor.getString(2) + ")");
        }

        // closing cursor
        cursor.close();

        // returning lables
        return labels;
    }


    public List<String> getActiveUnits() {
        List<String> labels = new ArrayList<String>();

        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_EINTABLE, null, TagebuchHelper.IS_ACTIVE + "=?", new String[]{String.valueOf(1)}, null, null, TagebuchHelper.TITEL);
        while (cursor.moveToNext()) {
            labels.add(cursor.getString(1));
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

    public int getRealIdFromMeal(String date, int category, int position) {
        int pos;
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_TBTABLE, null, TagebuchHelper.IS_ACTIVE + "=? and " + TagebuchHelper.DATUM + "=? and " + TagebuchHelper.KATEGORIE + "=?", new String[]{String.valueOf(1), date, String.valueOf(category)}, null, null, TagebuchHelper.TAGEBUCHEINTRAG_ID, position + ",1");

        cursor.moveToFirst();
        pos = Integer.parseInt(cursor.getString(0));

        return pos;
    }

    public int getRealIdFromUnit(int position) {
        int pos;
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_EINTABLE, null, TagebuchHelper.IS_ACTIVE + "=?", new String[]{String.valueOf(1)}, null, null, TagebuchHelper.TITEL, position + ",1");

        cursor.moveToFirst();
        pos = Integer.parseInt(cursor.getString(0));

        return pos;
    }

    public int getRealIdFromMenu(int position) {
        int pos;
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_MENUTABLE, null, TagebuchHelper.IS_ACTIVE + "=?", new String[]{String.valueOf(1)}, null, null, TagebuchHelper.TITEL, position + ",1");

        cursor.moveToFirst();
        pos = Integer.parseInt(cursor.getString(0));

        return pos;
    }

    public int getPositionFromFood(int id) {
        int pos = 0;
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_LMTABLE, new String[]{TagebuchHelper.LEBENSMITTEL_ID}, TagebuchHelper.IS_ACTIVE + "=?", new String[]{String.valueOf(1)}, null, null, TagebuchHelper.TITEL);

        while (cursor.moveToNext()) {
            int lm_id = Integer.parseInt(cursor.getString(0));
            if (lm_id == id) {
                return pos;
            }
            pos += 1;
        }
        return pos;
    }

    public void updateStatusOfLM(int id, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TagebuchHelper.IS_ACTIVE, status);
        database.update(TagebuchHelper.DATABASE_LMTABLE, contentValues, TagebuchHelper.LEBENSMITTEL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateStatusOfMenu(int id, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TagebuchHelper.IS_ACTIVE, status);
        database.update(TagebuchHelper.DATABASE_MENUTABLE, contentValues, TagebuchHelper.MENU_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateStatusOfUnit(int id, int status) {
        ContentValues unitValues = new ContentValues();
        unitValues.put(TagebuchHelper.IS_ACTIVE, status);
        database.update(TagebuchHelper.DATABASE_EINTABLE, unitValues, TagebuchHelper.EINHEIT_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteMeal(int id) {
        database.delete(TagebuchHelper.DATABASE_TBTABLE, TagebuchHelper.TAGEBUCHEINTRAG_ID + "=?", new String[]{String.valueOf(id)});
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


    public void addMealEntry(int is_lm, int id, String date, int category, int calories, float amount) {
        ContentValues mealValues = new ContentValues();
        mealValues.put(TagebuchHelper.IS_LM, is_lm);
        mealValues.put(TagebuchHelper.MENU_LM_ID, id);
        mealValues.put(TagebuchHelper.DATUM, date);
        mealValues.put(TagebuchHelper.KATEGORIE, category);
        mealValues.put(TagebuchHelper.KALORIEN, calories);
        mealValues.put(TagebuchHelper.ANZAHL, amount);

        database.insert(TagebuchHelper.DATABASE_TBTABLE, null, mealValues);
    }

    public void addMenuEntry(String titel, String description, List<Integer> positions) {
        ContentValues menuValues = new ContentValues();
        menuValues.put(TagebuchHelper.TITEL, titel);
        menuValues.put(TagebuchHelper.BESCHREIBUNG, description);

        long menu_id = database.insert(TagebuchHelper.DATABASE_MENUTABLE, null, menuValues);

        ContentValues menu_lmValues = new ContentValues();
        for (int pos : positions) {

            menu_lmValues.put(TagebuchHelper.MENU_ID, menu_id);
            menu_lmValues.put(TagebuchHelper.LEBENSMITTEL_ID, getRealIdFromLM(pos));
            database.insert(TagebuchHelper.DATABASE_MENU_LM_TABLE, null, menu_lmValues);
        }

    }

    public List<Integer> getFoodPosFromMenu(int id) {
        List<Integer> positions = new ArrayList<Integer>();

        Cursor allFoods = databaseRead.query(TagebuchHelper.DATABASE_LMTABLE, new String[]{TagebuchHelper.LEBENSMITTEL_ID}, TagebuchHelper.IS_ACTIVE + "=?", new String[]{String.valueOf(1)}, null, null, TagebuchHelper.TITEL);

        int pos = 0;
        while (allFoods.moveToNext()) {
            Cursor foodsFromMenu = databaseRead.query(TagebuchHelper.DATABASE_MENU_LM_TABLE, null, TagebuchHelper.MENU_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            while (foodsFromMenu.moveToNext()) {
                if (allFoods.getInt(0) == foodsFromMenu.getInt(1)) {
                    positions.add(pos);
                }
            }
            pos += 1;
        }
        return positions;
    }


    public void editMealEntry(int meal_id, int is_lm, int id, String date, int category, int calories, float amount) {
        ContentValues mealValues = new ContentValues();
        mealValues.put(TagebuchHelper.IS_LM, is_lm);
        mealValues.put(TagebuchHelper.MENU_LM_ID, id);
        mealValues.put(TagebuchHelper.DATUM, date);
        mealValues.put(TagebuchHelper.KATEGORIE, category);
        mealValues.put(TagebuchHelper.KALORIEN, calories);
        mealValues.put(TagebuchHelper.ANZAHL, amount);

        database.update(TagebuchHelper.DATABASE_TBTABLE, mealValues, TagebuchHelper.TAGEBUCHEINTRAG_ID + "=?", new String[]{String.valueOf(meal_id)});
    }

    public void addUnitEntry(String titel) {
        ContentValues unitValues = new ContentValues();
        unitValues.put(TagebuchHelper.TITEL, titel);

        database.insert(TagebuchHelper.DATABASE_EINTABLE, null, unitValues);
    }


    public List<String> getMealEntries(String date, int category) {
        List<String> meals = new ArrayList<String>();
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_TBTABLE, null, TagebuchHelper.DATUM + "=? and " + TagebuchHelper.KATEGORIE + "=?", new String[]{date, String.valueOf(category)}, null, null, null);

        while (cursor.moveToNext()) {
            String mealName;
            //(String table, String column, String key, int id)
            if(cursor.getInt(1) == 1){
                 mealName = getEntryFromDBTable(TagebuchHelper.DATABASE_LMTABLE, TagebuchHelper.TITEL, TagebuchHelper.LEBENSMITTEL_ID, Integer.parseInt(cursor.getString(2)));
            } else {
                mealName = getEntryFromDBTable(TagebuchHelper.DATABASE_MENUTABLE, TagebuchHelper.TITEL, TagebuchHelper.MENU_ID, Integer.parseInt(cursor.getString(2)));
            }
            meals.add(mealName + " (" + cursor.getString(5) + " kcal)");
        }
        // closing cursor
        cursor.close();
        return meals;

    }


    public void editFoodEntry(String name, String foodDescription, float amount, String unit, int equivalent, int id) {
        updateStatusOfLM(id, 0);
        addFoodEntry(name, foodDescription, amount, unit, equivalent);
    }

    public void editMenu(String titel, String description, List<Integer> positions, int id) {
        updateStatusOfMenu(id, 0);
        addMenuEntry(titel, description, positions);
    }

    public void editUnitEntry(String titel, int id) {
        updateStatusOfUnit(id, 0);
        addUnitEntry(titel);
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

    public int getAverageCalories(int days) {
        int calories = 0;

        for (int i = 0; i < days; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -i);
            Date dateBeforeNDays = cal.getTime();
            SimpleDateFormat showDate = new SimpleDateFormat("dd.MM.yyyy");
            calories += getConsumedCalories(showDate.format(dateBeforeNDays));
        }

        return Math.round(calories / days);
    }

    public String getEntryFromDBTable(String table, String column, String key, int id) {
        String entry = "";
        // Select Query
        Cursor cursor = databaseRead.query(table, new String[]{column}, key + "=?", new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        entry = cursor.getString(0);

        return entry;
    }

    public int getCaloriesFromMenu(int id) {
        int calories = 0;

        Cursor foodsFromMenu = databaseRead.query(TagebuchHelper.DATABASE_MENU_LM_TABLE, null, TagebuchHelper.MENU_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        while (foodsFromMenu.moveToNext()) {
            int foodCalories = Integer.parseInt(getEntryFromDBTable(TagebuchHelper.DATABASE_ENTSPTABLE, TagebuchHelper.ENTSPRECHUNG, TagebuchHelper.LEBENSMITTEL_ID, foodsFromMenu.getInt(1)));

            calories += foodCalories;
        }

        return calories;
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

    public int getConsumedCalories(String date) {
        int calories = 0;
        Cursor cursor = databaseRead.query(TagebuchHelper.DATABASE_TBTABLE, new String[]{TagebuchHelper.KALORIEN}, TagebuchHelper.DATUM + "=?", new String[]{date}, null, null, null);

        while (cursor.moveToNext()) {
            calories += Integer.parseInt(cursor.getString(0));
        }
        return calories;
    }

}