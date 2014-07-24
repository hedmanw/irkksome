package se.alkohest.irkksome.model.api.local;

import java.util.Date;
import java.util.List;

import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;

public interface IrcMessageDAOLocal {
    public IrcMessage create(IrcUser author, String message, Date timestamp);
    public void makeAllPersistent(List<IrcMessage> messages, long channelPK);
}
