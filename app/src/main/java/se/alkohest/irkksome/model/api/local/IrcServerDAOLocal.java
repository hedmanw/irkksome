package se.alkohest.irkksome.model.api.local;

import java.util.List;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcServerEB;

public interface IrcServerDAOLocal {
    public IrcServer create(String host);

    public IrcChannel getChannel(IrcServer ircServer, String channelName);

    public IrcUser getUser(IrcServer ircServer, String nick);

    public void addUser(IrcServer ircServer, IrcUser user);

    public void removeUser(IrcServer ircServer, IrcUser user);

    public void removeChannel(IrcServer ircServer, IrcChannel channel);

    public List<IrcServerEB> getAll();
}
