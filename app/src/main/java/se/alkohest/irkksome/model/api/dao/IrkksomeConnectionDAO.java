package se.alkohest.irkksome.model.api.dao;

import android.database.Cursor;

import java.util.List;

import se.alkohest.irkksome.model.api.local.IrkksomeConnectionDAOLocal;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;

/**
 * Created by wilhelm 2014-07-29.
 */
public class IrkksomeConnectionDAO implements IrkksomeConnectionDAOLocal {
    @Override
    public IrkksomeConnection create() {
        return new IrkksomeConnectionEB();
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
}
