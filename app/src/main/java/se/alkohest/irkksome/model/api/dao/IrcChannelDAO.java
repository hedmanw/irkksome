package se.alkohest.irkksome.model.api.dao;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcChannelEB;

public class IrcChannelDAO {

    public IrcChannel create(String name) {
        IrcChannel ircChannel = new IrcChannelEB();
        ircChannel.setName(name);
        ircChannel.setMessages(new ArrayList<IrcMessage>());
        ircChannel.setUsers(new ConcurrentHashMap<IrcUser, String>());
        return ircChannel;
    }

    public void addMessage(IrcChannel channel, IrcMessage message) {
        channel.getMessages().add(message);
    }

    public boolean compare(IrcChannel c, String channelName) {
        return c.getName().toLowerCase().equals(channelName.toLowerCase());
    }

    public void addUser(IrcChannel channel, IrcUser user, String flag) {
        channel.getUsers().put(user, flag);
    }

    public boolean hasUser(IrcChannel channel, IrcUser user) {

        final boolean b = channel.getUsers().keySet().contains(user);
        return b;
    }

    public boolean removeUser(IrcChannel channel, IrcUser user) {
        return channel.getUsers().remove(user) != null;
    }
}
