package se.alkohest.irkksome.model.api.local;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;

public interface IrcChannelDAOLocal {
    public IrcChannel create(String name);

    public void addMessage(IrcChannel channel, IrcMessage message);

    public boolean compare(IrcChannel c, String channelName);

    public void addUser(IrcChannel channel, IrcUser user, String flag);

    public boolean hasUser(IrcChannel channel, IrcUser user);

    public boolean removeUser(IrcChannel channel, IrcUser user);
}
