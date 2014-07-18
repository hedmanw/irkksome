package se.alkohest.irkksome.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteAdapter {
    private DatabaseHelper databaseHelper;

    public SQLiteAdapter(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public long insert(String table, ContentValues contentValues) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        return database.insert(table, null, contentValues);
    }
}
