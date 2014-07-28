package se.alkohest.irkksome.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrcServer;

public class ServerInfoFragment extends Fragment {
    private static IrcServer ircServer;
    private static String motd;

    public static ServerInfoFragment getInstance(IrcServer ircServer, String motd) {
        final ServerInfoFragment serverInfoFragment = new ServerInfoFragment();
        ServerInfoFragment.ircServer = ircServer;
        ServerInfoFragment.motd = motd.trim();
        return serverInfoFragment;
    }

    public ServerInfoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(ircServer.getHost());
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
        if (ircServer != null) {
            TextView hostname = (TextView) inflatedView.findViewById(R.id.server_info_hostname);
            TextView nickname = (TextView) inflatedView.findViewById(R.id.server_info_nickname);
            TextView motdView = (TextView) inflatedView.findViewById(R.id.server_motd);
            hostname.setText(ircServer.getHost());
            nickname.setText(ircServer.getSelf().getName());
            if (motd.length() > 20) {
                motdView.setText(motd.substring(0, 20) + "[...]");
            }
        }
        return inflatedView;
    }
}
