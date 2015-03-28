package se.alkohest.irkksome.ui.connection;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import se.alkohest.irkksome.R;

/**
 * Created by wilhelm 2014-11-18.
 */
public class NewConnectionActivity extends Activity implements ConnectionsListFragment.OnConnectionSelectedListener {
    public static final int MAKE_CONNECTION = 11;
    public static final int FRESH_STARTUP_CONNECTION = 1;
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
}
