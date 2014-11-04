package se.alkohest.irkksome.model.api.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import se.alkohest.irkksome.model.api.local.IrcChannelDAOLocal;
import se.alkohest.irkksome.model.api.local.IrcMessageDAOLocal;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcChannelEB;
import se.alkohest.irkksome.model.impl.IrcUserEB;

public class IrcChannelDAO implements IrcChannelDAOLocal {
    private IrcMessageDAOLocal messageDAO = new IrcMessageDAO();

    @Override
    public IrcChannel create(String name) {
        IrcChannel ircChannel = new IrcChannelEB();
        ircChannel.setName(name);
        ircChannel.setMessages(new ArrayList<IrcMessage>());
        ircChannel.setUsers(new ConcurrentHashMap<IrcUser, String>());
        return ircChannel;
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
    public boolean hasUser(IrcChannel channel, IrcUser user) {

        final boolean b = channel.getUsers().keySet().contains(user);
        return b;
    }

    @Override
    public boolean removeUser(IrcChannel channel, IrcUser user) {
        return channel.getUsers().remove(user) != null;
    }
}
