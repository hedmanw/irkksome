package se.alkohest.irkksome.model.entity;

import java.util.Date;

import se.alkohest.irkksome.orm.BeanEntity;

public interface IrcMessage extends BeanEntity {
    enum MessageTypeEnum {
        SENT, RECEIVED, JOIN, PART, QUIT, NICKCHANGE
    }

    public String getMessage();

    public void setMessage(String message);

    public Date getTimestamp();

    public void setTimestamp(Date timestamp);

    public IrcUser getAuthor();

    public void setAuthor(IrcUser author);

    public MessageTypeEnum getMessageType();

    public void setMessageType(MessageTypeEnum messageTypeEnum);
}
