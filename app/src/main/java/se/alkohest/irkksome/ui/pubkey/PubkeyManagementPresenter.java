package se.alkohest.irkksome.ui.pubkey;

import android.content.Context;

import se.alkohest.irkksome.model.entity.SSHConnection;

public interface PubkeyManagementPresenter {
    public void runUploadTask(long connectionPK, String connectionPassword);
    public void executeUploadTask(SSHConnection connection);
    public void createPubkey(Context context);
}
