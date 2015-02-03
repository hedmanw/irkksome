package se.alkohest.irkksome.irc;

import com.trilead.ssh2.Session;

import java.security.spec.X509EncodedKeySpec;

public class SSHKeyUploader extends SSHClient {
    public SSHKeyUploader(ConnectionData data) {
        super(data);
    }

    private void uploadPubKey() {
//        final Session session = ssh.startSession();
//        X509EncodedKeySpec key = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
//        final Session.Command cmd = session.exec("echo -e '\n' " + key.getEncoded() + " >> ~/.ssh/authorized_keys");

//        cmd.join(5, TimeUnit.SECONDS);
//        session.close();
    }


    @Override
    protected void postAuthAction() {
        uploadPubKey();
    }

    @Override
    protected void closeAll() {
        if (connected) {
            // close session
        }

        super.closeAll();
    }
}
