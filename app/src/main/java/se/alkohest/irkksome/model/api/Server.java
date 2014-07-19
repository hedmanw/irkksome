package se.alkohest.irkksome.model.api;

import java.util.List;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;

public interface Server {
    public IrcChannel joinChannel(String channelName);

    public void sendMessage(IrcChannel channel, String message);

    public List<IrcUser> getUsers();

    public IrcServer getBackingBean();

    public void setListener(ServerCallback listener);

}
