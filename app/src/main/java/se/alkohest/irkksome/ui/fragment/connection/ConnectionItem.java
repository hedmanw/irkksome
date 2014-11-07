package se.alkohest.irkksome.ui.fragment.connection;

import android.view.LayoutInflater;
import android.view.View;

import se.alkohest.irkksome.ui.AbstractListItem;

public abstract class ConnectionItem extends AbstractListItem {
    public enum ConnectionTypeEnum {
        NEW_CONNECTION, OLD_CONNECTION
    }

    private ConnectionTypeEnum viewType;

    public ConnectionItem(ConnectionTypeEnum viewType) {
        this.viewType = viewType;
    }

    public abstract View getView(LayoutInflater inflater, View convertView);
    public abstract AbstractConnectionFragment getConnectionFragment();

    public ConnectionTypeEnum getViewType() {
        return viewType;
    }
}
