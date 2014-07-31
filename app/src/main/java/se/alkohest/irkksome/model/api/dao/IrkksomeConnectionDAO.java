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

    public String getPresentation(IrkksomeConnection connection) {
        if (connection.isUseSSH()) {
            if (connection.isIrssiProxyConnection()) {
                return connection.getSshUser() + "@" + connection.getSshHost() + (connection.getSshPort() != 22 ? ":" + connection.getSshPort() : "") +
                        " (" + connection.getUsername() + ":" + connection.getPort() + ")";
            } else {
                return connection.getSshUser() + "@" + connection.getSshHost() + (connection.getSshPort() != 22 ? ":" + connection.getSshPort() : "") +
                        " (" + connection.getNickname() + "[" + connection.getUsername() + "]@" + connection.getHost() + ":" + connection.getPort() + ")";
            }
        }
        else {
            return connection.getNickname() + "@" + connection.getHost() + ":" + connection.getPort();
        }
    }

    @Override
    public void remove(IrkksomeConnection bean) {
        makeTransient(bean);
    }

    @Override
    protected IrkksomeConnection initFromCursor(Cursor cursor, long pk) {
        IrkksomeConnection irkksomeConnection = create();
        irkksomeConnection.setHost(cursor.getString(cursor.getColumnIndexOrThrow("host")));
        irkksomeConnection.setPort(cursor.getInt(cursor.getColumnIndexOrThrow("port")));
        irkksomeConnection.setNickname(cursor.getString(cursor.getColumnIndexOrThrow("nickname")));
        irkksomeConnection.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        irkksomeConnection.setRealname(cursor.getString(cursor.getColumnIndexOrThrow("realname")));
        irkksomeConnection.setUseSSL(cursor.getInt(cursor.getColumnIndexOrThrow("useSSL")) > 0);
        irkksomeConnection.setUseSSH(cursor.getInt(cursor.getColumnIndexOrThrow("useSSH")) > 0);
        irkksomeConnection.setSshHost(cursor.getString(cursor.getColumnIndexOrThrow("sshHost")));
        irkksomeConnection.setSshUser(cursor.getString(cursor.getColumnIndexOrThrow("sshUser")));
        irkksomeConnection.setSshPort(cursor.getInt(cursor.getColumnIndexOrThrow("sshPort")));
        return irkksomeConnection;
    }
}
