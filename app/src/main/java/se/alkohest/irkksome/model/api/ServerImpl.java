package se.alkohest.irkksome.model.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.alkohest.irkk.IrcProtocolFactory;
import se.alkohest.irkk.entity.BaseConnectionImpl;
import se.alkohest.irkk.entity.SSHConnectionImpl;
import se.alkohest.irkk.irc.IrcProtocol;
import se.alkohest.irkk.irc.IrcProtocolListener;
import se.alkohest.irkk.irc.IrcProtocolStrings;
import se.alkohest.irkksome.model.api.dao.IrcChannelDAO;
import se.alkohest.irkksome.model.api.dao.IrcMessageDAO;
import se.alkohest.irkksome.model.api.dao.IrcServerDAO;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.model.entity.SSHConnection;
import se.alkohest.irkksome.model.enumerations.HilightLevel;

public class ServerImpl implements Server, IrcProtocolListener {
    private IrcProtocol ircProtocol;
    private IrcServer ircServer;
    private IrkksomeConnection connectionData;
    private HilightManager hilightManager;
    private IrcChannelDAO channelDAO = new IrcChannelDAO();
    private IrcMessageDAO messageDAO = new IrcMessageDAO();
    private IrcServerDAO serverDAO = new IrcServerDAO();
    private ServerCallback listener;
    private String motd = "";

    private IrcChannel activeChannel;
    private List<ServerConnectionListener> connectionListeners = new ArrayList<>();
    private HilightListener hilightListener;

    public ServerImpl(IrcServer ircServer, IrkksomeConnection data) {
        this.connectionData = data;
        this.ircServer = ircServer;

        BaseConnectionImpl baseConnection; // TODO: unfuck this
        if (data.isUseSSH()) {
            SSHConnection sshConnection = data.getSSHConnection();
            baseConnection = new BaseConnectionImpl(data.getHost(), data.getPort(), new SSHConnectionImpl(sshConnection.getSshHost(), sshConnection.getSshUser(), sshConnection.getSshPassword(), sshConnection.getSshPort(), sshConnection.isUseKeyPair()));
        }
        else {
            baseConnection = new BaseConnectionImpl(data.getHost(), data.getPort());
        }

        ircProtocol = IrcProtocolFactory.getIrcProtocol(baseConnection);
        ircProtocol.setListener(this);
        ircProtocol.connect(data.getNickname(), data.getUsername(), data.getRealname(), data.getPassword());
        hilightManager = new HilightManager();
        hilightManager.addHilight(data.getNickname(), HilightLevel.NICKNAME);
    }

    @Override
    public IrcServer getBackingBean() {
        return ircServer;
    }

    @Override
    public IrkksomeConnection getConnectionData() {
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
        ircProtocol.disconnect("irkksome <3");
    }

    @Override
    public void addServerConnectionListener(ServerConnectionListener listener) {
        connectionListeners.add(listener);
    }

