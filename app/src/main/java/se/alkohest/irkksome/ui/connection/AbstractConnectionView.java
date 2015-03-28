package se.alkohest.irkksome.ui.connection;

public interface AbstractConnectionView {
    public void showProgress();
    public void connectionSuccess();
    public void connectionFailure();
}
