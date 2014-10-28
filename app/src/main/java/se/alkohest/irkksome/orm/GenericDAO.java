package se.alkohest.irkksome.orm;

import java.util.List;

import se.alkohest.irkksome.db.SprinklesAdapter;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Model;

public abstract class GenericDAO<E extends Model> {
    public void persist(E entityBean) {
        entityBean.save();
    }

    public void delete(E entityBean) {
        entityBean.delete();
    }

    public E getById(long id) {
        return SprinklesAdapter.findById(getEntityBean(), id);
    }

    public List<E> getAll() {
        final CursorList<E> databaseEntries = SprinklesAdapter.getAll(getEntityBean());
        final List<E> entityBeans = databaseEntries.asList();
        databaseEntries.close();
        return entityBeans;
    }

    protected abstract Class<E> getEntityBean();
}
