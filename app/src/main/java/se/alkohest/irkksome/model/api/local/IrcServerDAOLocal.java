package se.alkohest.irkksome.model.api.local;

import java.util.List;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;

public interface IrcServerDAOLocal {
    public IrcServer create(String host);

}
