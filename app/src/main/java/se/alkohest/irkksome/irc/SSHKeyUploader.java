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

    public void establishAndUpload() throws ConnectionIOException {
        establishConnection();
    }

    private void uploadPubKey() throws ConnectionIOException {
        try {
            final String pubKey = KeyProvider.getPubkey();
            final Session session = connection.openSession();
            session.execCommand(ECHO_LITERAL + pubKey + APPEND + AUTHORIZED_KEYS);
            session.close();
        } catch (IOException e) {
            throw new ConnectionIOException(ConnectionIOException.ErrorPhase.POST, "Could not upload pubkey.");
        }
    }

    @Override
    protected void postAuthAction() throws ConnectionIOException {
        uploadPubKey();
    }
}
