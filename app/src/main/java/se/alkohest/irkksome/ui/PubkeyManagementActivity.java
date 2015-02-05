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

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.irc.SSHKeyUploader;
import se.alkohest.irkksome.model.api.KeyPairManager;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.model.entity.SSHConnection;
import se.alkohest.irkksome.ui.fragment.pubkey.PubkeyDisabledFragment;
import se.alkohest.irkksome.ui.fragment.pubkey.PubkeyEnabledFragment;


public class PubkeyManagementActivity extends Activity implements PubkeyDisabledFragment.CreatePubkeyPressListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        KeyPairManager kpm = new KeyPairManager(this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        if (kpm.hasKeyPair()) {
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

        new PubkeyUploadTask().execute();
    }

    @Override
    public void createPubkey() {
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
            KeyPairManager kph = new KeyPairManager(PubkeyManagementActivity.this);
            try {
                host.setKeyPair(kph.getKeyPair());
            } catch (IOException e) {
                e.printStackTrace();
            }

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
