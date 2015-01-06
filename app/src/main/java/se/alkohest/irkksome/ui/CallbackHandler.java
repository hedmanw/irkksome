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
import se.alkohest.irkksome.model.api.ServerCallback;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.ui.fragment.server.ServerInfoFragment;
import se.alkohest.irkksome.ui.fragment.server.ServerListFragment;
import se.alkohest.irkksome.ui.fragment.channel.ChannelFragment;

public class CallbackHandler implements ServerCallback {
    private static CallbackHandler instance;
    private final ListView connectionListView;
    private UserAdapter userAdapter;
    private final Activity context;
    private final FragmentManager fragmentManager;

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
    public void showServerInfo(final IrcServer server, final String motd) {
        userAdapter = new UserSetAdapter(server.getKnownUsers());
        ChannelsAdapter.setInstance(context, server);

        context.runOnUiThread(new Runnable() {
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            @Override
            public void run() {
                context.findViewById(R.id.drawer_label_server).setVisibility(View.VISIBLE);
                connectionListView.setAdapter(ChannelsAdapter.getInstance());
                ServerInfoFragment serverInfoFragment = ServerInfoFragment.getInstance(server, motd);
                fragmentManager.popBackStack();
                fragmentTransaction.replace(R.id.fragment_container, serverInfoFragment);
                fragmentTransaction.commit();
                TextView serverName = (TextView) context.findViewById(R.id.drawer_label_server);
                serverName.setText(server.getHost());
                connectionListView.setItemChecked(connectionListView.getCheckedItemPosition(), false);
                connectionListView.setSelection(0);
                ((ListView) context.findViewById(R.id.right_drawer_list)).setAdapter(userAdapter);
            }
        });
    }

    @Override
    public void serverDisconnected() {
        context.runOnUiThread(new Runnable() {
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            @Override
            public void run() {
                // TODO - should go to another connected server if present
                if (false) {
                    // Show some other stuff
                } else {
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
                HilightManager.getInstance().updateHilightButton();
            }
        });
    }

    @Override
    public void setActiveChannel(final IrcChannel channel) {
        userAdapter = new UserMapAdapter(channel.getUsers());
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                context.setTitle(channel.getName());

                ChannelFragment channelFragment = ChannelFragment.newInstance(channel);
                fragmentTransaction.replace(R.id.fragment_container, channelFragment);
                fragmentTransaction.commit();

                ((ListView) context.findViewById(R.id.right_drawer_list)).setAdapter(userAdapter);

                ChannelsAdapter.getInstance().notifyDataSetChanged(); // We don't need to reload the dataset unless it's a NEW channel, fix this!
                int position = ChannelsAdapter.getInstance().getPosition(channel);
                connectionListView.setItemChecked(position, true);
                connectionListView.setSelection(position);
            }
        });
    }

    @Override
    public void messageReceived(final IrcMessage message) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChannelFragment.receiveMessage(message);
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
}
