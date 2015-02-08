package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.security.KeyPair;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.irc.SSHKeyUploader;
import se.alkohest.irkksome.model.api.KeyPairManager;
import se.alkohest.irkksome.model.api.dao.SSHConnectionDAO;
import se.alkohest.irkksome.model.api.local.SSHConnectionDAOLocal;
import se.alkohest.irkksome.model.entity.SSHConnection;
import se.alkohest.irkksome.ui.fragment.pubkey.PubkeyDisabledFragment;
import se.alkohest.irkksome.ui.fragment.pubkey.PubkeyEnabledFragment;
import se.alkohest.irkksome.util.KeyProvider;


public class PubkeyManagementActivity extends Activity implements PubkeyDisabledFragment.CreatePubkeyPressListener, PubkeyEnabledFragment.PubkeyManagementListener {
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
            fragment = PubkeyEnabledFragment.newInstance(this);
        } else {
            fragment = PubkeyDisabledFragment.newInstance(this);
        }
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void uploadPressed() {
        final long sshConnectionPK = getIntent().getLongExtra(SSH_CONNECTION_PK, -1);
        final String sshConnectionPassword = getIntent().getStringExtra(SSH_CONNECTION_PASSWORD);
        // If we have some kind of token, use it for the connection
        if (sshConnectionPK != -1) {
            final SSHConnection connection = sshConnectionDAO.findById(sshConnectionPK);
            if (sshConnectionPassword != null && !sshConnectionPassword.equals("")) {
                connection.setSshPassword(sshConnectionPassword);
                performUploadTask(connection);
            } else {
                showPasswordDialogAndConnect(connection);
            }
        }
        else {
            // TODO: something went wrong, notify user to create a new connection or something
        }
    }

    private void showPasswordDialogAndConnect(final SSHConnection connection) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter the password for " + connection.toString()).setTitle("SSH password");
        final EditText passwordField = new EditText(this);
        passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(passwordField);
        builder.setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                connection.setSshPassword(passwordField.getText().toString());
                performUploadTask(connection);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void performUploadTask(SSHConnection connection) {
        // Open progress dialog
        new PubkeyUploadTask().execute(connection);
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
        fragmentTransaction.replace(R.id.fragment_container, PubkeyEnabledFragment.newInstance(this));
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
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
            // finish activity?
        }
    }
}
