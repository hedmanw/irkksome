package se.alkohest.irkksome.irc;

import android.util.*;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.ConnectionInfo;
import com.trilead.ssh2.ConnectionMonitor;
import com.trilead.ssh2.DebugLogger;
import com.trilead.ssh2.LocalPortForwarder;
import com.trilead.ssh2.ServerHostKeyVerifier;
import com.trilead.ssh2.log.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Random;

public class SSHConnection implements ServerConnection, ConnectionMonitor {
    public static final String AUTH_PUBLIC_KEY = "publickey";
    public static final String AUTH_PASSWORD = "password";
    private static final int MIN_PORT = 49152;
    private static final int MAX_PORT = 65535;
    private static final String TAG = "irkksomeSSH";
    private static final int AUTH_TRIES = 20;
    private final int localPort = getRandomLocalPort();
    private ConnectionData connectionData;
    private ServerConnection irkkConnection;
    private Connection connection;
    private ConnectionInfo connectionInfo;
    private boolean connected;
    private LocalPortForwarder portForwarder;

    public SSHConnection(ConnectionData data) {
        this.connectionData = data;
        this.connected = false;
        this.irkkConnection = new NormalConnection(data.getHost(), localPort);
    }

    @Override
    public void connect() throws IOException {
        connected = true;
        connection = new Connection(connectionData.getSshHost(), connectionData.getSshPort());
        connection.addConnectionMonitor(this);
        DebugLogger logger = new DebugLogger() {
            @Override
            public void log(int level, String className, String message) {
                Log.i(TAG, message);
            }
        };
        Logger.enabled = true;
        Logger.logger = logger;

        boolean shouldAuth = true;

        try {
            connectionInfo = connection.connect(new HostKeyVerifier());
        } catch (IOException e) {
            close();
            Log.e(TAG, e.getCause().getMessage());
            shouldAuth = false;
        }

        if (shouldAuth) {
            int tries = 0;
            while (connected && !connection.isAuthenticationComplete() && tries < AUTH_TRIES) {
                authenticate();
                tries++;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Could not authenticate: (auth tries = " + tries + ")...", e);
                }
            }

            irkkConnection.connect();
        }
    }

    @Override
    public String readLine() throws IOException {
        return irkkConnection.readLine();
    }

    @Override
    public void write(String s) throws IOException {
        irkkConnection.write(s);
    }

    private void authenticate() {
        try {
            if (connection.authenticateWithNone(connectionData.getSshUser())) {
                finishConnection();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            /*if (connection.isAuthMethodAvailable(connectionData.getSshUser(), AUTH_PUBLIC_KEY)) {
                if (connection.authenticateWithPublicKey(connectionData.getSshUser(), connectionData.getKeyPair().getPrivate(), null)) {
                    finishConnection();
                }
            }
            else*/
            if (connection.isAuthMethodAvailable(connectionData.getSshUser(), AUTH_PASSWORD)) {
                if (connectionData.getSshPass() != null && connection.authenticateWithPassword(connectionData.getSshUser(), connectionData.getSshPass())) {
                    finishConnection();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "AUTH exception", e);
        }
    }

    private void finishConnection() {
        try {
            portForwarder = connection.createLocalPortForwarder(new InetSocketAddress(InetAddress.getLocalHost(), localPort), connectionData.getHost(), connectionData.getPort());
        } catch (IOException e) {
            Log.e(TAG, "could not create portforward", e);
        }
        // connection.openSession();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void close() {
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

//    private void uploadPubKey(SSHClient ssh) throws IOException {
//        final Session session = ssh.startSession();
//        X509EncodedKeySpec key = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
//        final Session.Command cmd = session.exec("echo -e '\n' " + key.getEncoded() + " >> ~/.ssh/authorized_keys");
//        Log.getInstance(this.getClass()).i(IOUtils.readFully(cmd.getInputStream()).toString());
//
//        cmd.join(5, TimeUnit.SECONDS);
//        session.close();
//    }

    private static int getRandomLocalPort() {
        return new Random().nextInt(MAX_PORT - MIN_PORT) + MIN_PORT;
    }

    @Override
    public void connectionLost(Throwable reason) {
        close();
        Log.i(TAG, "Connection lost: ", reason);
    }

    public class HostKeyVerifier implements ServerHostKeyVerifier {
        @Override
        public boolean verifyServerHostKey(String hostname, int port, String serverHostKeyAlgorithm, byte[] serverHostKey) throws IOException {
            return true;
        }
    }
}
