package se.alkohest.irkksome.model.api.local;

import java.util.List;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;

public interface IrcChannelDAOLocal {
    public IrcChannel create(String name);

    public void addMessage(IrcChannel channel, IrcMessage message);

    public boolean compare(IrcChannel c, String channelName);

    public void addUser(IrcChannel channel, IrcUser user, String flag);

    public String removeUser(IrcChannel channel, IrcUser user);

    public void makeAllPersistent(List<IrcChannel> channels, long serverPK);
}
