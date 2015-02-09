package se.alkohest.irkksome.ui.fragment.server;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.Server;
import se.alkohest.irkksome.ui.fragment.HilightContainedFragment;

public class ServerInfoFragment extends HilightContainedFragment {
    private static Server server;
    private static String motd;

    public static ServerInfoFragment getInstance(Server server) {
        final ServerInfoFragment serverInfoFragment = new ServerInfoFragment();
        ServerInfoFragment.server = server;
        ServerInfoFragment.motd = server.getMotd().trim();
        return serverInfoFragment;
    }

    public ServerInfoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(server.getBackingBean().getHost());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_leave_channel).setEnabled(false);
        menu.findItem(R.id.action_drop_server).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_server_info, container, false);
        if (server != null) {
            TextView hostname = (TextView) inflatedView.findViewById(R.id.server_info_hostname);
            TextView nickname = (TextView) inflatedView.findViewById(R.id.server_info_nickname);
            TextView motdView = (TextView) inflatedView.findViewById(R.id.server_motd);
            hostname.setText(server.getBackingBean().getHost());
            nickname.setText(server.getBackingBean().getSelf().getName());
            if (motd.length() > 20) {
                motdView.setText(motd.substring(0, 20) + "[...]");
            }
        }
        return inflatedView;
    }
}
