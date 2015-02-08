package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.security.KeyPair;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.irc.SSHKeyUploader;
import se.alkohest.irkksome.model.api.KeyPairManager;
import se.alkohest.irkksome.model.api.dao.SSHConnectionDAO;
import se.alkohest.irkksome.model.api.local.SSHConnectionDAOLocal;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.model.entity.SSHConnection;
import se.alkohest.irkksome.ui.fragment.pubkey.PubkeyDisabledFragment;
import se.alkohest.irkksome.ui.fragment.pubkey.PubkeyEnabledFragment;
import se.alkohest.irkksome.util.KeyProvider;


public class PubkeyManagementActivity extends Activity implements PubkeyDisabledFragment.CreatePubkeyPressListener {
    public static final String SSH_CONNECTION_PK = "sshConnectionPK";
    public static final String SSH_CONNECTION_PASSWORD= "sshConnectionPassword";
    private SSHConnectionDAOLocal sshConnectionDAO = new SSHConnectionDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        if (KeyProvider.hasKeys()) {
            fragment = new PubkeyEnabledFragment();
        } else {
            fragment = PubkeyDisabledFragment.newInstance(this);
        }
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void uploadPubkey() {
        // If we have some kind of token, use it for the connection
        // otherwise, select an existing connection to upload to and open dialog asking for ssh pw
        // open progress dialog
        final long sshConnectionPK = getIntent().getLongExtra(SSH_CONNECTION_PK, -1);
        final String sshConnectionPassword = getIntent().getStringExtra(SSH_CONNECTION_PASSWORD);
        if (sshConnectionPK != -1) {
            SSHConnection connection = sshConnectionDAO.findById(sshConnectionPK);
            connection.setSshPassword(sshConnectionPassword);
            new PubkeyUploadTask().execute(connection);
        }
    }

    @Override
    public void createPubkey() {
        KeyPairManager keyPairManager = new KeyPairManager(this);
        try {
            KeyPair keyPair = keyPairManager.getKeyPair();
            KeyProvider.initialize(keyPair.getPublic(), keyPair.getPrivate());
            KeyProvider.printKeyPair();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: Notify user!
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new PubkeyEnabledFragment());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pubkey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_pubkey_copy:
                // TODO: Copy pubkey to clipboard
                break;
            case R.id.action_upload_pubkey:
                uploadPubkey();
                break;
        }
        return true;
    }

    private class PubkeyUploadTask extends AsyncTask<SSHConnection, Void, Boolean> {
        @Override
        protected Boolean doInBackground(SSHConnection... hosts) {
            SSHConnection host = hosts[0];
            host.setUseKeyPair(false);

            SSHKeyUploader sshKeyUploader = new SSHKeyUploader(host);
            sshKeyUploader.establishAndUpload();
            sshKeyUploader.closeAll();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // close progress thing
            // report things based on result
            // set some variable in the connection?
        }
    }
}
