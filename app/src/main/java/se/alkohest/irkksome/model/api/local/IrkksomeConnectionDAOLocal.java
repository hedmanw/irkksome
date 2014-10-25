package se.alkohest.irkksome.model.api.local;

import se.alkohest.irkksome.model.entity.IrkksomeConnection;

/**
 * Created by wilhelm 2014-07-29.
 */
public interface IrkksomeConnectionDAOLocal {
    public IrkksomeConnection create();
    public String getPresentation(IrkksomeConnection connection);
}
