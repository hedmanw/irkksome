package se.alkohest.irkksome.irc;

/**
 * Created by oed on 7/18/14.
 */
public class IrcProtocolFactory {

    public static IrcProtocol getIrcProtocol(String host, int port) {
        return new IrcProtocolAdapter(new NormalConnection(host, port));
//        return new IrcProtocolAdapter(new SSHConnection("hubben.chalmers.it", "joelto", "lösen här",
//                "localhost", 50001, NormalConnection.class));
    }
}
