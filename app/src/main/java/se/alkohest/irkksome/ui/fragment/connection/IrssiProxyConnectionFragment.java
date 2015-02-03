package se.alkohest.irkksome.ui.fragment.connection;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.Date;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;

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
        if (templateConnection != null) {
            setFieldValue(parent, R.id.server_connect_host, templateConnection.getHost());
            setFieldValue(parent, R.id.server_connect_port, String.valueOf(templateConnection.getPort()));
            setFieldValue(parent, R.id.server_connect_username, templateConnection.getUsername());
            setFieldValue(parent, R.id.server_connect_password, templateConnection.getPassword());
            setFieldValue(parent, R.id.server_connect_sshHost, templateConnection.getSshHost());
            setFieldValue(parent, R.id.server_connect_sshUser, templateConnection.getSshUser());
            CheckBox usePubkey = (CheckBox) parent.findViewById(R.id.server_connect_use_pubkey);
            usePubkey.setChecked(templateConnection.isUseKeyPair());
            setEnabled(parent, R.id.server_connect_sshPass, !templateConnection.isUseKeyPair());
            usePubkey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                    if (state) {
                        setEnabled(parent, R.id.server_connect_sshPass, false);
                        // open dialog asking for ssh pw
                        // open progress dialog
                        Intent uploadIntent = new Intent(getActivity(), PubkeyUploadService.class);
                        IrkksomeConnection connection = getConnection();
                        connection.setLastUsed(new Date());
                        connection.save();
                        uploadIntent.putExtra(PubkeyUploadService.CONNECTION_DATA_PK, connection.getId());
                        uploadIntent.putExtra(PubkeyUploadService.CONNECTION_DATA_SSH_PW, connection.getSshPass());
                        getActivity().startService(uploadIntent);
                    }
                    else {
                        setEnabled(parent, R.id.server_connect_sshPass, true);
                    }
                }
            });
        }
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
        connection.setSshHost(getFieldValue(R.id.server_connect_sshHost));
        connection.setSshUser(getFieldValue(R.id.server_connect_sshUser));
        connection.setSshPass(getFieldValue(R.id.server_connect_sshPass));
        connection.setUseSSH(true);
        connection.setUseKeyPair(((CheckBox) getActivity().findViewById(R.id.server_connect_use_pubkey)).isChecked());
        if (connection.equals(templateConnection)) {
            templateConnection.setSshPass(connection.getSshPass());
            return templateConnection;
        }
        else {
            return connection;
        }
    }
}
