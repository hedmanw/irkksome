package se.alkohest.irkksome.model.impl;

import android.content.ContentValues;

import java.util.Date;

import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.Table;
import se.alkohest.irkksome.orm.Transient;

@Table("t_message")
public abstract class IrcMessageEB extends AbstractBean implements IrcMessage {
    protected String message;
    @Transient
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
    public ContentValues createRow(long dependentPK) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("channel_id", dependentPK);
        contentValues.put("message", message);
//        contentValues.put("timestamp", ); TODO: serialize/deserialize somehow (check SQLite)
        return contentValues;
    }
}
