package se.alkohest.irkksome.model.api;

import java.util.ArrayList;
import java.util.List;

import se.alkohest.irkksome.model.api.dao.IrcServerDAO;
import se.alkohest.irkksome.model.api.local.IrcServerDAOLocal;
import se.alkohest.irkksome.model.entity.IrcServer;

public class ServerManager {
    private IrcServerDAOLocal serverDAO;
    private List<Server> servers;

    public ServerManager() {
        serverDAO = new IrcServerDAO();
        servers = new ArrayList<>();
    }

    public Server addServer(String host) {
        final IrcServer ircServer = serverDAO.create(host);
        Server server = new ServerImpl(ircServer);
        return server;
    }

    public void shutDownServer(/* Argument? */) {
        // Mappa IrcServer -> Server?
    }

}
