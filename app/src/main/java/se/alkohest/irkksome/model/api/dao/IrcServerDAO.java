package se.alkohest.irkksome.model.api.dao;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import se.alkohest.irkksome.model.api.local.IrcChannelDAOLocal;
import se.alkohest.irkksome.model.api.local.IrcServerDAOLocal;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcServerEB;
import se.alkohest.irkksome.orm.GenericDAO;

public class IrcServerDAO extends GenericDAO<IrcServerEB, IrcServer> implements IrcServerDAOLocal {
    private IrcChannelDAOLocal channelDAO = new IrcChannelDAO();
    private IrcUserDAO userDAO = new IrcUserDAO();

    @Override
    public IrcServer create(String host) {
        IrcServer ircServer = new IrcServerEB();
        ircServer.setConnectedChannels(new ArrayList<IrcChannel>());
        ircServer.setKnownUsers(new HashSet<IrcUser>());
        ircServer.setHost(host);
        ircServer.setLastMessageTime(new Date(0));
        return ircServer;
    }

    @Override
    public IrcServer findById(long id) {
        return findById(IrcServerEB.class, id);
    }

    @Override
    protected IrcServer initFromCursor(Cursor cursor, long pk) {
        IrcServer ircServer = create(cursor.getString(1));
        ircServer.setSelf(userDAO.findById(cursor.getLong(2)));
        ircServer.setConnectedChannels(channelDAO.findChannelsByServer(pk));
        return ircServer;
    }

    @Override
    public void addUser(IrcServer ircServer, IrcUser user) {
        ircServer.getKnownUsers().add(user);
    }

    @Override
    public IrcChannel getChannel(IrcServer ircServer, String channelName) {
        for (IrcChannel c : ircServer.getConnectedChannels()) {
            if (channelDAO.compare(c, channelName)) {
                return c;
            }
        }
        IrcChannel channel = channelDAO.create(channelName);
        ircServer.getConnectedChannels().add(channel);
        return channel;
    }

    @Override
    public IrcUser getUser(IrcServer ircServer, String nick) {
        for (IrcUser u : ircServer.getKnownUsers()) {
            if (userDAO.compare(u, nick)) {
                return u;
            }
        }
        IrcUser user = userDAO.create(nick);
        ircServer.getKnownUsers().add(user);
        return user;
    }

    @Override
    public void removeChannel(IrcServer ircServer, IrcChannel channel) {
        ircServer.getConnectedChannels().remove(channel);
    }

    @Override
    public void removeUser(IrcServer ircServer, IrcUser user) {
        ircServer.getKnownUsers().remove(user);
    }

    @Override
    public List<IrcServer> getAll() {
        return getAll(IrcServerEB.class);
    }

    @Override
    public void dropServer(IrcServer backingBean) {
        makeTransient(backingBean);
    }

    @Override
    public void makePersistent(IrcServer beanEntity) {
        userDAO.makePersistent(beanEntity.getSelf());
        super.makePersistent(beanEntity);
        channelDAO.makeAllPersistent(beanEntity.getConnectedChannels(), beanEntity.getId());
    }
}
