package se.alkohest.irkksome.orm;

public abstract class AbstractBean implements BeanEntity {
    private long id;

    @Override
    public long getId() {
        return id;
    }
}
