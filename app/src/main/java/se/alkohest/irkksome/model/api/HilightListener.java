package se.alkohest.irkksome.model.api;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.enumerations.HilightLevel;

/**
 * Created by wilhelm 2014-11-06.
 */
public interface HilightListener {
    public boolean addUnread(IrcServer ircServer, IrcChannel ircChannel, HilightLevel hilightLevel);
}
