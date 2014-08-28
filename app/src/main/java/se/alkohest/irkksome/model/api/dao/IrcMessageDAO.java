package se.alkohest.irkksome.model.api.dao;

import android.database.Cursor;

import java.util.Date;
import java.util.List;

import se.alkohest.irkksome.model.api.local.IrcMessageDAOLocal;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcChatMessageEB;
import se.alkohest.irkksome.model.impl.IrcMessageEB;
import se.alkohest.irkksome.orm.GenericDAO;

public class IrcMessageDAO extends GenericDAO<IrcMessageEB, IrcMessage> implements IrcMessageDAOLocal {
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
    public IrcMessage findById(long id) {
        return findById(IrcMessageEB.class, id);
    }

    @Override
    protected IrcMessage initFromCursor(Cursor cursor, long pk) {
        // This will probably ball out when läsa upp från databasen
        // Must first init all IrcChatMessageEB and other subclasses and then
        // map them to their correct owner IrcMessageEB:s
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
    public List<IrcMessage> findMessagesByChannel(long channelPK) {
        return getAll(IrcMessageEB.class, "channel_id=?", channelPK);
    }

    @Override
    public void makePersistent(IrcMessage beanEntity) {
        if (beanEntity instanceof IrcChatMessageEB) {
            persist(((IrcChatMessageEB) beanEntity).getAuthor(), -1);
        }
        super.makePersistent(beanEntity);
    }
}
