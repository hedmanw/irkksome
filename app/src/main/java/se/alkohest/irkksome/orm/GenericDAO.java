package se.alkohest.irkksome.orm;

public abstract class GenericDAO<E extends AbstractBean, I extends BeanEntity> {

    protected boolean save(I bean) {
        // perform save to DB
        return true;
    }

    protected boolean update(I bean) {
        // perform update
        return true;
    }
}
