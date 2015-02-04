package se.alkohest.irkksome.irc;

import android.util.Base64;

import com.trilead.ssh2.Session;

import java.io.IOException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class SSHKeyUploader extends SSHClient {
    public SSHKeyUploader(ConnectionData data) {
        super(data);
    }

    public void establishAndUpload() {
        establishConnection();
    }

    private void uploadPubKey() {
        final String pubKey = makePubKey(connectionData.getKeyPair().getPublic().getEncoded());

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
        return "ssh-dsa " + Base64.encodeToString(encoded, Base64.NO_WRAP) + " irkksome";
    }

    @Override
    protected void postAuthAction() {
        uploadPubKey();
    }
}
