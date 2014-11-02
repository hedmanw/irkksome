package se.alkohest.irkksome.model.entity;

import java.util.Date;

import se.alkohest.irkksome.orm.BeanEntity;

public interface IrcMessage extends BeanEntity {
    public String getMessage();

    public void setMessage(String message);

    public Date getTimestamp();

    public void setTimestamp(Date timestamp);

    public String getDisplayTimestamp();
}
