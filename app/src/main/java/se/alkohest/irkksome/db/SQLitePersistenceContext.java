package se.alkohest.irkksome.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import se.alkohest.irkksome.orm.AnnotationStripper;
import se.alkohest.irkksome.orm.ORMException;
import se.alkohest.irkksome.orm.PersistenceContext;

public class SQLitePersistenceContext implements PersistenceContext {
    private DatabaseHelper databaseHelper;

    public SQLitePersistenceContext(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public long create(Object bean) throws ORMException {
        String table = AnnotationStripper.getTable(bean);
        ContentValues values = new ContentValues(); // get from fields not annotated with @Transient

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        long primaryKey = database.insert(table, null, values);
        if (primaryKey == -1) {
            throw new ORMException("Could not perform CREATE.");
        }
        return primaryKey;
    }
}
