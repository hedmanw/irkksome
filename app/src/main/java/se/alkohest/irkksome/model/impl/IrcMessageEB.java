package se.alkohest.irkksome.model.impl;

import android.content.ContentValues;

import java.util.Date;

import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.orm.AbstractBean;

public class IrcMessageEB extends AbstractBean implements IrcMessage {
    private String message;
    private Date timestamp;
    private IrcUser author;

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
    public IrcUser getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(IrcUser author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return author.getName() + ": " + message;
    }

    @Override
    public ContentValues createRow(long dependentPK) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("channel", dependentPK);
        contentValues.put("message", message);
//        contentValues.put("timestamp", ); TODO: serialize/deserialize somehow (check SQLite)
        contentValues.put("author", author.getName());
        return contentValues;
    }
}
