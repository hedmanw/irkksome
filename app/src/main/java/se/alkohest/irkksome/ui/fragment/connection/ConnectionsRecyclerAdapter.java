package se.alkohest.irkksome.ui.fragment.connection;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;
import se.alkohest.irkksome.util.DateFormatUtil;

public class ConnectionsRecyclerAdapter extends RecyclerView.Adapter<ConnectionsRecyclerAdapter.LegacyConnectionViewHolder> {
    private List<IrkksomeConnectionEB> dataset;
    private LegacyConnectionListener listener;

    public ConnectionsRecyclerAdapter(LegacyConnectionListener listener, List<IrkksomeConnectionEB> connectionsList) {
        this.listener = listener;
        this.dataset = connectionsList;
    }

    @Override
    public LegacyConnectionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.server_connect_list_item, viewGroup, false);
        return new LegacyConnectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LegacyConnectionViewHolder legacyConnectionViewHolder, int index) {
        final IrkksomeConnectionEB connection = dataset.get(index);
        legacyConnectionViewHolder.populate(connection);
        legacyConnectionViewHolder.mainClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.legacyConnectionClicked(connection);
            }
        });

        legacyConnectionViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.legacyConnectionRemoved(connection);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    protected static class LegacyConnectionViewHolder extends RecyclerView.ViewHolder {
        private TextView nickname;
        private TextView hostname;
        private TextView lastAccessed;
        private View mainClick;
        private View deleteButton;

        public LegacyConnectionViewHolder(View view) {
            super(view);
            nickname = (TextView) view.findViewById(android.R.id.title);
            hostname = (TextView) view.findViewById(android.R.id.text1);
            lastAccessed = (TextView) view.findViewById(android.R.id.text2);
            mainClick = view.findViewById(R.id.server_connect_legacy);
            deleteButton = view.findViewById(R.id.server_connect_legacy_remove);
        }

        public void populate(final IrkksomeConnectionEB connection) {
            nickname.setText(connection.getNickname());
            if (connection.isIrssiProxyConnection()) {
                hostname.setText(connection.getSshUser() + "@" + connection.getSshHost());
            } else {
                hostname.setText(connection.getHost());
            }
            lastAccessed.setText("Last synced " + DateFormatUtil.getTimeDay(connection.getLastUsed()));
        }
    }

    public interface LegacyConnectionListener {
        public void legacyConnectionClicked(IrkksomeConnectionEB connection);
        public void legacyConnectionRemoved(IrkksomeConnectionEB connection);
    }
}
