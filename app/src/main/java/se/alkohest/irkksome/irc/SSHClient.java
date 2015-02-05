package se.alkohest.irkksome.irc;

import android.util.Base64;
import android.util.Log;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.ConnectionInfo;
import com.trilead.ssh2.ConnectionMonitor;
import com.trilead.ssh2.DebugLogger;
import com.trilead.ssh2.LocalPortForwarder;
import com.trilead.ssh2.ServerHostKeyVerifier;
import com.trilead.ssh2.log.Logger;

import java.io.IOException;
import java.util.Random;

import se.alkohest.irkksome.model.entity.SSHConnection;
import se.alkohest.irkksome.util.Base64Encoder;

// TODO: Close everything on failures?
public abstract class SSHClient implements ConnectionMonitor {
    public static final String AUTH_PUBLIC_KEY = "publickey";
    public static final String AUTH_PASSWORD = "password";
    public static final String TAG = "irkksomeSSH";
    private static final Random PORTFORWARD_RANDOM = new Random();
    private static final boolean DEBUG_SSH = true;
    private static final int MIN_PORT = 49152;
    private static final int MAX_PORT = 65535;
    private static final int AUTH_ATTEMPTS = 4;

    protected final int localPort = getRandomLocalPort();
    protected SSHConnection sshConnectionData;
    protected LocalPortForwarder portForwarder;
    protected Connection connection;
    protected boolean connected;
    protected ConnectionInfo connectionInfo;
    private char[] decodedPemKey;

    static {
        if (DEBUG_SSH) {
            DebugLogger logger = new DebugLogger() {
                @Override
                public void log(int level, String className, String message) {
                    Log.i(TAG, message);
                }
            };
            Logger.enabled = true;
            Logger.logger = logger;
        }
    }


    public SSHClient(SSHConnection data) {
        this.sshConnectionData = data;
        if (sshConnectionData.isUseKeyPair() && sshConnectionData.getKeyPair() != null) {
            final String pemKey = Base64Encoder.createPrivkey(sshConnectionData.getKeyPair().getPrivate().getEncoded());
            decodedPemKey = pemKey.toCharArray();
        }
    }

    protected void establishConnection() {
        connected = true;
        connection = new Connection(sshConnectionData.getSshHost(), sshConnectionData.getSshPort());
        connection.addConnectionMonitor(this);

        boolean shouldAuth = true;

        try {
            connectionInfo = connection.connect(new HostKeyVerifier());
        } catch (IOException e) {
            closeAll();
            Log.e(TAG, e.getCause().getMessage());
            shouldAuth = false;
        }

        if (shouldAuth) {
            if (authLoop()) {
                postAuthAction();
            }
            else {
                closeAll();
            }
//            else {
//                Throw auth error?
//            }
        }
        // Throw connection error?
    }

    private boolean authLoop() {
        int tries = 0;
        boolean shouldRetryAuth = true;
        while (connected && shouldRetryAuth) {
            authenticate();
            tries++;
            shouldRetryAuth = tries < AUTH_ATTEMPTS && !connection.isAuthenticationComplete() ;
            if (shouldRetryAuth) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // just die
                }
            }
        }
        return connection.isAuthenticationComplete();
    }

    private boolean authenticate() {
        try {
            if (connection.authenticateWithNone(sshConnectionData.getSshUser())) {
                return true;
            }
            if (sshConnectionData.isUseKeyPair() && decodedPemKey != null) {
                if (connection.isAuthMethodAvailable(sshConnectionData.getSshUser(), AUTH_PUBLIC_KEY)) {
                    if (connection.authenticateWithPublicKey(sshConnectionData.getSshUser(), decodedPemKey, null)) {
                        return true;
                    }
                }
            }
            if (connection.isAuthMethodAvailable(sshConnectionData.getSshUser(), AUTH_PASSWORD)) {
                if (sshConnectionData.getSshPassword() != null && connection.authenticateWithPassword(sshConnectionData.getSshUser(), sshConnectionData.getSshPassword())) {
                    return true;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "AUTH exception", e);
        }
        return false;
    }

    protected abstract void postAuthAction();

    public void closeAll() {
        if (connected) {
            connected = false;
            if (portForwarder != null) {
                try {
                    portForwarder.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public void connectionLost(Throwable reason) {
        closeAll();
        Log.i(TAG, "Connection lost: ", reason);
    }

    private static int getRandomLocalPort() {
        return PORTFORWARD_RANDOM.nextInt(MAX_PORT - MIN_PORT) + MIN_PORT;
    }

    public class HostKeyVerifier implements ServerHostKeyVerifier {
        @Override
        public boolean verifyServerHostKey(String hostname, int port, String serverHostKeyAlgorithm, byte[] serverHostKey) throws IOException {
            return true;
        }
    }
}
