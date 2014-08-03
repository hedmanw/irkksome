package se.alkohest.irkksome.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_regular_connection, container, false);
        inflatedView.findViewById(R.id.server_connect_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectPressed();
            }
        });
        if (templateConnection != null) {
            setFieldValue(inflatedView, R.id.server_connect_host, templateConnection.getHost());
            setFieldValue(inflatedView, R.id.server_connect_port, String.valueOf(templateConnection.getPort()));
            setFieldValue(inflatedView, R.id.server_connect_nickname, templateConnection.getNickname());
            setFieldValue(inflatedView, R.id.server_connect_realname, templateConnection.getRealname());
            setFieldValue(inflatedView, R.id.server_connect_username, templateConnection.getUsername());
            setFieldValue(inflatedView, R.id.server_connect_password, templateConnection.getPassword());
        }
        return inflatedView;
    }

    @Override
    public IrkksomeConnection getConnection() {
        IrkksomeConnection connection = connectionDAO.create();
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
