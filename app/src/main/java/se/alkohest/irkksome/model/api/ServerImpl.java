package se.alkohest.irkksome.model.api;

import java.util.Date;
import java.util.List;

import se.alkohest.irkksome.irc.ConnectionData;
import se.alkohest.irkksome.irc.IrcProtocol;
import se.alkohest.irkksome.irc.IrcProtocolFactory;
import se.alkohest.irkksome.irc.IrcProtocolListener;
import se.alkohest.irkksome.irc.IrcProtocolStrings;
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
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;

public class ServerImpl implements Server, IrcProtocolListener {
    private IrcProtocol ircProtocol;
    private IrcServer ircServer;
    private ConnectionData connectionData;
    private HilightHelper hilightHelper;
    private IrcChannelDAOLocal channelDAO = new IrcChannelDAO();
    private IrcMessageDAOLocal messageDAO = new IrcMessageDAO();
    private IrcServerDAOLocal serverDAO = new IrcServerDAO();
    private IrcUserDAOLocal userDAO = new IrcUserDAO();
    private ServerCallback listener;
    private String motd = "";

    private IrcChannel activeChannel;
    private ServerConnectionListener connectionListener;
    private HilightListener hilightListener;

    public ServerImpl(IrcServer ircServer, ConnectionData data) {
        this.connectionData = data;
        this.ircServer = ircServer;
        ircProtocol = IrcProtocolFactory.getIrcProtocol(data);
        ircProtocol.setListener(this);
        ircProtocol.connect(data.getNickname(), data.getUsername(), data.getRealname(), data.getPassword());
        hilightHelper = new HilightHelper();
        // TODO - fix dynamic hilights
        hilightHelper.addHilight(data.getNickname());
    }

    @Override
    public IrcServer getBackingBean() {
        return ircServer;
    }

    @Override
    public ConnectionData getConnectionData() {
        return connectionData;
    }

    @Override
    public void setListener(ServerCallback listener) {
        this.listener = listener;
    }

    @Override
    public void joinChannel(String channelName) {
        ircProtocol.joinChannel((channelName.charAt(0) == '#' ? "" : "#") + channelName);
    }

    @Override
    public void changeNick(String nick) {
        ircProtocol.setNick(nick);
    }

    @Override
    public String getMotd() {
        return motd;
    }

    @Override
    public void disconnect() {
        // TODO - fix custom message
        ircProtocol.disconnect("irkksome <3");
    }

    @Override
    public void setServerDisconnectionListener(ServerConnectionListener listener) {
        connectionListener = listener;
    }

    @Override
    public void setHilightListener(HilightListener listener) {
        hilightListener = listener;
    }

    @Override
    public void leaveChannel(IrcChannel channel) {
        if(channel.getName().charAt(0) == '#') {
            ircProtocol.partChannel(channel.getName());
        }
        serverDAO.removeChannel(ircServer, channel);
        List<IrcChannel> channels = ircServer.getConnectedChannels();
        if (channels.size() != 0) {
            activeChannel = channels.get(channels.size() - 1);
            listener.setActiveChannel(activeChannel);
        } else {
            activeChannel = null;
            listener.showServerInfo(ircServer, motd);
        }
    }

    @Override
    public void sendMessage(IrcChannel channel, String message) {
        ircProtocol.sendChannelMessage(channel.getName(), message);
        IrcMessage ircMessage = messageDAO.create(IrcMessage.MessageTypeEnum.SENT, ircServer.getSelf(), message, new Date());

        channelDAO.addMessage(channel, ircMessage);
        listener.messageReceived(ircMessage);
    }

    @Override
    public java.util.Set<IrcUser> getUsers() {
        return ircServer.getKnownUsers();
    }

    @Override
    public IrcChannel getActiveChannel() {
        return activeChannel;
    }

    @Override
    public void setActiveChannel(IrcChannel ircChannel) {
        if (activeChannel != ircChannel) {
            listener.setActiveChannel(ircChannel);
            listener.updateHilights();
            activeChannel = ircChannel;
        }
    }

