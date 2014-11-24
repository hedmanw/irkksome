package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.ServerCallback;
import se.alkohest.irkksome.model.api.UnreadStack;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.ui.fragment.NoConnectionsFragment;
import se.alkohest.irkksome.ui.fragment.ServerInfoFragment;
import se.alkohest.irkksome.ui.fragment.channel.ChannelFragment;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class CallbackHandler implements ServerCallback {
    private static CallbackHandler instance;
    private final ConnectionListAdapter connectionListAdapter;
    private final StickyListHeadersListView connectionListView;
    private UserAdapter userAdapter;
    private final Activity context;
    private final FragmentManager fragmentManager;
    private UnreadStack unreadStack;

    public static CallbackHandler getInstance() {
        if (instance == null) {
            throw new IllegalStateException("No CallbackHandler has been initialized.");
        }
        return instance;
    }

    public static void setInstance(Activity context, UnreadStack unreadStack) {
        instance = new CallbackHandler(context, unreadStack);
    }

    private CallbackHandler(Activity context, UnreadStack unreadStack) {
        this.context = context;
        fragmentManager = context.getFragmentManager();
        connectionListAdapter = ConnectionListAdapter.getInstance();
        connectionListView = (StickyListHeadersListView) context.findViewById(R.id.left_drawer_list);
        this.unreadStack = unreadStack;
    }

    @Override
    public void showServerInfo(final IrcServer server, final String motd) {
        userAdapter = new UserSetAdapter(server.getKnownUsers());
        context.runOnUiThread(new Runnable() {
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            @Override
            public void run() {
                ServerInfoFragment serverInfoFragment = ServerInfoFragment.getInstance(server, motd);
                fragmentManager.popBackStack();
                fragmentTransaction.replace(R.id.fragment_container, serverInfoFragment);
                fragmentTransaction.commit();

                connectionListAdapter.notifyDataSetChanged(); // We don't need to reload the dataset unless it's a NEW connection, fix this!
                connectionListView.setItemChecked(0, true);
                connectionListView.setItemChecked(0, false);
                connectionListView.setSelection(0);
                ((ListView) context.findViewById(R.id.right_drawer_list)).setAdapter(userAdapter);
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
                fragmentTransaction.replace(R.id.fragment_container, new NoConnectionsFragment());
                fragmentTransaction.commit();
                connectionListAdapter.notifyDataSetChanged();
                ((ListView) context.findViewById(R.id.right_drawer_list)).setAdapter(null);
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
            ImageButton number = (ImageButton) context.findViewById(R.id.hilight_button);
            if (number != null) {
                if (unreadStack.getHilightCount() > 0) {
                    number.setVisibility(View.VISIBLE);
                    number.setBackground(context.getDrawable(R.drawable.highlightbadge_background_highlight));
                }
                else if (unreadStack.getMessageCount() > 0) {
                    number.setVisibility(View.VISIBLE);
                    number.setBackground(context.getDrawable(R.drawable.highlightbadge_background));
                }
                else {
                    number.setVisibility(View.GONE);
                }
            }

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

                connectionListAdapter.notifyDataSetChanged(); // We don't need to reload the dataset unless it's a NEW channel, fix this!

//                connectionListView.setItemChecked(position, true);
//                connectionListView.setSelection(position);
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
