package se.alkohest.irkksome.model.api.dao;

import se.alkohest.irkksome.model.impl.SSHConnectionEB;
import se.alkohest.irkksome.orm.GenericDAO;

/**
 * Created by wilhelm 2015-02-05.
 */
public class SSHConnectionDAO extends GenericDAO<SSHConnectionEB> {
    @Override
    protected Class<SSHConnectionEB> getEntityBean() {
        return SSHConnectionEB.class;
    }
}
