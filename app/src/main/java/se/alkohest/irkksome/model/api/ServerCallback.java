package se.alkohest.irkksome.model.api;

import java.util.List;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;

/**
 * Created by oed on 7/19/14.
 */
public interface ServerCallback {

    public void showServerInfo(IrcServer server, String motd);

    public void serverDisconnected();

    public void setActiveChannel(IrcChannel channel);

    public void messageReceived(IrcMessage message);

    public void error(String message);

    public void updateUserList();

    public void updateHilights();

    public void postInfoMessage(IrcMessage message, int color);
}
