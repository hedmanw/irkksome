package se.alkohest.irkksome.model.api;

import java.util.Date;
import java.util.List;

import se.alkohest.irkksome.irc.IrcProtocol;
import se.alkohest.irkksome.irc.IrcProtocolFactory;
import se.alkohest.irkksome.irc.IrcProtocolListener;
import se.alkohest.irkksome.model.api.dao.IrcChannelDAO;
import se.alkohest.irkksome.model.api.dao.IrcMessageDAO;
import se.alkohest.irkksome.model.api.dao.IrcServerDAO;
import se.alkohest.irkksome.model.api.dao.IrcUserDAO;
import se.alkohest.irkksome.model.api.local.IrcChannelDAOLocal;
import se.alkohest.irkksome.model.api.local.IrcMessageDAOLocal;
import se.alkohest.irkksome.model.api.local.IrcServerDAOLocal;
import se.alkohest.irkksome.model.api.local.IrcUserDAOLocal;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;

public class ServerImpl implements Server, IrcProtocolListener {
    private IrcProtocol ircProtocol;
    private IrcServer ircServer;
    private IrcChannelDAOLocal channelDAO = new IrcChannelDAO();
    private IrcMessageDAOLocal messageDAO = new IrcMessageDAO();
    private IrcServerDAOLocal serverDAO = new IrcServerDAO();
    private IrcUserDAOLocal userDAO = new IrcUserDAO();
    private ServerCallback listener;

    private IrcChannel activeChannel;

    public ServerImpl(IrcServer ircServer) {
        this.ircServer = ircServer;
        ircProtocol = IrcProtocolFactory.getIrcProtocol(ircServer.getUrl(), 6667);
        ircProtocol.setListener(this);
        ircProtocol.connect("fest", "banned", "banned");
    }

    @Override
    public IrcServer getBackingBean() {
        return ircServer;
    }

    @Override
    public void setListener(ServerCallback listener) {
        this.listener = listener;
    }

    @Override
    public void joinChannel(String channelName) {
        ircProtocol.joinChannel(channelName);
    }

    @Override
    public void sendMessage(IrcChannel channel, String message) {
        ircProtocol.sendChannelMessage(channel.getName(), message);
        IrcMessage ircMessage = messageDAO.create(ircServer.getSelf(), message, new Date());

        channelDAO.addMessage(channel, ircMessage);
        listener.messageRecived();
    }

    @Override
    public List<IrcUser> getUsers() {
        return ircServer.getKnownUsers();
    }

    @Override
    public IrcChannel getActiveChannel() {
        return activeChannel;
    }

//    ---------------------------------------------------------

    @Override
    public void serverConnected(String server, String nick) {
        this.ircServer.setSelf(userDAO.create(nick));
        listener.serverConnected();
    }

    @Override
    public void nickChanged(String oldNick, String newNick) {

    }

    @Override
    public void usersInChannel(String channelName, List<String> users) {

    }

    @Override
    public void userJoined(String channelName, String nick) {
        IrcChannel channel = serverDAO.getChannel(ircServer, channelName);
        if (userDAO.compare(ircServer.getSelf(), nick)) {
            listener.channelJoined(channel);
            activeChannel = channel;
        } else {
            IrcUser user = serverDAO.getUser(ircServer, nick);
            listener.userJoinedChannel(channel, user);
        }
    }

    @Override
    public void userParted(String channelName, String nick) {

    }

    @Override
    public void userQuit(String nick, String quitMessage) {

    }

    @Override
    public void channelMessageReceived(String channel, String user, String message) {
        IrcChannel ircChannel = serverDAO.getChannel(ircServer, channel);
        IrcUser ircUser = serverDAO.getUser(ircServer, user);
        IrcMessage ircMessage = messageDAO.create(ircUser, message, new Date());

        channelDAO.addMessage(ircChannel, ircMessage);
        listener.messageRecived();
    }

    @Override
    public void whoisChannels(String nick, List<String> channels) {

    }

    @Override
    public void whoisRealname(String nick, String realname) {

    }

    @Override
    public void whoisIdleTime(String nick, int seconds) {

    }

    @Override
    public void channelListResponse(String name, String topic, String users) {

    }

    @Override
    public void serverDisconnected() {
        // TODO - this method should try to reconnect if its appropriate?
        listener.serverDisconnected();
    }

    @Override
    public void ircError(String errorCode, String message) {

    }
}
