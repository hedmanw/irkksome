package se.alkohest.irkksome.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import se.alkohest.irkksome.R;

public class ConnectionsListFragment extends ListFragment {
    public static final String TAG = "CONNECTION_LIST";
    private OnConnectionSelectedListener listener;

    public static ConnectionsListFragment newInstance() {
        ConnectionsListFragment fragment = new ConnectionsListFragment();
        return fragment;
    }

    public ConnectionsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ConnectionsArrayAdapter(getActivity()));
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

        if (null != listener) {
            final ConnectionItem connectionItem = ConnectionController.CONNECTIONS.get(position);
            listener.onConnectionSelected(connectionItem);
        }
    }

    public interface OnConnectionSelectedListener {
        public void onConnectionSelected(ConnectionItem connectionItem);
    }

}
