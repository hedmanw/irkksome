package se.alkohest.irkksome.irc;

import com.trilead.ssh2.Session;

import java.io.IOException;
import java.security.spec.X509EncodedKeySpec;

public class SSHKeyUploader extends SSHClient {
    public SSHKeyUploader(ConnectionData data) {
        super(data);
    }

    public void establishAndUpload() {
        establishConnection();
    }

    private void uploadPubKey() {
        try {
            final Session session = connection.openSession();
//            X509EncodedKeySpec key = new X509EncodedKeySpec(connectionData.getKeyPair().getPublic().getEncoded());
//            session.execCommand("echo -e '\n' " + key.getEncoded() + " >> ~/.ssh/authorized_keys");
            session.execCommand("echo -e '\n' hej >> ~/irkksome-test.txt");
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void postAuthAction() {
        uploadPubKey();
    }

    @Override
    protected void closeAll() {
        if (connected) {
            // close session?
        }

        super.closeAll();
    }
}
