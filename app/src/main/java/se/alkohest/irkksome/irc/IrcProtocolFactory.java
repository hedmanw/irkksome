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
        return new IrcProtocolAdapter(new NormalConnection(data.getHost(), data.getPort()), bh);
    }

    private static Connection getSSHConnection(ConnectionData data) {
        return new SSHConnection(data, NormalConnection.class);
    }
}
