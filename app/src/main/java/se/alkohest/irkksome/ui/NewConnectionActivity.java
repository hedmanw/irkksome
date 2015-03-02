package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.Server;
import se.alkohest.irkksome.model.api.ServerConnectionListener;
import se.alkohest.irkksome.model.api.ServerManager;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.ui.fragment.connection.AbstractConnectionFragment;
import se.alkohest.irkksome.ui.fragment.connection.ConnectionsListFragment;

/**
 * Created by wilhelm 2014-11-18.
 */
public class NewConnectionActivity extends Activity implements ConnectionsListFragment.OnConnectionSelectedListener, AbstractConnectionFragment.OnConnectPressedListener, ServerConnectionListener {
    public static final int MAKE_CONNECTION = 11;
    public static final int FRESH_STARTUP_CONNECTION = 1;
    public static final int CONNECTION_ESTABLISHED = 1337;
    public static final String REQUEST_CODE = "requestCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        boolean canDelete = getIntent().getIntExtra(REQUEST_CODE, 0) == FRESH_STARTUP_CONNECTION;
        ConnectionsListFragment connectFragment = ConnectionsListFragment.newInstance(canDelete);
        fragmentTransaction.add(R.id.fragment_container, connectFragment, ConnectionsListFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onConnectionSelected(AbstractConnectionFragment connectionFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, connectionFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onConnectPressed(IrkksomeConnection irkksomeConnection) {
        ServerManager serverManager = ServerManager.INSTANCE;
        final Server pendingServer = serverManager.establishConnection(irkksomeConnection);
        pendingServer.addServerConnectionListener(this);
        pendingServer.setListener(CallbackHandler.getInstance());
        final Button button = (Button) findViewById(R.id.server_connect_button);
        button.setText("Connecting...");
        button.setEnabled(false);
        findViewById(R.id.server_connect_progress).setVisibility(View.VISIBLE);
    }

    @Override
    public void connectionEstablished(Server server) {
        setResult(CONNECTION_ESTABLISHED);
        finish();
    }

    @Override
    public void connectionDropped(Server server) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Button button = (Button) findViewById(R.id.server_connect_button);
                if (button != null) {
                    button.setText("Connect");
                    button.setEnabled(true);
                    findViewById(R.id.server_connect_progress).setVisibility(View.GONE);
                }

            }
        });
    }
}
