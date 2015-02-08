package se.alkohest.irkksome.ui.fragment.connection;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.io.IOException;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.KeyPairManager;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.model.entity.SSHConnection;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;
import se.alkohest.irkksome.model.impl.SSHConnectionEB;
import se.alkohest.irkksome.ui.PubkeyManagementActivity;
import se.alkohest.irkksome.util.KeyProvider;

public class IrssiProxyConnectionFragment extends AbstractConnectionFragment {
    public static AbstractConnectionFragment newInstance() {
        return new IrssiProxyConnectionFragment();
    }

    public static AbstractConnectionFragment newInstance(IrkksomeConnection irkksomeConnection) {
        AbstractConnectionFragment fragment = newInstance();
        Bundle args = new Bundle();
        args.putLong(CONNECTION_ARGUMENT, irkksomeConnection.getId());
        fragment.setArguments(args);
        return fragment;
    }

    public IrssiProxyConnectionFragment() {
    }

    @Override
    protected int getIcon() {
        return R.drawable.connection_icon_purple;
    }

    @Override
    protected int getHeadingStringId() {
        return R.string.connection_type_irssi;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_irssi_proxy_connection;
    }

    @Override
    public void inflateConnectionView(final ViewGroup parent) {
        CheckBox usePubkey = (CheckBox) parent.findViewById(R.id.server_connect_use_pubkey);
        if (templateConnection != null) {
            setFieldValue(parent, R.id.server_connect_host, templateConnection.getHost());
            setFieldValue(parent, R.id.server_connect_port, String.valueOf(templateConnection.getPort()));
            setFieldValue(parent, R.id.server_connect_username, templateConnection.getUsername());
            setFieldValue(parent, R.id.server_connect_password, templateConnection.getPassword());
            setFieldValue(parent, R.id.server_connect_sshHost, templateConnection.getSSHConnection().getSshHost());
            setFieldValue(parent, R.id.server_connect_sshUser, templateConnection.getSSHConnection().getSshUser());
            usePubkey.setChecked(templateConnection.getSSHConnection().isUseKeyPair());
            setEnabled(parent, R.id.server_connect_sshPass, !templateConnection.getSSHConnection().isUseKeyPair());
        }
        usePubkey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                if (state) {
                    setEnabled(parent, R.id.server_connect_sshPass, false);
                    SSHConnection sshConnection = getSshConnection();
                    Intent intent = new Intent(getActivity(), PubkeyManagementActivity.class);
                    sshConnection.save();
                    intent.putExtra(PubkeyManagementActivity.SSH_CONNECTION_PK, sshConnection.getId());
                    intent.putExtra(PubkeyManagementActivity.SSH_CONNECTION_PASSWORD, sshConnection.getSshPassword());
                    startActivity(intent);
                }
                else {
                    setEnabled(parent, R.id.server_connect_sshPass, true);
                }
            }
        });
    }

    private void setEnabled(ViewGroup group, int id, boolean enabled) {
        group.findViewById(id).setEnabled(enabled);
    }

    @Override
    public IrkksomeConnection getConnection() {
        IrkksomeConnectionEB connection = connectionDAO.create();
        connection.setHost(getFieldValue(R.id.server_connect_host));
        connection.setPort(Integer.parseInt(getFieldValue(R.id.server_connect_port))); // When this is left empty, it crashes.
        final String userName = getFieldValue(R.id.server_connect_username);
        connection.setUsername(userName);
        connection.setNickname(userName); // This field is not present in UI. The hack is to get hilights to recognize "your" name
        connection.setPassword(getFieldValue(R.id.server_connect_password));
        SSHConnection sshConnection = getSshConnection();
        connection.setSSHConnection(sshConnection);
        if (connection.equals(templateConnection)) {
            templateConnection.getSSHConnection().setSshPassword(sshConnection.getSshPassword());
            return templateConnection;
        }
        else {
            return connection;
        }
    }

    private SSHConnection getSshConnection() {
        SSHConnection sshConnection = new SSHConnectionEB();
        sshConnection.setSshHost(getFieldValue(R.id.server_connect_sshHost));
        sshConnection.setSshUser(getFieldValue(R.id.server_connect_sshUser));
        sshConnection.setSshPassword(getFieldValue(R.id.server_connect_sshPass));
        sshConnection.setUseKeyPair(((CheckBox) getActivity().findViewById(R.id.server_connect_use_pubkey)).isChecked());
        return sshConnection;
    }
}
