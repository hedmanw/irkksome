package se.alkohest.irkksome.orm;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.QueryResult;

public interface PersistenceContext {
    public QueryResult findById(Class<? extends Model> entityBean, long id);

    CursorList<? extends Model> getAll(Class<? extends Model> entity);
}
