package se.alkohest.irkksome.model.impl;

import android.graphics.Color;

import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.orm.AbstractBean;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

@Table("User")
public class IrcUserEB extends AbstractBean implements IrcUser {
    @Column("name")
    private String name;
    @Column("color")
    private int color;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        generateColor();
    }

    @Override
    public int getColor() {
        return color;
    }

    private void generateColor() {
        float hue = name.hashCode()%360;
        float[] hsv = {hue, 0.05f, 0.99f};
        color = Color.HSVToColor(hsv);
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
