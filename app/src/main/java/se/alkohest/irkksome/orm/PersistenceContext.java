package se.alkohest.irkksome.orm;

import android.content.ContentValues;
import android.database.Cursor;

public interface PersistenceContext {
    public long create(String table, ContentValues contentValues) throws ORMException;
    public Cursor read(String table, String[] projection);
    public int update() throws ORMException;
    public int delete() throws ORMException;

}
