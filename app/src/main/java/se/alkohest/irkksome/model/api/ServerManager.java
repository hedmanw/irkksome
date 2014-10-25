package se.alkohest.irkksome.model.api;

import java.util.ArrayList;
import java.util.List;

import se.alkohest.irkksome.model.api.dao.IrcServerDAO;
import se.alkohest.irkksome.model.api.dao.IrkksomeConnectionDAO;
import se.alkohest.irkksome.model.api.local.IrcServerDAOLocal;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.impl.IrcServerEB;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;

public class ServerManager implements ServerDropAcidListener {
    public static final ServerManager INSTANCE = new ServerManager();
    private IrcServerDAOLocal serverDAO;
    private List<Server> servers;
    private Server activeServer;
    private UnreadStack unreadStack;

    protected ServerManager() {
        serverDAO = new IrcServerDAO();
        servers = new ArrayList<>();
        unreadStack = new UnreadStack();
    }

    public void loadPersisted() {
        List<IrcServerEB> persisted = serverDAO.getAll();
        for (IrcServer ircServer : persisted) {
            /* maybe later...
            Server server = new ServerImpl(ircServer, ircServer.getSelf().getName());
            server.setDropListener(this);
            servers.add(server);
            */
        }
        if (!persisted.isEmpty()) {
            setActiveServer(servers.get(0));
        }
    }

    public Server addServer(IrkksomeConnectionEB irkksomeConnection) {
        new IrkksomeConnectionDAO().persist(irkksomeConnection);
        final IrcServer ircServer = serverDAO.create(irkksomeConnection.getHost());
        Server server = new ServerImpl(ircServer, irkksomeConnection);
        server.setDropListener(this);
        servers.add(server);
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
    }

    public UnreadStack getUnreadStack() {
        return unreadStack;
    }

    @Override
    public void addUnread(UnreadEntity entity, boolean isHilight) {
        unreadStack.push(entity, isHilight);
    }

    @Override
    public void dropServer(Server server) {
        servers.remove(server);
    }
}
