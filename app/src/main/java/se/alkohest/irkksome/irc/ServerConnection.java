package se.alkohest.irkksome.irc;

import java.io.IOException;

/**
 * This interface handles a connection.
 */
public interface ServerConnection {

    public void connect() throws IOException;

    public boolean isConnected();

    public String readLine() throws IOException;

    public void write(String s) throws IOException;

    public void close();
}
