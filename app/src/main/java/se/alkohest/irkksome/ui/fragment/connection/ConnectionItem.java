package se.alkohest.irkksome.ui.fragment.connection;

import android.view.LayoutInflater;
import android.view.View;

import se.alkohest.irkksome.ui.AbstractListItem;

public abstract class ConnectionItem extends AbstractListItem {
    public enum ConnectionTypeEnum {
        NEW_CONNECTION, OLD_CONNECTION
    }

    private String representation;
    private ConnectionTypeEnum viewType;

    public ConnectionItem(ConnectionTypeEnum viewType, String representation) {
        this.representation = representation;
        this.viewType = viewType;
    }

    public abstract View getView(LayoutInflater inflater, View convertView);
    public abstract AbstractConnectionFragment getConnectionFragment();

    @Override
    public String toString() {
        return representation;
    }

    public ConnectionTypeEnum getViewType() {
        return viewType;
    }
}
