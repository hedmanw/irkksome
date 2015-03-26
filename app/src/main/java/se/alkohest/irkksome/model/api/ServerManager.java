package se.alkohest.irkksome.model.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.alkohest.irkksome.model.api.dao.IrcServerDAO;
import se.alkohest.irkksome.model.api.dao.IrkksomeConnectionDAO;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.model.enumerations.HilightLevel;
import se.alkohest.irkksome.model.impl.IrcServerEB;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;

public class ServerManager implements ServerConnectionListener, HilightListener {
    public static final ServerManager INSTANCE = new ServerManager();
    private IrcServerDAO serverDAO;
    private IrkksomeConnectionDAO connectionDAO;
    private List<Server> servers;
    private Server activeServer;
    private UnreadStack unreadStack;

    protected ServerManager() {
        serverDAO = new IrcServerDAO();
        connectionDAO = new IrkksomeConnectionDAO();
        servers = new ArrayList<>();
        unreadStack = new UnreadStack();
    }

    public void loadPersisted() {
        List<IrcServerEB> persisted = serverDAO.getAll();
        for (IrcServer ircServer : persisted) {
            /* maybe later...
            Server server = new ServerImpl(ircServer, ircServer.getSelf().getName());
            server.addServerConnectionListener(this);
            servers.add(server);
            */
        }
        if (!persisted.isEmpty()) {
            setActiveServer(servers.get(0));
        }
    }

    public Server establishConnection(IrkksomeConnection irkksomeConnection) {
        final IrcServer ircServer = serverDAO.create();
        Server server = new ServerImpl(ircServer, irkksomeConnection);
        server.addServerConnectionListener(this);
        server.setHilightListener(this);

        return server;
    }

    public Server getActiveServer() {
        return activeServer;
    }

    public void setActiveServer(Server activeServer) {
        this.activeServer = activeServer;
    }

    public void setActiveServer(IrcServer server) {
        for (Server s : servers) {
            if (s.getBackingBean() == server) {
                this.activeServer = s;
                break;
            }
        }
    }

    public List<Server> getServers() {
        return servers;
    }

    public void shutDownServer(Server server) {
        server.disconnect();
        this.activeServer = null; // TODO: Change this when we allow multiple conns
    }

    public UnreadStack getUnreadStack() {
        return unreadStack;
    }

    @Override
    public boolean addUnread(IrcServer ircServer, IrcChannel ircChannel, HilightLevel hilightLevel) {
        return unreadStack.pushOverwrite(ircServer, ircChannel, hilightLevel);
    }

    @Override
    public void connectionEstablished(Server server) {
        servers.add(server);
        setActiveServer(server);
        server.getConnectionData().setLastUsed(new Date());
        connectionDAO.persist((IrkksomeConnectionEB) server.getConnectionData());
    }

    @Override
    public void connectionDropped(Server server) {
        servers.remove(server);
    }

    public void clearActiveChannel() {
        if (activeServer != null) {
            activeServer.setActiveChannel(null);
        }
    }
}
