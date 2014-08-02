package se.alkohest.irkksome.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import se.alkohest.irkksome.R;

public class ConnectionsListFragment extends ListFragment implements ConnectionController.LegacyConnectionListener {
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
        adapter = new ConnectionsArrayAdapter(getActivity());
        ConnectionController.listener = this;
        setListAdapter(adapter);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.title_connect_to_server);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_join_channel).setEnabled(false);
        menu.findItem(R.id.action_leave_channel).setEnabled(false);
        menu.findItem(R.id.action_change_nick).setEnabled(false);
        super.onCreateOptionsMenu(menu, inflater);
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
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        if (listener != null) {
            final ConnectionItem connectionItem = ConnectionController.CONNECTIONS.get(position);
            listener.onConnectionSelected(connectionItem);
        }
    }

    @Override
    public void legacyConnectionClicked(ConnectionItem connectionItem) {
        if (listener != null) {
            listener.onConnectionSelected(connectionItem);
        }
    }

    @Override
    public void legacyConnectionRemoved() {
        adapter.notifyDataSetChanged();
    }

    public interface OnConnectionSelectedListener {
        public void onConnectionSelected(ConnectionItem connectionItem);
    }

}
