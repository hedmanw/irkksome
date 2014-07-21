package se.alkohest.irkksome.model.api;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;

public interface Server {
    public void joinChannel(String channelName);

    public void leaveChannel(IrcChannel channel);

    public void sendMessage(IrcChannel channel, String message);

    public java.util.Set<IrcUser> getUsers();

    public IrcServer getBackingBean();

    public void setListener(ServerCallback listener);

    public IrcChannel getActiveChannel();

    void setActiveChannel(IrcChannel ircChannel);

    public void changeNick(String nick);

    public void disconnect();
}
