package se.alkohest.irkksome.ui.fragment.connection;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.dao.IrkksomeConnectionDAO;
import se.alkohest.irkksome.model.api.local.IrkksomeConnectionDAOLocal;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;

public class ConnectionsListFragment extends Fragment implements ConnectionsRecyclerAdapter.LegacyConnectionListener {
    public static final String TAG = "CONNECTION_LIST";
    private final IrkksomeConnectionDAOLocal connectionDAO = new IrkksomeConnectionDAO();
    private OnConnectionSelectedListener listener;
    private ConnectionsRecyclerAdapter adapter;
    private List<IrkksomeConnectionEB> connectionsForDisplay;

    public static ConnectionsListFragment newInstance() {
        ConnectionsListFragment fragment = new ConnectionsListFragment();
        return fragment;
    }

    public ConnectionsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionsForDisplay = connectionDAO.getConnectionsForDisplay();
        adapter = new ConnectionsRecyclerAdapter(this, connectionsForDisplay);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_new_connection_list, container, false);
        RecyclerView recyclerView = (RecyclerView) inflatedView.findViewById(R.id.legacy_connection_listView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        View regularConnectionButton = inflatedView.findViewById(R.id.new_connection_regular);
        regularConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCallback(RegularConnectionFragment.newInstance());
            }
        });
        View irssiConnectionButton = inflatedView.findViewById(R.id.new_connection_irssi);
        irssiConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCallback(IrssiProxyConnectionFragment.newInstance());
            }
        });
        return inflatedView;
    }

    public void sendCallback(AbstractConnectionFragment abstractConnectionFragment) {
        if (listener != null) {
            listener.onConnectionSelected(abstractConnectionFragment);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnConnectionSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnConnectionSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void legacyConnectionClicked(IrkksomeConnectionEB connectionItem) {
        if (connectionItem.isIrssiProxyConnection()) {
            sendCallback(IrssiProxyConnectionFragment.newInstance(connectionItem));
        }
        else {
            sendCallback(RegularConnectionFragment.newInstance(connectionItem));
        }
    }

    @Override
    public void legacyConnectionRemoved(IrkksomeConnectionEB connectionItem) {
        connectionDAO.delete(connectionItem);
        connectionsForDisplay.remove(connectionItem);
        adapter.notifyDataSetChanged();
    }

    public interface OnConnectionSelectedListener {
        public void onConnectionSelected(AbstractConnectionFragment connectionFragment);
    }

}
