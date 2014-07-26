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
    private IrcUserDAO userDAO = new IrcUserDAO();

    @Override
    public IrcMessage create(IrcUser author, String message, Date timestamp) {
        IrcMessage ircMessage = new IrcMessageEB();
        ircMessage.setAuthor(author);
        ircMessage.setMessage(message);
        ircMessage.setTimestamp(timestamp);
        return ircMessage;
    }

    @Override
    public IrcMessage findById(long id) {
        return findById(IrcMessageEB.class, id);
    }

    @Override
    protected IrcMessage initFromCursor(Cursor cursor) {
        IrcMessage message = create(userDAO.findById(cursor.getLong(2)), cursor.getString(1), new Date());
        return message;
    }

    @Override
    public void makeAllPersistent(List<IrcMessage> messages, long channelPK) {
        for (IrcMessage message : messages) {
            makePersistent(message, channelPK);
        }
    }

    @Override
    public void makePersistent(IrcMessage beanEntity) {
        persist(beanEntity.getAuthor(), -1);
        super.makePersistent(beanEntity);
    }
}
