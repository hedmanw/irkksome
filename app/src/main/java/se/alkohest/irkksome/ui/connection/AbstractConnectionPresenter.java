package se.alkohest.irkksome.ui.connection;

import java.util.Date;

import se.alkohest.irkksome.model.api.Server;
import se.alkohest.irkksome.model.api.ServerConnectionListener;
import se.alkohest.irkksome.model.api.ServerManager;
import se.alkohest.irkksome.model.api.dao.IrkksomeConnectionDAO;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.ui.CallbackHandler;

public class AbstractConnectionPresenter implements ConnectionPresenter, ServerConnectionListener {
    protected IrkksomeConnection templateConnection; // Switch to DTO
    protected IrkksomeConnectionDAO connectionDAO = new IrkksomeConnectionDAO();

    protected AbstractConnectionView view;

    public AbstractConnectionPresenter(AbstractConnectionView view) {
        this.view = view;
    }

    @Override
    public void setTemplateConnection(long connectionPK) {
        templateConnection = connectionDAO.findById(connectionPK);
    }

    @Override
    public IrkksomeConnection getTemplateConnection() {
        return templateConnection;
    }

    @Override
    public void connect(IrkksomeConnection connection) { // Switch to DTO
        connection.setLastUsed(new Date());
        ServerManager serverManager = ServerManager.INSTANCE;
        final Server pendingServer = serverManager.establishConnection(connection);
        pendingServer.addServerConnectionListener(this);
        pendingServer.setListener(CallbackHandler.getInstance());
        view.showProgress();
    }

    @Override
    public IrkksomeConnection getNewConnection() { // Switch to DTO
        return connectionDAO.create();
    }

    @Override
    public void connectionEstablished(Server server) {
        server.removeServerConnectionListener();
        view.connectionSuccess();
    }

    @Override
    public void connectionDropped(Server server) {
        view.connectionFailure();
    }
}
