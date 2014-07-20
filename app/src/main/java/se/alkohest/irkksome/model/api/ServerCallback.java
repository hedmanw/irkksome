package se.alkohest.irkksome.model.api;

import java.util.List;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcUser;

/**
 * Created by oed on 7/19/14.
 */
public interface ServerCallback {

    public void serverConnected();

    public void serverDisconnected();

    public void setActiveChannel(IrcChannel channel);

    public void channelJoinFailed();

    public void userJoinedChannel(IrcChannel channel, IrcUser user);

    public void messageReceived();

    public void nickChanged(String oldNick, IrcUser user);

    public void userLeftChannel(IrcChannel channel, IrcUser user);

    public void userQuit(IrcUser user, List<IrcChannel> channels);
}
