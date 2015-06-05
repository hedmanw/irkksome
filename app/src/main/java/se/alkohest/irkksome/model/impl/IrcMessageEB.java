package se.alkohest.irkksome.model.impl;

import java.util.Date;

import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.orm.AbstractBean;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * This is ONLY for messages that belong to a specific IrcChannel.
 * Invites, motd, list queries, whois-queries(?) should have their own "async" info EB.
 */
@Table("Message")
public class IrcMessageEB extends AbstractBean implements IrcMessage {
    @Column("message")
    private String message;

    @Column("timestamp")
    private Date timestamp;

    @Column("author")
    private String author;

    private MessageTypeEnum messageType;

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
    public String getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public MessageTypeEnum getMessageType() {
        return messageType;
    }

    @Override
    public void setMessageType(MessageTypeEnum messageTypeEnum) {
        this.messageType = messageTypeEnum;
    }

    public String toString() {
        return messageType + ":" + author + ": " + message;
    }
}
