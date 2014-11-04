package se.alkohest.irkksome.model.api.dao;

import java.util.Date;

import se.alkohest.irkksome.model.api.local.IrcMessageDAOLocal;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcMessageEB;

public class IrcMessageDAO implements IrcMessageDAOLocal {
    private IrcUserDAO userDAO = new IrcUserDAO();

    @Override
    public IrcMessage create(IrcMessage.MessageTypeEnum messageType, IrcUser author, String message, Date timestamp) {
        IrcMessage ircMessage = new IrcMessageEB();
        ircMessage.setAuthor(author);
        ircMessage.setMessage(message);
        ircMessage.setTimestamp(timestamp);
        ircMessage.setMessageType(messageType);
        return ircMessage;
    }
}