    @Override
    public void removeServerConnectionListener() {
        connectionListeners.remove(1);
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
            setActiveChannel(channels.get(channels.size() - 1));
        } else {
            setActiveChannel(null);
            listener.showServerInfo(this);
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
    public java.util.Set<String> getUsers() {
        return ircServer.getKnownUsers();
    }

    @Override
    public IrcChannel getActiveChannel() {
        return activeChannel;
    }

    @Override
    public void setActiveChannel(IrcChannel ircChannel) {
        if (activeChannel != ircChannel) {
            if (ircChannel != null) {
                listener.setActiveChannel(ircChannel);
            }
            activeChannel = ircChannel;
        }
    }

    @Override
    public void startQuery(String nick) {
        IrcChannel query = serverDAO.getChannel(ircServer, nick);
        channelDAO.addUser(query, nick, "");
        setActiveChannel(query);
    }

    @Override
    public void showServer() {
        listener.showServerInfo(this);
        setActiveChannel(null);
    }

    //    ---------------------------------------------------------

    @Override
    public void serverConnectionEstablished() {
        for (ServerConnectionListener serverConnectionListener : connectionListeners) {
            serverConnectionListener.connectionEstablished(this);
        }

        // this should perhaps only be done when we know we connect to irrsi or another backlog provider
        ircProtocol.sendBacklogRequest(ircServer.getLastMessageTime().getTime() / 1000);
    }

    @Override
    public void serverRegistered(String server, String nick) {
        ircServer.setServerName(server);
        ircServer.setSelf(nick);
        showServer();
        for (IrcChannel channel : getBackingBean().getConnectedChannels()) {
            joinChannel(channel.getName());
        }
    }

    @Override
    public void nickChanged(String oldNick, String newNick, Date time) {
        if (ircServer.getSelf().equalsIgnoreCase(oldNick)) {
            ircServer.setSelf(newNick);
            final IrcMessage message = messageDAO.create(IrcMessage.MessageTypeEnum.NICKCHANGE, ircServer.getSelf(), "You are now known as " + newNick, time);
            for (IrcChannel channel : ircServer.getConnectedChannels()) {
                channelDAO.addMessage(channel, message);
                if (channel == getActiveChannel()) {
                    listener.messageReceived(message);
                }
            }
        } else {
            final IrcMessage message = messageDAO.create(IrcMessage.MessageTypeEnum.NICKCHANGE, newNick, oldNick + " is now known as " + newNick, time);
            updateChannelsWithUser(oldNick, message, newNick);
            listener.updateUserList();
        }
        ircServer.setLastMessageTime(time);
    }

    private void updateChannelsWithUser(String user, IrcMessage message, String newNick) {
        boolean refreshUI = false;
        final List<IrcChannel> connectedChannels = ircServer.getConnectedChannels();
        for (IrcChannel channel : connectedChannels) {
            if (channelDAO.hasUser(channel, user)) {
                channelDAO.removeUser(channel, user);
                channelDAO.addUser(channel, newNick, "");
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
            channelDAO.addUser(channel, userName, flag);
            serverDAO.addUser(ircServer, userName);
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
        if (ircServer.getSelf().equalsIgnoreCase(nick)) {
            if (ircProtocol.shouldPassMessageEvents()) {
                setActiveChannel(channel);
            }
        }
        else {
            channelDAO.addUser(channel, nick, "");
            final IrcMessage message = messageDAO.create(IrcMessage.MessageTypeEnum.JOIN, nick, nick + " joined the channel.", time);
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
        if (!ircServer.getSelf().equalsIgnoreCase(nick)) {
            channelDAO.removeUser(channel, nick);
            IrcMessage message = messageDAO.create(IrcMessage.MessageTypeEnum.PART, nick, nick + " left the channel.", time);
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
        serverDAO.removeUser(ircServer, nick);

        final IrcMessage message = messageDAO.create(IrcMessage.MessageTypeEnum.QUIT, nick, nick + " quit. (" + quitMessage + ")", time);
        for (IrcChannel channel : ircServer.getConnectedChannels()) {
            if (channelDAO.hasUser(channel, nick)) {
                channelDAO.removeUser(channel, nick);
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
        IrcMessage ircMessage = messageDAO.create(IrcMessage.MessageTypeEnum.RECEIVED, user, message, time);
        IrcChannel ircChannel;

        HilightLevel level;
        if (ircServer.getSelf().equals(channel)) {
            ircChannel = serverDAO.getChannel(ircServer, user); // TODO: Add the user to the query channel
            level = HilightLevel.NICKNAME;
        } else {
            ircChannel = serverDAO.getChannel(ircServer, channel);
            level = hilightManager.getHilightLevel(message);
        }

        if (!ircChannel.equals(activeChannel)) {
            if (hilightListener.addUnread(ircServer, ircChannel, level)) {
                listener.updateHilights();
            }
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
        Log.e("irkksome", techMessage);
        for (ServerConnectionListener connectionListener : connectionListeners) {
            connectionListener.connectionDropped(this);
        }
    }

    @Override
    public void serverDisconnected() {
        for (ServerConnectionListener connectionListener : connectionListeners) {
            connectionListener.connectionDropped(this);
        }
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
