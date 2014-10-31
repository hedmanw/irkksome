package se.alkohest.irkksome.model.impl;

import android.content.ContentValues;
import android.graphics.Color;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        float hue = generateHue();
        float[] hsv = {hue, 0.05f, 0.99f};
        color = Color.HSVToColor(hsv);
    }
    private float generateHue() {
        byte[] bytes = new byte[0];
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(name.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        float sum = 0;
        for (byte value : bytes) {
            sum += Math.abs(value);
        }
        return sum % 360;
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
