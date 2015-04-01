package se.alkohest.irkksome.ui.pubkey;

import se.alkohest.irkksome.model.entity.SSHConnection;

public interface PubkeyManagementView {
    public void showPasswordDialog(SSHConnection connection);
    public void showProgress();
    public void uploadSuccess();
    public void uploadFailure(String errorMessage);
}
