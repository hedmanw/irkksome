package se.alkohest.irkksome.orm;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAO<E extends AbstractBean, I extends BeanEntity> {
    public static PersistenceContext persistenceContext;

    public void makePersistent(I beanEntity) {
        String table = AnnotationStripper.getTable(beanEntity);
        try {
            long pk = persistenceContext.create(table, beanEntity.createRow(beanEntity));
            beanEntity.setId(pk);
        } catch (ORMException e) {
            e.printStackTrace();
        }
    }

    protected List<I> getAll(Class<E> beanEntity) {
        String table = AnnotationStripper.getTable(beanEntity);
        Cursor cursor = persistenceContext.read(table, queryProjection(beanEntity));
        cursor.moveToFirst();
        List<I> allUsers = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            final I bean = initFromCursor(cursor);
            bean.setId(cursor.getLong(0));
            allUsers.add(bean);
        }
        cursor.close();
        return allUsers;
    }

    protected String[] queryProjection(Class<E> beanEntity) {
        return AnnotationStripper.getColumnHeads(beanEntity);
    }

    protected void makeTransient(I beanEntity) throws ORMException {
        persistenceContext.delete(beanEntity.getId());
    }

    public abstract void findById(long id);
    protected abstract I initFromCursor(Cursor cursor);
}
