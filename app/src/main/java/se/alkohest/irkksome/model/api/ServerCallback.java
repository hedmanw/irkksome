package se.alkohest.irkksome.model.api;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcUser;

/**
 * Created by oed on 7/19/14.
 */
public interface ServerCallback {

    public void serverConnected();

    public void serverDisconnected();

    public void channelJoined(IrcChannel channel);

    public void channelJoinFailed();

    public void userJoinedChannel(IrcChannel channel, IrcUser user);

    public void messageRecived();
}
