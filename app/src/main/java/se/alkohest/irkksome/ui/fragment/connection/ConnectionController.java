package se.alkohest.irkksome.ui.fragment.connection;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.dao.IrkksomeConnectionDAO;
import se.alkohest.irkksome.model.api.local.IrkksomeConnectionDAOLocal;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;
import se.alkohest.irkksome.util.DateFormatUtil;

public class ConnectionController {
    public static List<ConnectionItem> CONNECTIONS = new ArrayList<>();
    public static List<ConnectionItem> PERMANENT_CONNECTIONS = new ArrayList<>();
    private static final IrkksomeConnectionDAOLocal connectionDAO = new IrkksomeConnectionDAO();
    public static LegacyConnectionListener listener;

    static {
        addPermanentConnection(new ConnectionMethod(R.string.connection_type_regular, R.drawable.connection_icon_blue));
        addPermanentConnection(new ConnectionMethod(R.string.connection_type_irssi, R.drawable.connection_icon_purple));
    }

    private static void addConnectionItem(ConnectionItem item) {
        CONNECTIONS.add(item);
    }

    private static void addPermanentConnection(ConnectionItem item) {
        PERMANENT_CONNECTIONS.add(item);
        addConnectionItem(item);
    }

    private static void addLegacyConnections() {
        List<IrkksomeConnectionEB> connections = connectionDAO.getConnectionsForDisplay();
        for (IrkksomeConnectionEB connection : connections) {
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
        @StringRes
        private int connectionNameRes;
        @DrawableRes
        private int iconRes;

        public ConnectionMethod(@StringRes int representation, @DrawableRes int color) {
            super(ConnectionTypeEnum.NEW_CONNECTION);
            this.connectionNameRes = representation;
            this.iconRes = color;
        }

        @Override
        public View getView(LayoutInflater inflater, View convertView) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.server_connect_list_new_item, null);
            }
            Drawable icon = inflater.getContext().getResources().getDrawable(this.iconRes);
            View iconContainer = convertView.findViewById(R.id.server_connect_icon);
            iconContainer.setBackground(icon);
            TextView description = (TextView) convertView.findViewById(android.R.id.text1);
            description.setText(connectionNameRes);
            return convertView;
        }

        @Override
        public AbstractConnectionFragment getConnectionFragment() {
            return AbstractConnectionFragment.newInstance(iconRes);
        }
    }

    public static class LegacyConnection extends ConnectionItem {
        IrkksomeConnectionEB connection;

        public LegacyConnection(IrkksomeConnectionEB connection) {
            super(ConnectionTypeEnum.OLD_CONNECTION);
            this.connection = connection;
        }

        @Override
        public View getView(final LayoutInflater inflater, View convertView) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.server_connect_list_item, null);
            }

            TextView nick = (TextView) convertView.findViewById(android.R.id.title);
            nick.setText(connection.getNickname());
            TextView host = (TextView) convertView.findViewById(android.R.id.text1);
            if (connection.isIrssiProxyConnection()) {
                host.setText(connection.getSshUser() + "@" + connection.getSshHost());
            } else {
                host.setText(connection.getHost());
            }
            TextView lastAccessed = (TextView) convertView.findViewById(android.R.id.text2);
            lastAccessed.setText("Last synced " + DateFormatUtil.getTimeDay(connection.getLastUsed()));

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
                    connectionDAO.delete(connection);
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
