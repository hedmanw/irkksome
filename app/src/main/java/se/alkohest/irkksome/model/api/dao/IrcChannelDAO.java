package se.alkohest.irkksome.model.api.dao;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.alkohest.irkksome.model.api.local.IrcChannelDAOLocal;
import se.alkohest.irkksome.model.api.local.IrcMessageDAOLocal;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcChannelEB;
import se.alkohest.irkksome.orm.GenericDAO;

public class IrcChannelDAO extends GenericDAO<IrcChannelEB, IrcChannel> implements IrcChannelDAOLocal {
    private IrcMessageDAOLocal messageDAO = new IrcMessageDAO();

    @Override
    public IrcChannel create(String name) {
        IrcChannel ircChannel = new IrcChannelEB();
        ircChannel.setName(name);
        ircChannel.setMessages(new ArrayList<IrcMessage>());
        ircChannel.setUsers(new HashMap<IrcUser, String>());
        return ircChannel;
    }

    @Override
    public IrcChannel findById(long id) {
        return findById(IrcChannelEB.class, id);
    }

    @Override
    protected IrcChannel initFromCursor(Cursor cursor, long pk) {
        IrcChannel channel = create(cursor.getString(2));
        channel.getMessages().addAll(messageDAO.findMessagesByChannel(pk));
        return channel;
    }

    @Override
    public void addMessage(IrcChannel channel, IrcMessage message) {
        channel.getMessages().add(message);
    }

    @Override
    public boolean compare(IrcChannel c, String channelName) {
        return c.getName().toLowerCase().equals(channelName.toLowerCase());
    }

    @Override
    public void addUser(IrcChannel channel, IrcUser user, String flag) {
        channel.getUsers().put(user, flag);
    }

    @Override
    public String removeUser(IrcChannel channel, IrcUser user) {
        return channel.getUsers().remove(user);
    }

    @Override
    public void makeAllPersistent(List<IrcChannel> channels, long serverPK) {
        for (IrcChannel channel : channels) {
            makePersistent(channel, serverPK);
        }
    }

    @Override
    public List<IrcChannel> findChannelsByServer(long serverId) {
        return getAll(IrcChannelEB.class, "server_id=?", serverId);
    }

    @Override
    public void makePersistent(IrcChannel beanEntity) {
        super.makePersistent(beanEntity);
        messageDAO.makeAllPersistent(beanEntity.getMessages(), beanEntity.getId());
    }
}
