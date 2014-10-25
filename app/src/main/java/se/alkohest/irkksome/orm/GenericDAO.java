package se.alkohest.irkksome.orm;

import java.util.List;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Model;

public abstract class GenericDAO<E extends Model> {
    public static PersistenceContext persistenceContext;

    public void persist(E entityBean) {
        entityBean.save();
    }

    public void delete(E entityBean) {
        entityBean.delete();
    }

    public E findById(Class<? extends Model> entity, long id) {
        return (E) persistenceContext.findById(entity, id);
    }

    public List<E> getAll(Class<? extends Model> entity) {
        CursorList list = persistenceContext.getAll(entity);
        List evaluatedList = list.asList();
        list.close();
        return evaluatedList;
    }

    protected abstract Class<E> getEntityBean();
}
