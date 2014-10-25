package se.alkohest.irkksome.model.api.local;

import java.util.Date;
import java.util.List;

import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcChatMessageEB;

public interface IrcMessageDAOLocal {
    public IrcChatMessageEB create(IrcUser author, String message, Date timestamp);
}
