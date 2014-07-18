package se.alkohest.irkksome.orm;

import android.content.ContentValues;

public abstract class GenericDAO<E extends AbstractBean, I extends BeanEntity> {
    protected static PersistenceContext persistenceContext;
    private String table;

    public GenericDAO() {
        // get table from Annotation on E
    }

    protected void makePersistent(I beanEntity) throws ORMException {
        persistenceContext.create(table, createContentValues(beanEntity));
    }

    protected void makeTransient(I beanEntity) throws ORMException {
        persistenceContext.delete();
    }

    protected abstract ContentValues createContentValues(I beanEntity);
}
