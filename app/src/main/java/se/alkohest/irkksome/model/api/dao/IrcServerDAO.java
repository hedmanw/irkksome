package se.alkohest.irkksome.model.api.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcServerEB;
import se.alkohest.irkksome.orm.GenericDAO;

public class IrcServerDAO extends GenericDAO<IrcServerEB> {
    private IrcChannelDAO channelDAO = new IrcChannelDAO();
    private IrcUserDAO userDAO = new IrcUserDAO();

    public IrcServer create() {
        IrcServer ircServer = new IrcServerEB();
        ircServer.setConnectedChannels(new ArrayList<IrcChannel>());
        ircServer.setKnownUsers(new HashSet<String>());
        ircServer.setLastMessageTime(new Date(0));
        return ircServer;
    }

    public void addUser(IrcServer ircServer, String user) {
        ircServer.getKnownUsers().add(user);
    }

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

    public String getUser(IrcServer ircServer, String nick) {
        for (String userName : ircServer.getKnownUsers()) {
            if (userName.equalsIgnoreCase(nick)) {
                return userName;
            }
        }
        ircServer.getKnownUsers().add(nick);
        return nick;
    }

    public void removeChannel(IrcServer ircServer, IrcChannel channel) {
        ircServer.getConnectedChannels().remove(channel);
    }

    public void removeUser(IrcServer ircServer, String user) {
        ircServer.getKnownUsers().remove(user);
    }

    @Override
    public List<IrcServerEB> getAll() {
        return super.getAll();
    }

    @Override
    protected Class<IrcServerEB> getEntityBean() {
        return IrcServerEB.class;
    }
}
