package se.alkohest.irkksome.irc;

/**
 * This interface is used to communicate with IRC.
 */
public interface IrcProtocol {

    /**
     * Connect to the specified server with this identity.
     *
     * @param nick - the nick to use
     * @param login - the login to use
     * @param realName - the users realname
     */
    public void connect(String nick, String login, String realName);

    /**
     * Connect to the specified server with this identity and password.
     *
     * @param nick - the nick to use
     * @param login - the login to use
     * @param realName - the users realname
     * @param password - the password to use
     */
    public void connect(String nick, String login, String realName, String password);

    /**
     * Disconnect from the current server.
     *
     * @param message - the message to be displayed when quiting
     */
    public void disconnect(String message);

    /**
     * Join a channel.
     * @param channel - the channel to join
     */
    public void joinChannel(String channel);

    /**
     * Join a channel that requires a key.
     * @param channel - the channel to join
     * @param key - the key to use
     */
    public void joinChannel(String channel, String key);

    /**
     * Part from a channel.
     * @param channel - the channel to part
     */
    public void partChannel(String channel);

    /**
     * Set or change the nickname.
     *
     * @param nick - the nick to use
     */
    public void setNick(String nick);

    /**
     * Sends a message to the server.
     * @param channel - the channel to send message to
     * @param message - the message to send.
     */
    public void sendChannelMessage(String channel, String message);

    /**
     * Send request to list the users in the given channel.
     * @param channel - the channel to check
     */
    public void getUsers(String channel);

    /**
     * Send a request to get all channels on the server.
     */
    public void listChannels();

    /**
     * Send request to get whois info.
     * @param nick - the nick to get info for
     */
    public void whois(String nick);

    /**
     * Invite a user to a channel.
     * @param nick
     * @param channel
     */
    public void invite(String nick, String channel);

    public void addListener(IrcProtocolListener listener);

    public void removeListener(IrcProtocolListener listener);

}
