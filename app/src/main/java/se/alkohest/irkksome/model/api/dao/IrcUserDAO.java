package se.alkohest.irkksome.model.api.dao;

import android.content.ContentValues;

import se.alkohest.irkksome.model.api.local.IrcUserDAOLocal;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcUserEB;
import se.alkohest.irkksome.orm.GenericDAO;

public class IrcUserDAO extends GenericDAO<IrcUserEB, IrcUser> implements IrcUserDAOLocal {

    public IrcUser create(String name) {
        IrcUser ircUser = new IrcUserEB();
        ircUser.setName(name);
        return ircUser;
    }

    @Override
    protected ContentValues createContentValues(IrcUser bean) {
        return null;
    }
}
