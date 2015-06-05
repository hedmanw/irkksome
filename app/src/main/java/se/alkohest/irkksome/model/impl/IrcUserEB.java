package se.alkohest.irkksome.model.impl;

import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.util.ColorProvider;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

@Table("User")
public class IrcUserEB extends AbstractBean implements IrcUser {
    @Column("name")
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

        return name.equals(ircUserEB.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
