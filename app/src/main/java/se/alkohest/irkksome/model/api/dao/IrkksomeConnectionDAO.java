package se.alkohest.irkksome.model.api.dao;

import android.database.Cursor;

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
    public IrkksomeConnection findById(long id) {
        return null;
    }

    @Override
    protected IrkksomeConnection initFromCursor(Cursor cursor, long pk) {
        return null;
    }
}
