package se.alkohest.irkksome.model.api.local;

import java.util.List;

import se.alkohest.irkksome.model.entity.IrkksomeConnection;

/**
 * Created by wilhelm 2014-07-29.
 */
public interface IrkksomeConnectionDAOLocal {
    public IrkksomeConnection create();
    public List<IrkksomeConnection> getAll();
    public String getPresentation(IrkksomeConnection connection);
    public void remove(IrkksomeConnection bean);
    IrkksomeConnection findById(long id);
}
