package se.alkohest.irkksome.model.api;

import java.util.Stack;

/**
 * Created by oed on 7/21/14.
 */
public interface ServerDropAcidListener {

    public void addUnread(UnreadEntity entity, boolean isHilight);

    public void dropServer(Server server);

}
