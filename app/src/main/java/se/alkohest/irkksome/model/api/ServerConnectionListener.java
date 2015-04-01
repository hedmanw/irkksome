package se.alkohest.irkksome.model.api;

/**
 * Created by oed on 7/21/14.
 */
public interface ServerConnectionListener {
    public void connectionEstablished(Server server);
    public void connectionDropped(Server server);
}
