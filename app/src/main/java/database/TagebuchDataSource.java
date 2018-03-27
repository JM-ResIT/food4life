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
    }

    public void addFood(String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TagebuchHelper.TITEL, name);

        database.insert(TagebuchHelper.DATABASE_LMTABLE, null, contentValues);
    }
}