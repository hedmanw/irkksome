package se.alkohest.irkksome.model.impl;

import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.Table;

@Table("t_user")
public class IrcUserEB extends AbstractBean implements IrcUser {
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IrcUserEB ircUserEB = (IrcUserEB) o;

        if (!name.equals(ircUserEB.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
