package se.alkohest.irkksome.ui.fragment.connection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public void inflateConnectionView(ViewGroup parent) {
        if (templateConnection != null) {
            setFieldValue(parent, R.id.server_connect_host, templateConnection.getHost());
            setFieldValue(parent, R.id.server_connect_port, String.valueOf(templateConnection.getPort()));
            setFieldValue(parent, R.id.server_connect_username, templateConnection.getUsername());
            setFieldValue(parent, R.id.server_connect_password, templateConnection.getPassword());
            setFieldValue(parent, R.id.server_connect_sshHost, templateConnection.getSshHost());
            setFieldValue(parent, R.id.server_connect_sshUser, templateConnection.getSshUser());
        }
    }

    @Override
    public IrkksomeConnection getConnection() {
        IrkksomeConnectionEB connection = connectionDAO.create();
        connection.setHost(getFieldValue(R.id.server_connect_host));
        connection.setPort(Integer.parseInt(getFieldValue(R.id.server_connect_port)));
        final String userName = getFieldValue(R.id.server_connect_username);
        connection.setUsername(userName);
        connection.setNickname(userName); // This field is not present in UI. The hack is to get hilights to recognize "your" name
        connection.setPassword(getFieldValue(R.id.server_connect_password));
        connection.setSshHost(getFieldValue(R.id.server_connect_sshHost));
        connection.setSshUser(getFieldValue(R.id.server_connect_sshUser));
        connection.setSshPass(getFieldValue(R.id.server_connect_sshPass));
        connection.setUseSSH(true);
        if (connection.equals(templateConnection)) {
            templateConnection.setSshPass(connection.getSshPass());
            return templateConnection;
        }
        else {
            return connection;
        }
    }
}
