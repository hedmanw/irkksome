package se.alkohest.irkksome.model.api.local;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;

public interface IrcChannelDAOLocal {
    public IrcChannel create();

    public void addMessage(IrcChannel channel, IrcMessage message);
}
