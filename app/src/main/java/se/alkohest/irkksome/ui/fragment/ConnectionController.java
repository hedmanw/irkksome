package se.alkohest.irkksome.ui.fragment;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.dao.IrkksomeConnectionDAO;
import se.alkohest.irkksome.model.api.local.IrkksomeConnectionDAOLocal;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;

public class ConnectionController {
    public static List<ConnectionItem> CONNECTIONS = new ArrayList<>();
    public static List<ConnectionItem> PERMANENT_CONNECTIONS = new ArrayList<>();
    private static final IrkksomeConnectionDAOLocal connectionDAO = new IrkksomeConnectionDAO();
    public static LegacyConnectionListener listener;

    static {
        addPermanentConnection(new ConnectionMethod("Regular irkk", R.drawable.connection_icon_blue));
        addPermanentConnection(new ConnectionMethod("Irssi proxy", R.drawable.connection_icon_purple));
    }

    private static void addConnectionItem(ConnectionItem item) {
        CONNECTIONS.add(item);
    }

    private static void addPermanentConnection(ConnectionItem item) {
        PERMANENT_CONNECTIONS.add(item);
        addConnectionItem(item);
    }

    private static void addLegacyConnections() {
        List<IrkksomeConnection> connections = connectionDAO.getAll();
        for (IrkksomeConnection connection : connections) {
            addConnectionItem(new LegacyConnection(connection));
        }
    }

    private static void datasetChanged() {
        CONNECTIONS.retainAll(PERMANENT_CONNECTIONS);
        addLegacyConnections();
    }

    public static List<ConnectionItem> getDataset() {
        datasetChanged();
        return CONNECTIONS;
    }

    public static class ConnectionMethod extends ConnectionItem {
        private int icon;

        public ConnectionMethod(String representation, int color) {
            super(ConnectionTypeEnum.NEW_CONNECTION, representation);
            this.icon = color;
        }

        @Override
        public View getView(LayoutInflater inflater, View convertView) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.server_connect_list_new_item, null);
            }
            Drawable icon = inflater.getContext().getResources().getDrawable(this.icon);
            View view = convertView.findViewById(R.id.server_connect_icon);
            view.setBackground(icon);
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(toString());
            return convertView;
        }

        @Override
        public AbstractConnectionFragment getConnectionFragment() {
            return AbstractConnectionFragment.newInstance(icon);
        }
    }

    public static class LegacyConnection extends ConnectionItem {
        IrkksomeConnection connection;

        public LegacyConnection(IrkksomeConnection connection) {
            super(ConnectionTypeEnum.OLD_CONNECTION, connectionDAO.getPresentation(connection));
            this.connection = connection;
        }

        @Override
        public View getView(final LayoutInflater inflater, View convertView) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.server_connect_list_item, null);
            }

            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(toString());

            View mainClick = convertView.findViewById(R.id.server_connect_legacy);
            mainClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.legacyConnectionClicked(LegacyConnection.this);
                }
            });

            View deleteButton = convertView.findViewById(R.id.server_connect_legacy_remove);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    connectionDAO.remove(connection);
                    datasetChanged();
                    listener.legacyConnectionRemoved();
                }
            });
            return convertView;
        }

        @Override
        public AbstractConnectionFragment getConnectionFragment() {
            if (connection.isIrssiProxyConnection()) {
                return IrssiProxyConnectionFragment.newInstance(connection);
            }
            else {
                return RegularConnectionFragment.newInstance(connection);
            }
        }
    }

    public interface LegacyConnectionListener {
        public void legacyConnectionClicked(ConnectionItem connectionItem);
        public void legacyConnectionRemoved();
    }
}
