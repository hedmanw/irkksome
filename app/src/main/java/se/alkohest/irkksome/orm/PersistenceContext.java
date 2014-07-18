package se.alkohest.irkksome.orm;

import android.content.ContentValues;

public interface PersistenceContext {
    public long create(String table, ContentValues contentValues) throws ORMException;
    public ContentValues read(long id);
    public int update() throws ORMException;
    public int delete() throws ORMException;

}
