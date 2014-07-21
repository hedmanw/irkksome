package se.alkohest.irkksome.model.api;

import java.util.ArrayList;
import java.util.List;

import se.alkohest.irkksome.model.api.dao.IrcServerDAO;
import se.alkohest.irkksome.model.api.local.IrcServerDAOLocal;
import se.alkohest.irkksome.model.entity.IrcServer;

public class ServerManager implements ServerDropAcidListener {
    private IrcServerDAOLocal serverDAO;
    private List<Server> servers;
    private Server activeServer;

    public ServerManager() {
        serverDAO = new IrcServerDAO();
        servers = new ArrayList<>();
    }

    public Server addServer(String host, String nickname) {
        final IrcServer ircServer = serverDAO.create(host);
        Server server = new ServerImpl(ircServer, nickname);
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

    public List<Server> getServers() {
        return servers;
    }

    public void shutDownServer(Server server) {
        server.disconnect();
    }

    @Override
    public void dropServer(Server server) {
        servers.remove(server);
    }
}
