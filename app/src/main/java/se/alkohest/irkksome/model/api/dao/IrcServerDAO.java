package se.alkohest.irkksome.model.api.dao;

import android.content.ContentValues;

import java.util.ArrayList;

import se.alkohest.irkksome.model.api.local.IrcServerDAOLocal;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.impl.IrcServerEB;
import se.alkohest.irkksome.orm.GenericDAO;

public class IrcServerDAO extends GenericDAO<IrcServerEB, IrcServer> implements IrcServerDAOLocal {
    @Override
    public IrcServer create(String host) {
        IrcServer ircServer = new IrcServerEB();
        ircServer.setConnectedChannels(new ArrayList<IrcChannel>());
        ircServer.setUrl(host);
        return ircServer;
    }

    @Override
    protected ContentValues createContentValues(IrcServer beanEntity) {
        return null;
    }
}