    @Override
    public void startQuery(String nick) {
        IrcUser user = serverDAO.getUser(ircServer, nick);
        IrcChannel query = serverDAO.getChannel(ircServer, nick);
        channelDAO.addUser(query, user, "");
        listener.setActiveChannel(query);
        activeChannel = query;
    }

    @Override
    public void showServer() {
        listener.showServerInfo(ircServer, motd);
        activeChannel = null;
    }

    //    ---------------------------------------------------------

    @Override
    public void serverConnectionEstablished() {
        connectionListener.connectionEstablished(this);
        // this should perhaps only be done when we know we connect to irrsi or another backlog provider
        ircProtocol.sendBacklogRequest(ircServer.getLastMessageTime().getTime() / 1000);
    }

    @Override
    public void serverRegistered(String server, String nick) {
        // TODO - this should maybe check if your nick has changed and change it instead of creating new
        ircServer.setSelf(serverDAO.getUser(ircServer, nick));
        showServer();
        for (IrcChannel channel : getBackingBean().getConnectedChannels()) {
            joinChannel(channel.getName());
        }
    }

    @Override
    public void nickChanged(String oldNick, String newNick, Date time) {
        if (userDAO.compare(ircServer.getSelf(), oldNick)) {
            ircServer.getSelf().setName(newNick);
            final IrcMessage message = messageDAO.create(IrcMessage.MessageTypeEnum.NICKCHANGE, ircServer.getSelf(), "You are now known as " + newNick, time);
            for (IrcChannel channel : ircServer.getConnectedChannels()) {
                channelDAO.addMessage(channel, message);
                if (channel == getActiveChannel()) {
                    listener.messageReceived(message);
                }
            }
        } else {
            IrcUser user = serverDAO.getUser(ircServer, oldNick);
            final IrcMessage message = messageDAO.create(IrcMessage.MessageTypeEnum.NICKCHANGE, user, oldNick + " is now known as " + newNick, time);
            updateChannelsWithUser(user, message, newNick);
            listener.updateUserList();
        }
        ircServer.setLastMessageTime(time);
    }

    private void updateChannelsWithUser(IrcUser user, IrcMessage message, String newNick) {
        boolean refreshUI = false;
        final List<IrcChannel> connectedChannels = ircServer.getConnectedChannels();
        for (IrcChannel channel : connectedChannels) {
            if (channelDAO.hasUser(channel, user)) {
                channelDAO.removeUser(channel, user);
                user.setName(newNick);
                channelDAO.addUser(channel, user, "");
                channelDAO.addMessage(channel, message);
                if (getActiveChannel() == channel) {
                    refreshUI = true;
                }
            }
        }
        if (refreshUI) {
            listener.messageReceived(message);
        }
    }

    @Override
    public void usersInChannel(String channelName, List<String> users) {
        IrcChannel channel = serverDAO.getChannel(ircServer, channelName);
        for (String userName : users) {
            String flag = "";
            if (hasFlag(userName)) {
                flag = String.valueOf(userName.charAt(0));
                userName = userName.substring(1);
            }
            IrcUser user = serverDAO.getUser(ircServer, userName);
            channelDAO.addUser(channel, user, flag);
            serverDAO.addUser(ircServer, user);
        }
        checkUserUpdate(channel);
    }

    private void checkUserUpdate(IrcChannel channel) {
        if (activeChannel == null ||
                channel.equals(activeChannel)) {
            listener.updateUserList();
        }
    }

    private boolean hasFlag(String user) {
        return user.startsWith(IrcProtocolStrings.FLAG_HALFOP) ||
                user.startsWith(IrcProtocolStrings.FLAG_OP) ||
                user.startsWith(IrcProtocolStrings.FLAG_OWNER) ||
                user.startsWith(IrcProtocolStrings.FLAG_SUPEROP) ||
                user.startsWith(IrcProtocolStrings.FLAG_VOICE);
    }

