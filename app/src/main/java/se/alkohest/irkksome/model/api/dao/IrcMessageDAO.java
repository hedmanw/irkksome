package se.alkohest.irkksome.model.api.dao;

import java.util.Date;

import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.impl.IrcMessageEB;

public class IrcMessageDAO {
    public IrcMessage create(IrcMessage.MessageTypeEnum messageType, String author, String message, Date timestamp) {
        IrcMessage ircMessage = new IrcMessageEB();
        ircMessage.setAuthor(author);
        ircMessage.setMessage(message);
        ircMessage.setTimestamp(timestamp);
        ircMessage.setMessageType(messageType);
        return ircMessage;
    }
}
