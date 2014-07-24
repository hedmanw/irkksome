package se.alkohest.irkksome.model.api.dao;

import android.database.Cursor;

import java.util.Date;
import java.util.List;

import se.alkohest.irkksome.model.api.local.IrcMessageDAOLocal;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcMessageEB;
import se.alkohest.irkksome.orm.GenericDAO;

public class IrcMessageDAO extends GenericDAO<IrcMessageEB, IrcMessage> implements IrcMessageDAOLocal {
    @Override
    public IrcMessage create(IrcUser author, String message, Date timestamp) {
        IrcMessage ircMessage = new IrcMessageEB();
        ircMessage.setAuthor(author);
        ircMessage.setMessage(message);
        ircMessage.setTimestamp(timestamp);
        return ircMessage;
    }

    @Override
    public void findById(long id) {

    }

    @Override
    protected IrcMessage initFromCursor(Cursor cursor) {
        return null;
    }

    @Override
    public void makeAllPersistent(List<IrcMessage> messages, long channelPK) {
        for (IrcMessage message : messages) {
            makePersistent(message, channelPK);
        }
    }
}
