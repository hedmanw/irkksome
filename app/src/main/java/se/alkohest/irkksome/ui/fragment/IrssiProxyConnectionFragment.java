package se.alkohest.irkksome.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_irssi_proxy_connection, container, false);
        inflatedView.findViewById(R.id.server_connect_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectPressed();
            }
        });
        return inflatedView;
    }

    @Override
    public IrkksomeConnection getConnection() {
        IrkksomeConnection connection = connectionDAO.create();
        connection.setHost(getFieldValue(R.id.server_connect_host));
        connection.setPort(Integer.parseInt(getFieldValue(R.id.server_connect_port)));
        connection.setNickname(""); // Because why the fuck not?
        connection.setUsername(getFieldValue(R.id.server_connect_username));
        connection.setPassword(getFieldValue(R.id.server_connect_password));
        connection.setSshHost(getFieldValue(R.id.server_connect_sshHost));
        connection.setSshUser(getFieldValue(R.id.server_connect_sshUser));
        connection.setSshPass(getFieldValue(R.id.server_connect_sshPass));
        connection.setUseSSH(true);
        return connection;
    }
}
