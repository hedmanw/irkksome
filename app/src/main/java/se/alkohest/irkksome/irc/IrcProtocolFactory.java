package se.alkohest.irkksome.irc;

/**
 * Created by oed on 7/18/14.
 */
public class IrcProtocolFactory {

    public static IrcProtocol getIrcProtocol(ConnectionData data) {
        if (data.isUseSSH()) {
            return new IrcProtocolAdapter(getSSHConnection(data));
        }
        return new IrcProtocolAdapter(new NormalConnection(data.getHost(), data.getPort()));
    }

    private static Connection getSSHConnection(ConnectionData data) {
        return new SSHConnection(data.getSshHost(), data.getSshUser(), data.getSshPass(),
                data.getHost(), data.getPort(), NormalConnection.class);
    }
}
