package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.ServerCallback;
import se.alkohest.irkksome.model.api.UnreadStack;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.ui.fragment.ConnectionsListFragment;

public class CallbackHandler implements ServerCallback {
    private final ConnectionListAdapter connectionListAdapter;
    private final ListView connectionListView;
    private UserAdapter userAdapter;
    private final Activity context;
    private final FragmentManager fragmentManager;
    private UnreadStack unreadStack;

    public CallbackHandler(Activity context, UnreadStack unreadStack) {
        this.context = context;
        fragmentManager = context.getFragmentManager();
        connectionListAdapter = ConnectionListAdapter.getInstance();
        connectionListView = (ListView) context.findViewById(R.id.left_drawer_list);
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
                ConnectionsListFragment connectionsListFragment = ConnectionsListFragment.newInstance();
                fragmentTransaction.replace(R.id.fragment_container, connectionsListFragment);
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
                TextView number = (TextView) context.findViewById(R.id.actionbar_notifcation_textview);
                if (unreadStack.getHilightCount() > 0) {
                    number.setText(unreadStack.getHilightCount() + "");
                    ((LinearLayout) number.getParent()).setBackground(context.getResources().getDrawable(R.drawable.highlightbadge_background_highlight));
                } else if (unreadStack.getMessageCount() > 0) {
                    number.setText(unreadStack.getMessageCount() + "");
                    ((LinearLayout) number.getParent()).setBackground(context.getResources().getDrawable(R.drawable.highlightbadge_background));
                } else {
                    number.setText("0");
                    ((LinearLayout) number.getParent()).setBackground(context.getResources().getDrawable(R.drawable.highlightbadge_background_disabled));
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
                final int position = connectionListAdapter.getPosition(channel);
                connectionListView.setItemChecked(position, true);
                connectionListView.setSelection(position);
            }
        });
    }

    @Override
    public void userJoinedChannel(IrcChannel channel, IrcUser user) {
        updateUserList();
    }

    @Override
    public void messageReceived() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ChannelFragment.getAdapter() != null) {
                    ChannelFragment.getAdapter().notifyDataSetChanged();
                    ChannelFragment.scrollToBottom();
                }
            }
        });
    }

    @Override
    public void nickChanged(String oldNick, IrcUser user) {
        updateUserList();
    }

    @Override
    public void userLeftChannel(IrcChannel channel, IrcUser user) {
    }

    @Override
    public void userQuit(IrcUser user, List<IrcChannel> channels) {
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
