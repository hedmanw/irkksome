package se.alkohest.irkksome.orm;

public abstract class GenericDAO<E extends AbstractBean, I extends BeanEntity> {
    protected abstract I create();
}
