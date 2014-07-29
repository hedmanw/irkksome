package se.alkohest.irkksome.model.impl;

import android.content.ContentValues;

import java.util.Date;

import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.OneToOne;
import se.alkohest.irkksome.orm.Table;
import se.alkohest.irkksome.orm.Transient;

@Table("t_message")
public class IrcMessageEB extends AbstractBean implements IrcMessage {
    private String message;
    @Transient
    private Date timestamp;
    @OneToOne(IrcUserEB.class)
    private IrcUser author;
    private boolean hilight;

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
    public boolean isHilight() {
        return hilight;
    }

    @Override
    public void setHilight(boolean hilight) {
        this.hilight = hilight;
    }

    @Override
    public String toString() {
        return author.getName() + ": " + message;
    }

    @Override
    public ContentValues createRow(long dependentPK) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("channel_id", dependentPK);
        contentValues.put("message", message);
//        contentValues.put("timestamp", ); TODO: serialize/deserialize somehow (check SQLite)
        contentValues.put("author", author.getId());
        contentValues.put("hilight", hilight);
        return contentValues;
    }
}
