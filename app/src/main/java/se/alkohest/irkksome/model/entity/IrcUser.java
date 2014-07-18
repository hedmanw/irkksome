package se.alkohest.irkksome.model.entity;

import se.alkohest.irkksome.orm.BeanEntity;

public interface IrcUser extends BeanEntity {
    public String getName();

    public void setName(String name);
}
