package se.alkohest.irkksome.db;

import android.content.Context;

import se.alkohest.irkksome.orm.AnnotationStripper;
import se.alkohest.irkksome.orm.PersistenceContext;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.QueryResult;

public class SprinklesPersistenceContext implements PersistenceContext {
    private SprinklesAdapter database;

    SprinklesPersistenceContext() {

    }

    public SprinklesPersistenceContext(Context context) {
        database = new SprinklesAdapter(context);
    }

    @Override
    public QueryResult findById(Class<? extends Model> entityBean, long id) {
        return database.read(entityBean, "SELECT * FROM WHERE " + AnnotationStripper.getTableName(entityBean) + " ID=?", new String[] {String.valueOf(id)}).get();
    }

    @Override
    public CursorList<? extends Model> getAll(Class<? extends Model> entity) {
        return Query.all(entity).get();
    }
}
