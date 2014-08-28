package se.alkohest.irkksome.model.impl;

import android.content.ContentValues;

import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.orm.OneToOne;
import se.alkohest.irkksome.orm.Table;

@Table("t_chatmessage")
public class IrcChatMessageEB extends IrcMessageEB {
    @OneToOne(IrcUserEB.class)
    private IrcUser author;
    private boolean hilight;

    public IrcUser getAuthor() {
        return author;
    }

    public void setAuthor(IrcUser author) {
        this.author = author;
    }

    public boolean isHilight() {
        return hilight;
    }

    public void setHilight(boolean hilight) {
        this.hilight = hilight;
    }

    public String toString() {
        return author.getName() + ": " + message;
    }

    @Override
    public ContentValues createRow(long dependentPK) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("message_id", getId());
        contentValues.put("author", author.getId());
        contentValues.put("ishilight", hilight);
        return contentValues;
    }
}
