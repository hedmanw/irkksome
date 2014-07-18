package se.alkohest.irkksome.orm;

public interface PersistenceContext {
    public long create(Object bean) throws ORMException;
}
