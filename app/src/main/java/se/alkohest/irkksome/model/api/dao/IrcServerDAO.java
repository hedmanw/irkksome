package se.alkohest.irkksome.model.api.dao;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.HashSet;

import se.alkohest.irkksome.model.api.local.IrcChannelDAOLocal;
import se.alkohest.irkksome.model.api.local.IrcServerDAOLocal;
import se.alkohest.irkksome.model.api.local.IrcUserDAOLocal;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcServerEB;
import se.alkohest.irkksome.orm.GenericDAO;

public class IrcServerDAO extends GenericDAO<IrcServerEB, IrcServer> implements IrcServerDAOLocal {
    private IrcChannelDAOLocal channelDAO = new IrcChannelDAO();
    private IrcUserDAOLocal userDAO = new IrcUserDAO();

    @Override
    public IrcServer create(String host) {
        IrcServer ircServer = new IrcServerEB();
        ircServer.setConnectedChannels(new ArrayList<IrcChannel>());
        ircServer.setKnownUsers(new HashSet<IrcUser>());
        ircServer.setUrl(host);
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
    protected ContentValues createContentValues(IrcServer beanEntity) {
        return null;
    }

    @Override
    public void removeChannel(IrcServer ircServer, IrcChannel channel) {
        ircServer.getConnectedChannels().remove(channel);
    }

    @Override
    public void removeUser(IrcServer ircServer, IrcUser user) {
        ircServer.getKnownUsers().remove(user);
    }
}
