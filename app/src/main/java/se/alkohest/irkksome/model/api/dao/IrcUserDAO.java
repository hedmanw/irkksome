package se.alkohest.irkksome.model.api.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import se.alkohest.irkksome.model.api.local.IrcUserDAOLocal;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.model.impl.IrcUserEB;
import se.alkohest.irkksome.orm.GenericDAO;

public class IrcUserDAO extends GenericDAO<IrcUserEB, IrcUser> implements IrcUserDAOLocal {
    @Override
    public IrcUser create(String name) {
        IrcUser ircUser = new IrcUserEB();
        ircUser.setName(name);
        return ircUser;
    }

    @Override
    public void findById(long id) {

    }

    @Override
    protected IrcUser initFromCursor(Cursor cursor) {
        IrcUser ircUser = create(cursor.getString(1));
        return ircUser;
    }

    public List<IrcUser> getAllPersisted() {
        return getAll(IrcUserEB.class);
    }

    @Override
    public boolean compare(IrcUser user, String nick) {
        return user.getName().toLowerCase().equals(nick.toLowerCase());
    }

    @Override
    protected ContentValues createContentValues(IrcUser bean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", bean.getName());
        return contentValues;
    }
}
