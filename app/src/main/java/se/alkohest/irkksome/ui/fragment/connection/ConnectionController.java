package se.alkohest.irkksome.ui.fragment.connection;

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
    private final IrkksomeConnectionDAOLocal connectionDAO = new IrkksomeConnectionDAO();
    public LegacyConnectionListener listener;
    private List<ConnectionItem> dataset;

    public ConnectionController(LegacyConnectionListener listener) {
        dataset = new ArrayList<>();
        loadConnectionBeans();
        this.listener = listener;
    }

    public List<ConnectionItem> getDataset() {
        return dataset;
    }

    private void loadConnectionBeans() {
        final List<IrkksomeConnectionEB> connectionsForDisplay = connectionDAO.getConnectionsForDisplay();
        for (IrkksomeConnectionEB connection : connectionsForDisplay) {
            dataset.add(new LegacyConnection(connection));
        }
    }

    public class LegacyConnection extends ConnectionItem {
        IrkksomeConnectionEB connection;

        public LegacyConnection(IrkksomeConnectionEB connection) {
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
                    dataset.remove(LegacyConnection.this);
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
