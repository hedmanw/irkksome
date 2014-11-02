package se.alkohest.irkksome.model.api.local;

import java.util.List;

import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;
import se.emilsjolander.sprinkles.ManyQuery;

/**
 * Created by wilhelm 2014-07-29.
 */
public interface IrkksomeConnectionDAOLocal {
    public IrkksomeConnectionEB create();
    public String getPresentation(IrkksomeConnectionEB connection);
    public IrkksomeConnectionEB findById(long id);
    public void delete(IrkksomeConnectionEB connection);
    public List<IrkksomeConnectionEB> getAll();
    public List<IrkksomeConnectionEB> getConnectionsForDisplay();
}
