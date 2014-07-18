package se.alkohest.irkksome.model.api.local;

import se.alkohest.irkksome.model.entity.IrcServer;

public interface IrcServerDAOLocal {
    public IrcServer create(String host);
}
