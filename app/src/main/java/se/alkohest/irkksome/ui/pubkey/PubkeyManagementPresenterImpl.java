package se.alkohest.irkksome.ui.pubkey;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.security.KeyPair;

import se.alkohest.irkk.entity.SSHConnectionImpl;
import se.alkohest.irkk.irc.ConnectionIOException;
import se.alkohest.irkk.irc.SSHKeyUploader;
import se.alkohest.irkk.util.KeyProvider;
import se.alkohest.irkksome.model.api.KeyPairManager;
import se.alkohest.irkksome.model.api.dao.SSHConnectionDAO;
import se.alkohest.irkksome.model.entity.SSHConnection;

public class PubkeyManagementPresenterImpl implements PubkeyManagementPresenter {
    private SSHConnectionDAO sshConnectionDAO = new SSHConnectionDAO();

    private PubkeyManagementView view;

    public PubkeyManagementPresenterImpl(PubkeyManagementView view) {
        this.view = view;
    }

    @Override
    public void runUploadTask(long sshConnectionPK, String sshConnectionPassword) {
        // If we have some kind of token, use it for the connection
        if (sshConnectionPK != -1) {
            final SSHConnection connection = sshConnectionDAO.findById(sshConnectionPK);
            if (sshConnectionPassword != null && !sshConnectionPassword.equals("")) {
                connection.setSshPassword(sshConnectionPassword);
                executeUploadTask(connection);
            } else {
                view.showPasswordDialog(connection);
            }
        }
        else {
            // TODO: something went wrong, notify user to create a new connection or something
        }
    }

    @Override
    public void executeUploadTask(SSHConnection connection) {
        view.showProgress();
        new PubkeyUploadTask().execute(connection);
    }

    @Override
    public void createPubkey(Context context) {
        KeyPairManager keyPairManager = new KeyPairManager(context);
        try {
            KeyPair keyPair = keyPairManager.getKeyPair();
            KeyProvider.initialize(keyPair.getPublic(), keyPair.getPrivate());
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: Notify user!
        }
    }

    private class PubkeyUploadTask extends AsyncTask<SSHConnection, Void, UploadTaskFinishStatus> {
        @Override
        protected UploadTaskFinishStatus doInBackground(SSHConnection... hosts) {
            SSHConnection host = hosts[0];
            host.setUseKeyPair(false);

            SSHKeyUploader sshKeyUploader = new SSHKeyUploader(new SSHConnectionImpl(host.getSshHost(), host.getSshUser(), host.getSshPassword(), host.getSshPort(), host.isUseKeyPair()));
            try {
                sshKeyUploader.establishAndUpload();
            } catch (ConnectionIOException e) {
                return new UploadTaskFinishStatus(e.getPhase(), e.getMessage());
            }
            sshKeyUploader.closeAll();
            return new UploadTaskFinishStatus(null, null);
        }

        @Override
        protected void onPostExecute(UploadTaskFinishStatus result) {
            // report things based on result?
            if (result.taskWasFinished()) {
                view.uploadSuccess();
                // set some variable in the connection?
                // finish activity?
            }
            else {
                view.uploadFailure(result.message);
            }
        }
    }

    private static class UploadTaskFinishStatus {
        private ConnectionIOException.ErrorPhase errorPhase;
        private String message;

        UploadTaskFinishStatus(ConnectionIOException.ErrorPhase errorPhase, String message) {
            this.errorPhase = errorPhase;
            this.message = message;
        }

        boolean taskWasFinished() {
            return errorPhase == null && message == null;
        }
    }
}
