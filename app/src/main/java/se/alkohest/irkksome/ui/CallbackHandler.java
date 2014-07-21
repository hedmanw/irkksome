package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.Toast;

import java.util.List;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.ServerCallback;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;

public class CallbackHandler implements ServerCallback {
    private final ConnectionListAdapter connectionListAdapter;
    private final Activity context;
    private final FragmentManager fragmentManager;

    public CallbackHandler(Activity context) {
        this.context = context;
        fragmentManager = context.getFragmentManager();
        connectionListAdapter = ConnectionListAdapter.getInstance();
    }

    @Override
    public void showServerInfo(final IrcServer server) {
        context.runOnUiThread(new Runnable() {
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            @Override
            public void run() {
                ServerInfoFragment serverInfoFragment = ServerInfoFragment.getInstance(server);
                fragmentTransaction.replace(R.id.fragment_container, serverInfoFragment);
                fragmentTransaction.commit();

                connectionListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void serverDisconnected() {
        // TODO - should go to another connected server if present
        // TODO - should not create a new serverConnectFragment
        context.runOnUiThread(new Runnable() {
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            @Override
            public void run() {
                ServerConnectFragment serverConnectFragment = ServerConnectFragment.newInstance();
                fragmentTransaction.replace(R.id.fragment_container, serverConnectFragment);
                fragmentTransaction.commit();

                connectionListAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void error(final String message) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        });
    }

    @Override
    public void setActiveChannel(final IrcChannel channel) {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                context.setTitle(channel.getName());

                ChannelFragment channelFragment = ChannelFragment.newInstance(channel);
                fragmentTransaction.replace(R.id.fragment_container, channelFragment);
                fragmentTransaction.commit();

                connectionListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void userJoinedChannel(IrcChannel channel, IrcUser user) {

    }

    @Override
    public void messageReceived() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChannelFragment.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void nickChanged(String oldNick, IrcUser user) {

    }

    @Override
    public void userLeftChannel(IrcChannel channel, IrcUser user) {

    }

    @Override
    public void userQuit(IrcUser user, List<IrcChannel> channels) {

    }
}
