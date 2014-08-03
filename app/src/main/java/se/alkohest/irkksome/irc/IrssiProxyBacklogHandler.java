package se.alkohest.irkksome.irc;

import java.util.Date;

/**
 * Created by oed on 8/2/14.
 */
public class IrssiProxyBacklogHandler implements BacklogHandler {
    public static final String BLANK = " ";
    public static final String PROXY = "PROXY";
    public static final String BACKLOG = "backlog";
    public static final String START = "start";
    public static final String STOP = "stop";

    private boolean backlogReplaying;

    public IrssiProxyBacklogHandler() {
        backlogReplaying = false;
    }

    @Override
    public Date extractDate(String[] parts) {
        if (parts[1].equals(PROXY)) {
            switch (parts[2]) {
                case START:
                    backlogReplaying = true;
                    break;
                case STOP:
                    backlogReplaying = false;
                    break;
            }
        } else if (backlogReplaying) {
            try {
                int index = parts[2].lastIndexOf(':');
                long unixTime = Long.parseLong(parts[2].substring(index + 1));
                parts[2] = parts[2].substring(0, index - 1);
                return new Date(unixTime*1000);
            } catch (NumberFormatException e) {}
        }
        return new Date();
    }

    @Override
    public String getBacklogRequest(long unixTime) {
        return PROXY + BLANK + BACKLOG + BLANK + unixTime;
    }
}
