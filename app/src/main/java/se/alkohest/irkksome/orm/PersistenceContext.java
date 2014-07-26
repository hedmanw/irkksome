package se.alkohest.irkksome.orm;

import android.content.ContentValues;
import android.database.Cursor;

public interface PersistenceContext {
    public long create(String table, ContentValues contentValues) throws ORMException;
    public Cursor read(String table, String[] projection);
    public Cursor read(String table, String where, long id);
    public Cursor findById(String table, long id);
    public int update() throws ORMException;
    public int delete(long id) throws ORMException;

}
