package se.alkohest.irkksome.ui.connection;

import se.alkohest.irkksome.model.entity.IrkksomeConnection;

public interface ConnectionPresenter {
    public void setTemplateConnection(long connectionPK);
    public IrkksomeConnection getTemplateConnection();
    public void connect(IrkksomeConnection connection);
    public IrkksomeConnection getNewConnection();
}
