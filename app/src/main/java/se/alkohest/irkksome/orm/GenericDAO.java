package se.alkohest.irkksome.orm;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class GenericDAO<E extends AbstractBean, I extends BeanEntity> {
    public static PersistenceContext persistenceContext;

    public void makePersistent(I beanEntity) {
        String table = AnnotationStripper.getTable(beanEntity);
        try {
            long pk = persistenceContext.create(table, createContentValues(beanEntity));
            beanEntity.setId(pk);
        } catch (ORMException e) {
            e.printStackTrace();
        }
    }

    protected Cursor getAll(Class<E> beanEntity) {
        String table = AnnotationStripper.getTable(beanEntity);
        return persistenceContext.read(table, queryProjection(beanEntity));
    }

    protected String[] queryProjection(Class<E> beanEntity) {
        return null;
    }

    protected void makeTransient(I beanEntity) throws ORMException {
        persistenceContext.delete();
    }

    public abstract void findById(long id);
    protected abstract ContentValues createContentValues(I beanEntity);
}