    @Override
    public void userJoined(String channelName, String nick, Date time) {
        IrcChannel channel = serverDAO.getChannel(ircServer, channelName);
        if (userDAO.compare(ircServer.getSelf(), nick)) {
            listener.setActiveChannel(channel);
            activeChannel = channel;
        } else {
            IrcUser user = serverDAO.getUser(ircServer, nick);
            channelDAO.addUser(channel, user, "");
            final IrcMessage message = messageDAO.create(IrcMessage.MessageTypeEnum.JOIN, user, nick + " joined the channel.", time);
            channelDAO.addMessage(channel, message);
            if (channel == activeChannel) {
                listener.messageReceived(message);
            }
        }
        checkUserUpdate(channel);
        ircServer.setLastMessageTime(time);
    }

    @Override
    public void userParted(String channelName, String nick, Date time) {
        IrcChannel channel = serverDAO.getChannel(ircServer, channelName);
        if (!userDAO.compare(ircServer.getSelf(), nick)) {
            IrcUser user = serverDAO.getUser(ircServer, nick);
            channelDAO.removeUser(channel, user);
            IrcMessage message = messageDAO.create(IrcMessage.MessageTypeEnum.PART, user, nick + " left the channel.", time);
            channelDAO.addMessage(channel, message);
            if (channel == getActiveChannel()) {
                listener.messageReceived(message);
            }
            checkUserUpdate(channel);
        } else {
            leaveChannel(channel);
        }
        ircServer.setLastMessageTime(time);
    }

    @Override
    public void userQuit(String nick, String quitMessage, Date time) {
        IrcUser user = serverDAO.getUser(ircServer, nick);
        serverDAO.removeUser(ircServer, user);

        final IrcMessage message = messageDAO.create(IrcMessage.MessageTypeEnum.QUIT, user, nick + " quit. (" + quitMessage + ")", time);
        for (IrcChannel channel : ircServer.getConnectedChannels()) {
            if (channelDAO.hasUser(channel, user)) {
                channelDAO.removeUser(channel, user);
                channelDAO.addMessage(channel, message);
                if (channel == getActiveChannel()) {
                    listener.messageReceived(message);
                }
            }
        }

        ircServer.setLastMessageTime(time);
        listener.updateUserList();
    }

    @Override
    public void channelMessageReceived(String channel, String user, String message, Date time) {
        IrcUser ircUser = serverDAO.getUser(ircServer, user);
        IrcMessage ircMessage = messageDAO.create(IrcMessage.MessageTypeEnum.RECEIVED, ircUser, message, time);
        IrcChannel ircChannel;

        // Hilightlogiken ska flyttas till hilights
        if (ircServer.getSelf().getName().equals(channel)) {
            ircChannel = serverDAO.getChannel(ircServer, user);
            ircMessage.setHilight(true);
        } else {
            ircChannel = serverDAO.getChannel(ircServer, channel);
            ircMessage.setHilight(hilightHelper.checkMessage(message));
        }

        if (!ircChannel.equals(activeChannel)) {
            UnreadEntity entity = new UnreadEntity(ircChannel, ircServer);
            hilightListener.addUnread(entity, ircMessage.isHilight());
            listener.updateHilights();
        }

        ircServer.setLastMessageTime(time);
        channelDAO.addMessage(ircChannel, ircMessage);
        if (ircChannel == activeChannel && !ircProtocol.isBacklogReplaying()) {
            listener.messageReceived(ircMessage);
        }
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
    public void motdReceived(String motd) {
        this.motd = motd;
    }

    @Override
    public void couldNotEstablish(String techMessage) {
        connectionListener.connectionDropped(this);
    }

    @Override
    public void serverDisconnected() {
        // TODO - this method should try to reconnect if its appropriate?
        connectionListener.connectionDropped(this);
        listener.serverDisconnected();
    }

    @Override
    public void ircError(String errorCode, String message) {
        listener.error(message);
        if ((errorCode.equals(IrcProtocolStrings.ERR_NICKNAMEINUSE) ||
                errorCode.equals(IrcProtocolStrings.ERR_YOUREBANNEDCREEP) ||
                errorCode.equals(IrcProtocolStrings.ERR_ERRONEUSNICKNAME)) &&
                                        ircServer.getSelf() == null) {
            disconnect();
        }
    }
}
