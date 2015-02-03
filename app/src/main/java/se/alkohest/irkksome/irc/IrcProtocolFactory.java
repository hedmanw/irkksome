package se.alkohest.irkksome.irc;

/**
 * Created by oed on 7/18/14.
 */
public class IrcProtocolFactory {

    public static IrcProtocol getIrcProtocol(ConnectionData data) {
        // for now..
        BacklogHandler bh = new IrssiProxyBacklogHandler();
        if (data.isUseSSH()) {
            return new IrcProtocolAdapter(getSSHConnection(data), bh);
        }
        return new IrcProtocolAdapter(getNormalConnection(data), bh);
    }

    private static ServerConnection getSSHConnection(ConnectionData data) {
        return new SSHConnection(data);
    }

    private static ServerConnection getNormalConnection(ConnectionData data) {
        return new NormalConnection(data.getHost(), data.getPort());
    }
}
