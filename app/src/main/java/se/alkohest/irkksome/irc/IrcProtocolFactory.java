package se.alkohest.irkksome.irc;

import se.alkohest.irkksome.model.entity.IrkksomeConnection;

/**
 * Created by oed on 7/18/14.
 */
public class IrcProtocolFactory {

    public static IrcProtocol getIrcProtocol(IrkksomeConnection data) {
        // for now..
        BacklogHandler bh = new IrssiProxyBacklogHandler();
        if (data.isUseSSH()) {
            return new IrcProtocolAdapter(getSSHConnection(data), bh);
        }
        return new IrcProtocolAdapter(getNormalConnection(data), bh);
    }

    private static ServerConnection getSSHConnection(IrkksomeConnection data) {
        return new SSHIrkkForwarder(data);
    }

    private static ServerConnection getNormalConnection(IrkksomeConnection data) {
        return new NormalConnection(data.getHost(), data.getPort());
    }
}
