package se.alkohest.irkksome.irc;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.LocalPortForwarder;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.security.PublicKey;
import java.util.Random;

/**
 * Created by oed on 7/26/14.
 */
public class SSHConnection implements Connection, HostKeyVerifier {

    private static final String LOCALHOST = "127.0.0.1";
    private static final int MIN_PORT = 49152;
    private static final int MAX_PORT = 65535;
    private final int localPort = getLocalPort();
    private final boolean sshKeyCreated;
    private String sshAddress;
    private String sshUser;
    private String sshPass;
    private String ircHost;
    private int ircPort;
    private Connection socketConnection;

    private SSHClient ssh;
    private ServerSocket serverSocket;

    private final Log LOG = Log.getInstance(this.getClass());

    public SSHConnection(ConnectionData data,
                         Class<? extends Connection> socketConnection) {
        this.sshAddress = data.getSshHost();
        this.sshUser = data.getSshUser();
        this.sshPass = data.getSshPass();
        this.ircHost = data.getHost();
        this.ircPort = data.getPort();
        this.sshKeyCreated = data.isSSHKeySaved();
        try {
            this.socketConnection = socketConnection.getConstructor(String.class, int.class)
                    .newInstance(LOCALHOST, localPort);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connect() throws IOException {
        createTunnel();
        socketConnection.connect();
    }

    @Override
    public boolean isConnected() {
        return socketConnection.isConnected();
//        return true;
    }

    @Override
    public String readLine() throws IOException {
        return socketConnection.readLine();
//        return "";
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
        ssh.authPassword(sshUser, sshPass);

        final LocalPortForwarder.Parameters params
                = new LocalPortForwarder.Parameters(LOCALHOST, localPort, ircHost, ircPort);

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

    private static int getLocalPort() {
        return new Random().nextInt(MAX_PORT - MIN_PORT) + MIN_PORT;
    }

    @Override
    public boolean verify(String hostname, int port, PublicKey key) {
        return true;
    }
}
