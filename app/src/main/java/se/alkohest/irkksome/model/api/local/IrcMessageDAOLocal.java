package se.alkohest.irkksome.model.api.local;

import java.util.Date;

import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;

public interface IrcMessageDAOLocal {
    public IrcMessage create(IrcUser author, String message, Date timestamp);
}
