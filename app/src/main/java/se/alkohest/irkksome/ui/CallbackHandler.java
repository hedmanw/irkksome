package se.alkohest.irkksome.ui;

import android.content.Context;

import se.alkohest.irkksome.model.api.ServerCallback;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcUser;

/**
 * Created by oed on 7/20/14.
 */
public class CallbackHandler implements ServerCallback{
    public CallbackHandler(Context context) {
    }

    @Override
    public void serverConnected() {

    }

    @Override
    public void serverDisconnected() {

    }

    @Override
    public void channelJoined(IrcChannel channel) {

    }

    @Override
    public void channelJoinFailed() {

    }

    @Override
    public void userJoinedChannel(IrcChannel channel, IrcUser user) {

    }
}
