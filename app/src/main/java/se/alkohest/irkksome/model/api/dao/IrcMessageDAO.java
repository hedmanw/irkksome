package se.alkohest.irkksome.model.api.dao;

import java.util.Date;

import se.alkohest.irkksome.model.api.local.IrcMessageDAOLocal;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcChatMessageEB;
import se.alkohest.irkksome.model.impl.IrcMessageEB;

public class IrcMessageDAO implements IrcMessageDAOLocal {
    private IrcUserDAO userDAO = new IrcUserDAO();

    @Override
    public IrcChatMessageEB create(IrcUser author, String message, Date timestamp) {
        IrcChatMessageEB ircMessage = new IrcChatMessageEB();
        ircMessage.setAuthor(author);
        ircMessage.setMessage(message);
        ircMessage.setTimestamp(timestamp);
        return ircMessage;
    }

    @Override
    public IrcMessage create(String message, Date timestamp) {
        IrcMessage ircMessage = new IrcMessageEB();
        ircMessage.setMessage(message);
        ircMessage.setTimestamp(timestamp);
        return ircMessage;
    }
}
