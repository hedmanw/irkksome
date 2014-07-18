package se.alkohest.irkksome.orm;

public abstract class GenericDAO<E extends AbstractBean, I extends BeanEntity> {
    protected static PersistenceContext persistenceContext;

    protected abstract I create();
}
