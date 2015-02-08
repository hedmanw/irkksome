package se.alkohest.irkksome.irc;

import com.trilead.ssh2.Session;

import java.io.IOException;

import se.alkohest.irkksome.model.entity.SSHConnection;
import se.alkohest.irkksome.util.KeyEncodingUtil;
import se.alkohest.irkksome.util.KeyProvider;

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
        try {
            final String pubKey = KeyProvider.getPubkey();
            final Session session = connection.openSession();
            session.execCommand(ECHO_LITERAL + pubKey + APPEND + AUTHORIZED_KEYS);
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void postAuthAction() {
        uploadPubKey();
    }
}
