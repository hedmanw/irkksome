package se.alkohest.irkksome.irc;

import android.util.Base64;

import com.trilead.ssh2.Session;

import java.io.IOException;

import se.alkohest.irkksome.model.entity.SSHConnection;

public class SSHKeyUploader extends SSHClient {
    public SSHKeyUploader(SSHConnection data) {
        super(data);
    }

    public void establishAndUpload() {
        establishConnection();
    }

    private void uploadPubKey() {
        final String pubKey = makePubKey(sshConnectionData.getKeyPair().getPublic().getEncoded());

        try {
            final Session session = connection.openSession();
//            session.execCommand("echo -e '\n' " + key.getEncoded() + " >> ~/.ssh/authorized_keys");

            session.execCommand("echo -e " + pubKey + " >> ~/.ssh/authorized_keys");
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String makePubKey(byte[] encoded) {
        return "ssh-dss " + Base64.encodeToString(encoded, Base64.NO_WRAP) + " irkksome";
    }

    @Override
    protected void postAuthAction() {
        uploadPubKey();
    }
}
