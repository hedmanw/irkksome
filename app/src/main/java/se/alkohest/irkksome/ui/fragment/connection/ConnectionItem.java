package se.alkohest.irkksome.ui.fragment.connection;

import android.view.LayoutInflater;
import android.view.View;

import se.alkohest.irkksome.ui.AbstractListItem;

public abstract class ConnectionItem extends AbstractListItem {
    public abstract View getView(LayoutInflater inflater, View convertView);
    public abstract AbstractConnectionFragment getConnectionFragment();
}
