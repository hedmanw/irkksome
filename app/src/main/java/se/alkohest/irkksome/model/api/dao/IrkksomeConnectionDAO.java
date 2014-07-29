package se.alkohest.irkksome.model.api.dao;

import android.database.Cursor;

import java.util.List;

import se.alkohest.irkksome.model.api.local.IrkksomeConnectionDAOLocal;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;
import se.alkohest.irkksome.orm.GenericDAO;

/**
 * Created by wilhelm 2014-07-29.
 */
public class IrkksomeConnectionDAO extends GenericDAO<IrkksomeConnectionEB, IrkksomeConnection> implements IrkksomeConnectionDAOLocal {
    @Override
    public IrkksomeConnection create() {
        return new IrkksomeConnectionEB();
    }

    @Override
    public List<IrkksomeConnection> getAll() {
        return getAll(IrkksomeConnectionEB.class);
    }

    @Override
    public IrkksomeConnection findById(long id) {
        return findById(IrkksomeConnectionEB.class, id);
    }

    @Override
    protected IrkksomeConnection initFromCursor(Cursor cursor, long pk) {
        IrkksomeConnection irkksomeConnection = create();
        irkksomeConnection.setHost(cursor.getString(1));
        irkksomeConnection.setPort(cursor.getInt(2));
        irkksomeConnection.setNickname(cursor.getString(3));
        irkksomeConnection.setUsername(cursor.getString(4));
        irkksomeConnection.setRealname(cursor.getString(5));
        irkksomeConnection.setPassword(cursor.getString(6));
        irkksomeConnection.setUseSSL(cursor.getInt(7) > 0);
        irkksomeConnection.setUseSSH(cursor.getInt(8) > 0);
        irkksomeConnection.setSshHost(cursor.getString(9));
        irkksomeConnection.setSshUser(cursor.getString(10));
        irkksomeConnection.setSshPass(cursor.getString(11));
        irkksomeConnection.setSshPort(cursor.getInt(12));
        return irkksomeConnection;
    }
}
