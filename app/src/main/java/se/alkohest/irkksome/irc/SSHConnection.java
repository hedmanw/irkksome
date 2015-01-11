package se.alkohest.irkksome.irc;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.LocalPortForwarder;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by oed on 7/26/14.
 */
public class SSHConnection implements Connection, HostKeyVerifier {

    private static final String LOCALHOST = "127.0.0.1";
    private static final int MIN_PORT = 49152;
    private static final int MAX_PORT = 65535;
    private final int localPort = getLocalPort();
    private final boolean sshKeyCreated;
    private final KeyPair keyPair;
    private String sshAddress;
    private String sshUser;
    private String sshPass;
    private String ircHost;
    private int ircPort;
    private Connection socketConnection;

    private SSHClient ssh;
    private ServerSocket serverSocket;

    public SSHConnection(ConnectionData data) {
        this.sshAddress = data.getSshHost();
        this.sshUser = data.getSshUser();
        this.sshPass = data.getSshPass();
        this.ircHost = data.getHost();
        this.ircPort = data.getPort();
        this.sshKeyCreated = false; //data.isSSHKeySaved();
        this.keyPair = data.getKeyPair();
        this.socketConnection = new NormalConnection(LOCALHOST, localPort);
    }

    @Override
    public void connect() throws IOException {
        createTunnel();
        socketConnection.connect();
    }

    @Override
    public boolean isConnected() {
        return socketConnection.isConnected();
    }

    @Override
    public String readLine() throws IOException {
        return socketConnection.readLine();
    }

    @Override
    public void write(String s) throws IOException {
        socketConnection.write(s);
    }

    @Override
    public void close() {
        socketConnection.close();
        try {
            ssh.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTunnel() throws IOException {
        ssh = new SSHClient();
        ssh.addHostKeyVerifier(this);
        ssh.connect(sshAddress);

        if (!sshKeyCreated) {
            ssh.authPassword(sshUser, sshPass);
//            uploadPubKey(ssh);
        } else {
            // auth key
        }

        final LocalPortForwarder.Parameters params = new LocalPortForwarder.Parameters(LOCALHOST, localPort, ircHost, ircPort);

        serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(params.getLocalPort()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ssh.newLocalPortForwarder(params, serverSocket).listen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void uploadPubKey(SSHClient ssh) throws IOException {
        final Session session = ssh.startSession();
        X509EncodedKeySpec key = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
        final Session.Command cmd = session.exec("echo -e '\n' " + key.getEncoded() + " >> ~/.ssh/authorized_keys");
        Log.getInstance(this.getClass()).i(IOUtils.readFully(cmd.getInputStream()).toString());

        cmd.join(5, TimeUnit.SECONDS);
        session.close();
    }

    private static int getLocalPort() {
        return new Random().nextInt(MAX_PORT - MIN_PORT) + MIN_PORT;
    }

    @Override
    public boolean verify(String hostname, int port, PublicKey key) {
        return true;
    }
}
