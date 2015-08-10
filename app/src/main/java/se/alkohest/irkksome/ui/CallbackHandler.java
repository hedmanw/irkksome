package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.Server;
import se.alkohest.irkksome.model.api.ServerCallback;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.ui.interaction.channel.ChannelPresenter;
import se.alkohest.irkksome.ui.interaction.server.ServerInfoFragment;
import se.alkohest.irkksome.ui.interaction.server.ServerListFragment;
import se.alkohest.irkksome.ui.interaction.channel.ChannelFragment;

// TODO: Change the navigation between fragments to a state machine that encapsulates the necessary logic
public class CallbackHandler implements ServerCallback {
    private static CallbackHandler instance;
    private final ListView connectionListView;
    private UserAdapter userAdapter;
    private final Activity context;
    private final FragmentManager fragmentManager;
    private ChannelPresenter channelPresenter;

    public static CallbackHandler getInstance() {
        if (instance == null) {
            throw new IllegalStateException("No CallbackHandler has been initialized.");
        }
        return instance;
    }

    public static void setInstance(Activity context) {
        instance = new CallbackHandler(context);
    }

    private CallbackHandler(Activity context) {
        this.context = context;
        fragmentManager = context.getFragmentManager();
        connectionListView = (ListView) context.findViewById(R.id.left_drawer_list);

    }

    @Override
    public void showServerInfo(final Server server) {
        final IrcServer ircServer = server.getBackingBean();
        userAdapter = new UserSetAdapter(ircServer.getKnownUsers());
        ChannelsAdapter.setInstance(context, ircServer);

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                context.findViewById(R.id.drawer_label_server).setVisibility(View.VISIBLE);
                connectionListView.setAdapter(ChannelsAdapter.getInstance());
                ServerInfoFragment serverInfoFragment = ServerInfoFragment.getInstance(server);
//                fragmentManager.popBackStack(); // TODO: why do we do this? this causes unexplainable crashes impossible to recreate
                fragmentTransaction.replace(R.id.fragment_container, serverInfoFragment);
                fragmentTransaction.commit();
                TextView serverName = (TextView) context.findViewById(R.id.drawer_label_server);
                serverName.setText(ircServer.getServerName());
                connectionListView.setItemChecked(connectionListView.getCheckedItemPosition(), false);
                connectionListView.setSelection(0);
                ((ListView) context.findViewById(R.id.right_drawer_list)).setAdapter(userAdapter);
            }
        });
    }

    @Override
    public void serverDisconnected() {
        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO - should go to another connected server if present
                if (false) {
                    // Show some other stuff
                } else {
                    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    context.findViewById(R.id.drawer_label_server).setVisibility(View.GONE);
                    fragmentTransaction.replace(R.id.fragment_container, new ServerListFragment());
                    fragmentTransaction.commit();
                    ChannelsAdapter.getInstance().notifyDataSetChanged();
                    ((ListView) context.findViewById(R.id.right_drawer_list)).setAdapter(null);
                    ((ListView) context.findViewById(R.id.left_drawer_list)).setAdapter(null);
                }
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
    public void updateHilights() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HilightHandler.getInstance().updateHilightButton();
            }
        });
    }

    @Override
    public void setActiveChannel(final IrcChannel channel) {
        userAdapter = new UserMapAdapter(channel.getUsers());

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                context.setTitle(channel.getName());
                if (fragmentManager.findFragmentByTag(ChannelFragment.FRAGMENT_TAG) == null) {
                    final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, ChannelFragment.newInstance(channel), ChannelFragment.FRAGMENT_TAG);
                    fragmentTransaction.commit();
                }
                else {
                    channelPresenter.changeChannel(channel);
                    HilightHandler.getInstance().updateHilightButton();
                    channelPresenter.smooothScrollToBottom();
                }

                ((ListView) context.findViewById(R.id.right_drawer_list)).setAdapter(userAdapter);

                ChannelsAdapter.getInstance().notifyDataSetChanged(); // We don't need to reload the dataset unless it's a NEW channel, fix this!
                int position = ChannelsAdapter.getInstance().getPosition(channel);
                connectionListView.setItemChecked(position, true);
                connectionListView.setSelection(position);
            }
        });
    }

    @Override
    public void messageReceived(final IrcMessage message) { // TODO: get rid of this parameter
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                channelPresenter.messageReceived();
            }
        });
    }

    @Override
    public void updateUserList() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userAdapter.notifyDataSetChanged();
            }
        });
    }

    public void registerChannelPresenter(ChannelPresenter presenter) {
        this.channelPresenter = presenter;
    }
}
