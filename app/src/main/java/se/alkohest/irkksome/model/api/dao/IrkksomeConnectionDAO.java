package se.alkohest.irkksome.model.api.dao;

import java.util.List;

import se.alkohest.irkksome.model.api.local.IrkksomeConnectionDAOLocal;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;
import se.alkohest.irkksome.orm.GenericDAO;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Query;

/**
 * Created by wilhelm 2014-07-29.
 */
public class IrkksomeConnectionDAO extends GenericDAO<IrkksomeConnectionEB> implements IrkksomeConnectionDAOLocal {
    @Override
    public IrkksomeConnectionEB create() {
        return new IrkksomeConnectionEB();
    }

    @Override
    public IrkksomeConnectionEB findById(long id) {
        return getById(id);
    }

    @Override
    protected Class<IrkksomeConnectionEB> getEntityBean() {
        return IrkksomeConnectionEB.class;
    }

    @Override
    public List<IrkksomeConnectionEB> getAll() {
        return super.getAll();
    }

    @Override
    public List<IrkksomeConnectionEB> getConnectionsForDisplay() {
        CursorList<IrkksomeConnectionEB> cursorList = Query.many(getEntityBean(), "SELECT * FROM Connection ORDER BY lastUsed DESC").get();
        List<IrkksomeConnectionEB> list = cursorList.asList();
        cursorList.close();
        return list;
    }
}
