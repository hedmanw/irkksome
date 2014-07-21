package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.ArrayAdapter;

import java.util.List;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.ServerCallback;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;

/**
 * Created by oed on 7/20/14.
 */
public class CallbackHandler implements ServerCallback {
    private final ConnectionListAdapter connectionListAdapter;
    private ArrayAdapter<IrcMessage> arrayAdapter;
    private final Activity context;
    private final FragmentManager fragmentManager;

    public CallbackHandler(Activity context) {
        this.context = context;
        fragmentManager = context.getFragmentManager();
        connectionListAdapter = ConnectionListAdapter.getInstance();
    }

    @Override
    public void serverRegistered(final IrcServer server) {
        context.runOnUiThread(new Runnable() {
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            @Override
            public void run() {
                context.setTitle(server.getUrl());
                ServerInfoFragment serverInfoFragment = ServerInfoFragment.getInstance(server);
                fragmentTransaction.replace(R.id.fragment_container, serverInfoFragment);
                fragmentTransaction.commit();

                connectionListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void serverDisconnected() {

    }

    @Override
    public void setActiveChannel(final IrcChannel channel) {
        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, channel.getMessages());
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                context.setTitle(channel.getName());

                ChannelFragment channelFragment = ChannelFragment.newInstance("arbitrary", "stuff");
                fragmentTransaction.replace(R.id.fragment_container, channelFragment);
                fragmentTransaction.commit();

                channelFragment.setArrayAdapter(arrayAdapter);

                connectionListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void channelJoinFailed() {

    }

    @Override
    public void userJoinedChannel(IrcChannel channel, IrcUser user) {

    }

    @Override
    public void messageReceived() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                arrayAdapter.notifyDataSetChanged();
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
