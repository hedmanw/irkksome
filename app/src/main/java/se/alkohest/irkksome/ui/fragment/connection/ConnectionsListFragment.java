package se.alkohest.irkksome.ui.fragment.connection;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.dao.IrkksomeConnectionDAO;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;

public class ConnectionsListFragment extends Fragment {
    public static final String TAG = "CONNECTION_LIST";
    private final IrkksomeConnectionDAO connectionDAO = new IrkksomeConnectionDAO();
    private OnConnectionSelectedListener listener;
    private LegacyConnectionsAdapter adapter;
    private ListView listView;
    private boolean canDeleteConnections; // should be static?

    public static ConnectionsListFragment newInstance(boolean canDeleteConnections) {
        ConnectionsListFragment fragment = new ConnectionsListFragment();
        fragment.canDeleteConnections = canDeleteConnections;
        return fragment;
    }

    public ConnectionsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new LegacyConnectionsAdapter(getActivity(), connectionDAO.getConnectionsForDisplay());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_new_connection_list, container, false);
        listView = (ListView) inflatedView.findViewById(R.id.legacy_connection_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                legacyConnectionClicked(adapter.getItem(position));
            }
        });

        if (canDeleteConnections) {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int pos, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    MenuInflater menuInflater = actionMode.getMenuInflater();
                    menuInflater.inflate(R.menu.context_connections, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.action_delete_connection:
                            deleteSelectedItems();
                            actionMode.finish();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }
            });
        }

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

    public void legacyConnectionClicked(IrkksomeConnectionEB connectionItem) {
        if (connectionItem.isIrssiProxyConnection()) {
            sendCallback(IrssiProxyConnectionFragment.newInstance(connectionItem));
        }
        else {
            sendCallback(RegularConnectionFragment.newInstance(connectionItem));
        }
    }

    private void deleteSelectedItems() {
        final long[] checkedItemIds = listView.getCheckedItemIds();
        for (long id : checkedItemIds) {
            connectionDAO.delete(id);
        }
        adapter.setNewData(connectionDAO.getConnectionsForDisplay());
    }

    public interface OnConnectionSelectedListener {
        public void onConnectionSelected(AbstractConnectionFragment connectionFragment);
    }

}
