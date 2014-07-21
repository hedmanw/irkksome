package se.alkohest.irkksome.model.api.dao;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.HashMap;

import se.alkohest.irkksome.model.api.local.IrcChannelDAOLocal;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcChannelEB;
import se.alkohest.irkksome.orm.GenericDAO;

public class IrcChannelDAO extends GenericDAO<IrcChannelEB, IrcChannel> implements IrcChannelDAOLocal {
    @Override
    protected ContentValues createContentValues(IrcChannel beanEntity) {
        return null;
    }

    @Override
    public IrcChannel create(String name) {
        IrcChannel ircChannel = new IrcChannelEB();
        ircChannel.setName(name);
        ircChannel.setMessages(new ArrayList<IrcMessage>());
        ircChannel.setUsers(new HashMap<IrcUser, String>());
        return ircChannel;
    }

    @Override
    public void findById(long id) {

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
}
