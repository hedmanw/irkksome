package se.alkohest.irkksome.irc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oed on 7/18/14.
 */
public class IrcProtocolAdapter implements IrcProtocol {

    private static final String BLANK = " ";
    private static final String COLON = ":";
    private static final String HASHTAG = "#";
    private static final String BANG = "!";

    private IrcProtocolListener listener;
    private Connection connection;

    /**
     * This method parses a reply and propagates either
     * a message or a channel message.
     */
    private void handleReply(String reply) {
        String[] parts = reply.split(BLANK, 3);
        // Too few parts means that reply is not a valid IRC string.
        if (parts.length < 2) return;
        handlePing(parts);

        switch (parts[1]) {
            case IrcProtocolStrings.PRIVMSG:
                handlePrivmsg(parts);
                break;
            case IrcProtocolStrings.JOIN:
                listener.userJoined(
                        parts[2].substring(parts[2].indexOf(COLON) + 1),
                        parts[0].substring(1, parts[0].indexOf(BANG)));
                break;
            case IrcProtocolStrings.PART:
                listener.userParted(
                        parts[2],
                        parts[0].substring(1, parts[0].indexOf(BANG)));
                break;
            case IrcProtocolStrings.QUIT:
                listener.userQuit(
                        parts[0].substring(1, parts[0].indexOf(BANG)),
                        parts[2].substring(parts[2].indexOf(COLON) + 1));
                break;
            case IrcProtocolStrings.NICK:
                listener.nickChanged(
                        parts[0].substring(1, parts[0].indexOf(BANG)),
                        parts[2].substring(parts[2].indexOf(COLON) + 1));
                break;
            case IrcProtocolStrings.RPL_WELCOME:
                listener.serverConnected(
                        parts[0].substring(1),
                        parts[2].substring(0, parts[2].indexOf(BLANK)));
                break;
            case IrcProtocolStrings.RPL_WHOISUSER:
                listener.whoisRealname(
                        parts[2].split(BLANK, 3)[1],
                        parts[2].substring(parts[2].lastIndexOf(COLON) + 1));
                break;
            case IrcProtocolStrings.RPL_WHOISIDLE:
                String[] idleData = parts[2].split(BLANK, 4);
                listener.whoisIdleTime(
                        idleData[1],
                        Integer.parseInt(idleData[2]));
                break;
            case IrcProtocolStrings.RPL_WHOISCHANNELS:
                listener.whoisChannels(
                        parts[2].split(BLANK, 3)[1],
                        Arrays.asList(parts[2].substring(
                                parts[2].lastIndexOf(COLON) + 1).split(BLANK)));
                break;
            case IrcProtocolStrings.RPL_LIST:
                String[] clrData = parts[2].split(BLANK,5);
                listener.channelListResponse(clrData[1], clrData[4], clrData[2]);
                break;
            case IrcProtocolStrings.RPL_NAMREPLY:
                int colonIndex = parts[2].indexOf(COLON);
                listener.usersInChannel(
                        parts[2].substring(parts[2].indexOf(HASHTAG), colonIndex - 1),
                        Arrays.asList(parts[2].substring(colonIndex + 1).split(BLANK)));
                break;
            default:
                try {
                    int errorCode = Integer.parseInt(parts[1]);
                    if (errorCode >= 400 && errorCode <= 599) {
                        listener.ircError(parts[1], parts[2]);
                    }
                } catch (NumberFormatException e) {}
                break;
        }
    }

    private void handlePing(String[] parts) {
        if (parts[0].equals(IrcProtocolStrings.PING)) {
            connection.write(IrcProtocolStrings.PONG + BLANK + parts[1]);
        }
    }

    private void handlePrivmsg(String[] parts) {
        String nick = parts[0].substring(1, parts[0].indexOf(BANG));
        String channel = parts[2].substring(0, parts[2].indexOf(BLANK));
        String msg = parts[2].substring(parts[2].indexOf(COLON) + 1);

        listener.channelMessageReceived(channel, nick, msg);
    }

    @Override
    public void connect(String nick, String login, String realName) {

    }

    @Override
    public void connect(String nick, String login, String realName, String password) {

    }

    @Override
    public void disconnect(String message) {

    }

    @Override
    public void joinChannel(String channel) {

    }

    @Override
    public void joinChannel(String channel, String key) {

    }

    @Override
    public void partChannel(String channel) {

    }

    @Override
    public void setNick(String nick) {

    }

    @Override
    public void sendChannelMessage(String channel, String message) {

    }

    @Override
    public void getUsers(String channel) {

    }

    @Override
    public void listChannels() {

    }

    @Override
    public void whois(String nick) {

    }

    @Override
    public void invite(String nick, String channel) {

    }

    @Override
    public void setListener(IrcProtocolListener listener) {
        this.listener = listener;
    }
}
