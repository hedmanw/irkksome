package se.alkohest.irkksome.irc;

import java.io.IOException;

public class ConnectionIOException extends IOException {
    private ErrorPhase phase;

    public ConnectionIOException(ErrorPhase phase, String msg) {
        super(msg);
        this.phase = phase;
    }

    public ConnectionIOException(ErrorPhase phase) {
        super("We have no clue what went wrong, but something did. Try again!");
        this.phase = phase;
    }

    public ErrorPhase getPhase() {
        return phase;
    }

    public enum ErrorPhase {
        PRE, AUTH, GENERAL, POST
    }
}
