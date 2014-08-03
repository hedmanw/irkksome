package se.alkohest.irkksome.irc;

import java.util.Date;

/**
 * Created by oed on 8/2/14.
 */
public class NoBacklogHandler implements BacklogHandler {
    @Override
    public Date extractDate(String[] parts) {
        return new Date();
    }

    @Override
    public String getBacklogRequest(long unixTime) {
        return "";
    }
}
