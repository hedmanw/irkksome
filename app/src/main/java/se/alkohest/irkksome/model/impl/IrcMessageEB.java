package se.alkohest.irkksome.model.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.orm.AbstractBean;
import se.emilsjolander.sprinkles.annotations.Table;

@Table("Message")
public class IrcMessageEB extends AbstractBean implements IrcMessage {
    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    protected String message;
    protected Date timestamp;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getDisplayTimestamp() {
        return dateFormat.format(timestamp);
    }
}
