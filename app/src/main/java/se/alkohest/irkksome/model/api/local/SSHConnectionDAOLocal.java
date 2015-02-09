package se.alkohest.irkksome.model.api.local;

import se.alkohest.irkksome.model.impl.SSHConnectionEB;

/**
 * Created by wilhelm 2015-02-05.
 */
public interface SSHConnectionDAOLocal {
    public SSHConnectionEB findById(long id);
}
