package se.alkohest.irkksome.ui.fragment.connection;

import android.app.IntentService;
import android.content.Intent;

import se.alkohest.irkksome.irc.SSHKeyUploader;
import se.alkohest.irkksome.model.api.dao.IrkksomeConnectionDAO;
import se.alkohest.irkksome.model.api.local.IrkksomeConnectionDAOLocal;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;

public class PubkeyUploadService extends IntentService {
    public static final String CONNECTION_DATA_PK = "connectionDataPK";
    public static final String CONNECTION_DATA_SSH_PW = "connectionDataSSHPW";

    public PubkeyUploadService() {
        super("Irkksome SSH pubkey upload service (Greger)");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long pk = intent.getLongExtra(CONNECTION_DATA_PK, -1);
        String sshPassword = intent.getStringExtra(CONNECTION_DATA_SSH_PW);

        IrkksomeConnectionDAOLocal irkksomeConnectionDAO = new IrkksomeConnectionDAO();
        IrkksomeConnection connection = irkksomeConnectionDAO.findById(pk);
        connection.setSshPass(sshPassword);

        SSHKeyUploader sshKeyUploader = new SSHKeyUploader(connection);
        sshKeyUploader.establishAndUpload();
    }
}
