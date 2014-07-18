package se.alkohest.irkksome.model.api.local;

import se.alkohest.irkksome.model.entity.IrcUser;

public interface IrcUserDAOLocal {
    public IrcUser create(String name);
}
