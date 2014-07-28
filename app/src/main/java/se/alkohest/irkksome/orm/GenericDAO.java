package se.alkohest.irkksome.orm;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

// TODO: make DAOs overriding makePersistent to persist OneToOne rels use annotation and use reflection to persist those entities
public abstract class GenericDAO<E extends AbstractBean, I extends BeanEntity> {
    public static PersistenceContext persistenceContext;

    public void makePersistent(I beanEntity) {
        persist(beanEntity, -1);
    }

    public void makePersistent(I beanEntity, long dependentPK) {
        persist(beanEntity, dependentPK);
    }

    public static <T extends BeanEntity> long persist(T beanEntity, long dependentPK) {
        if (beanEntity.getId() != -1) {
            return beanEntity.getId();
        }
        String table = AnnotationStripper.getTable(beanEntity);
        long pk = 0;
        try {
            pk = persistenceContext.create(table, beanEntity.createRow(dependentPK));
            beanEntity.setId(pk);
        } catch (ORMException e) {
            e.printStackTrace();
        }
        return pk;
    }

    protected List<I> getAll(Class<E> beanEntity) {
        String table = AnnotationStripper.getTable(beanEntity);
        Cursor cursor = persistenceContext.read(table, null);
        return initAll(cursor);
    }

    protected List<I> getAll(Class<E> beanEntity, String where, long id) {
        String table = AnnotationStripper.getTable(beanEntity);
        Cursor cursor = persistenceContext.read(table, where, id);
        return initAll(cursor);
    }

    private List<I> initAll(Cursor cursor) {
        List<I> allEntities = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            final long pk = cursor.getLong(0);
            final I bean = initFromCursor(cursor, pk);
            bean.setId(pk);
            allEntities.add(bean);
        }
        cursor.close();
        return allEntities;
    }

    protected String[] queryProjection(Class<E> beanEntity) {
        return AnnotationStripper.getColumnHeads(beanEntity);
    }

    protected void makeTransient(I beanEntity) {
        String table = AnnotationStripper.getTable(beanEntity);
        try {
            persistenceContext.delete(table, beanEntity.getId());
        } catch (ORMException e) {
            e.printStackTrace();
        }
    }

    protected I findById(Class<E> beanClass, long id) {
        final Cursor cursor = persistenceContext.findById(AnnotationStripper.getTable(beanClass), id);
        cursor.moveToNext();
        return initFromCursor(cursor, id);
    }

    public abstract I findById(long id);

    protected abstract I initFromCursor(Cursor cursor, long pk);
}
