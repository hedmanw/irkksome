package se.alkohest.irkksome.ui.fragment.connection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;

public class RegularConnectionFragment extends AbstractConnectionFragment {

    public static AbstractConnectionFragment newInstance() {
        return new RegularConnectionFragment();
    }

    public static AbstractConnectionFragment newInstance(IrkksomeConnection irkksomeConnection) {
        AbstractConnectionFragment fragment = newInstance();
        Bundle args = new Bundle();
        args.putLong(CONNECTION_ARGUMENT, irkksomeConnection.getId());
        fragment.setArguments(args);
        return fragment;
    }

    public RegularConnectionFragment() {
    }

    @Override
    protected int getIcon() {
        return R.drawable.connection_icon_blue;
    }

    @Override
    protected int getHeadingStringId() {
        return R.string.connection_type_regular;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_regular_connection;
    }

    @Override
    public void inflateConnectionView(ViewGroup parent) {
        if (templateConnection != null) {
            setFieldValue(parent, R.id.server_connect_host, templateConnection.getHost());
            setFieldValue(parent, R.id.server_connect_port, String.valueOf(templateConnection.getPort()));
            setFieldValue(parent, R.id.server_connect_nickname, templateConnection.getNickname());
            setFieldValue(parent, R.id.server_connect_realname, templateConnection.getRealname());
            setFieldValue(parent, R.id.server_connect_username, templateConnection.getUsername());
            setFieldValue(parent, R.id.server_connect_password, templateConnection.getPassword());
        }
    }

    @Override
    public IrkksomeConnection getConnection() {
        IrkksomeConnectionEB connection = connectionDAO.create();
        connection.setHost(getFieldValue(R.id.server_connect_host));
        connection.setPort(Integer.parseInt(getFieldValue(R.id.server_connect_port)));
        connection.setNickname(getFieldValue(R.id.server_connect_nickname));
        connection.setRealname(getFieldValue(R.id.server_connect_realname));
        connection.setUsername(getFieldValue(R.id.server_connect_username));
        connection.setPassword(getFieldValue(R.id.server_connect_password));
        if (connection.equals(templateConnection)) {
            return templateConnection;
        }
        else {
            return connection;
        }
    }
}
