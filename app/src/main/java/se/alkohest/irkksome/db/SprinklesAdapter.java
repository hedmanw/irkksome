package se.alkohest.irkksome.db;

import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.AnnotationStripper;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.OneQuery;
import se.emilsjolander.sprinkles.Query;

public class SprinklesAdapter {
    public static <T extends AbstractBean> T findById(Class<T> entityBean, long id) {
        return getSingle(entityBean, "SELECT * FROM " + AnnotationStripper.getTableName(entityBean) + " WHERE ID=?", new String[]{String.valueOf(id)}).get();
    }

    public static <T extends AbstractBean> CursorList<T> getAll(Class<T> entity) {
        return Query.all(entity).get();
    }

    public static <T extends AbstractBean> OneQuery<T> getSingle(Class<T> bean, String selection, String[] selectionArgs) {
        return Query.one(bean, selection, selectionArgs);
    }
}
