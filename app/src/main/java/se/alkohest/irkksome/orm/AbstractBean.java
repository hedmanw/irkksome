package se.alkohest.irkksome.orm;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.AutoIncrement;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;

public abstract class AbstractBean extends Model implements BeanEntity {
    @Key
    @AutoIncrement
    @Column("id")
    private long id;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
