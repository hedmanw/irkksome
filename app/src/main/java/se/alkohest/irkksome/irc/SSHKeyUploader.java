package se.alkohest.irkksome.irc;

import android.util.Base64;

import com.trilead.ssh2.Session;

import java.io.IOException;

import se.alkohest.irkksome.model.entity.SSHConnection;

public class SSHKeyUploader extends SSHClient {
    public static final String ECHO_LITERAL = "echo -e ";
    public static final String APPEND = " >> ";
    public static final String AUTHORIZED_KEYS = "~/.ssh/authorized_keys";
    public static final String TEST_FILE = "~/irkksome-text.txt";

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
            session.execCommand(ECHO_LITERAL + pubKey + APPEND + TEST_FILE); // TODO: actual file
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
