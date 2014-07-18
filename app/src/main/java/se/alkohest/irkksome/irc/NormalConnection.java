package se.alkohest.irkksome.irc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * This class represents a normal connection.
 */
public class NormalConnection implements Connection {

    private final String host;
    private final int port;

    private boolean connected;
    private BufferedReader input;
    private BufferedWriter output;

    public NormalConnection(String host, int port) {
        this.host = host;
        this.port = port;
        connected = false;
    }

    @Override
    public void connect() throws IOException {
        Socket socket = new Socket(host, port);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        connected = true;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public String readLine() throws IOException {
        checkReady();
        return input.readLine();
    }

    @Override
    public void write(String s) throws IOException {
        checkReady();
        output.write(s);
        output.flush();
    }

    private void checkReady() {
        if (!connected) {
            throw new IllegalStateException("Connection has not been initialized.");
        }
    }
}
