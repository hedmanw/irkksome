package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.ServerManager;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.ui.fragment.connection.AbstractConnectionFragment;
import se.alkohest.irkksome.ui.fragment.connection.ConnectionItem;
import se.alkohest.irkksome.ui.fragment.connection.ConnectionsListFragment;

/**
 * Created by wilhelm 2014-11-18.
 */
public class NewConnectionActivity extends Activity implements ConnectionsListFragment.OnConnectionSelectedListener, AbstractConnectionFragment.OnConnectPressedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_connection);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ConnectionsListFragment connectFragment = ConnectionsListFragment.newInstance();
        fragmentTransaction.add(R.id.fragment_container, connectFragment, ConnectionsListFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onConnectionSelected(ConnectionItem connectionItem) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AbstractConnectionFragment connectFragment = connectionItem.getConnectionFragment();
        fragmentTransaction.replace(R.id.fragment_container, connectFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onConnectPressed(IrkksomeConnection irkksomeConnection) {
        ServerManager serverManager = ServerManager.INSTANCE;

        // TODO: gör saker och ting
//        final Server pendingServer = serverManager.establishConnection(irkksomeConnection);
//        pendingServer.setListener(new CallbackHandler(this, serverManager.getUnreadStack()));
//        connectionsList.expandGroup(serverManager.getServers().indexOf(serverManager.getActiveServer()));
    }
}
