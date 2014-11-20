package se.alkohest.irkksome.ui.fragment.connection;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import se.alkohest.irkksome.R;

public class ConnectionsListFragment extends Fragment implements ConnectionController.LegacyConnectionListener {
    public static final String TAG = "CONNECTION_LIST";
    private OnConnectionSelectedListener listener;
    private ConnectionsArrayAdapter adapter;

    public static ConnectionsListFragment newInstance() {
        ConnectionsListFragment fragment = new ConnectionsListFragment();
        return fragment;
    }

    public ConnectionsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectionController connectionController = new ConnectionController(this);
        adapter = new ConnectionsArrayAdapter(getActivity(), connectionController);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_new_connection_list, container, false);
        ListView listView = (ListView) inflatedView.findViewById(R.id.legacy_connection_listView);
        listView.setAdapter(adapter);
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
    public void legacyConnectionClicked(ConnectionItem connectionItem) {
        sendCallback(connectionItem.getConnectionFragment());
    }

    @Override
    public void legacyConnectionRemoved() {
        adapter.notifyDataSetChanged();
    }

    public interface OnConnectionSelectedListener {
        public void onConnectionSelected(AbstractConnectionFragment connectionFragment);
    }

}
