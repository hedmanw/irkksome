package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.ExpandableListView;
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

public class CallbackHandler implements ServerCallback {
    private final ConnectionListAdapter connectionListAdapter;
    private UserAdapter userAdapter;
    private final Activity context;
    private final FragmentManager fragmentManager;
    private UnreadStack unreadStack;

    public CallbackHandler(Activity context, UnreadStack unreadStack) {
        this.context = context;
        fragmentManager = context.getFragmentManager();
        connectionListAdapter = ConnectionListAdapter.getInstance();
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
                fragmentTransaction.replace(R.id.fragment_container, serverInfoFragment);
                fragmentTransaction.commit();

                connectionListAdapter.notifyDataSetChanged();
                ((ListView) context.findViewById(R.id.right_drawer_list)).setAdapter(userAdapter);
                // TODO: This is ugly
                ((ExpandableListView) context.findViewById(R.id.left_drawer_list)).expandGroup(0);
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

                connectionListAdapter.notifyDataSetChanged();
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
                ChannelFragment.getAdapter().notifyDataSetChanged();
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
