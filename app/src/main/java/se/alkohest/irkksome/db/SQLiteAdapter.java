package se.alkohest.irkksome.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import se.alkohest.irkksome.irc.Log;

public class SQLiteAdapter {
    private DatabaseHelper databaseHelper;

    public SQLiteAdapter(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public long insert(String table, ContentValues contentValues) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Log.getInstance(getClass()).i(table + " " + contentValues.toString());
        return database.insert(table, null, contentValues);
    }

    public Cursor read(String table, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        queryBuilder.setTables(table);
        return queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public int delete(String table, String where, String[] whereArgs) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        return database.delete(table, where, whereArgs);
    }
}
