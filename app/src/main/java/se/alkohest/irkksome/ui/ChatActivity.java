package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.irc.Log;
import se.alkohest.irkksome.model.api.Server;
import se.alkohest.irkksome.model.api.ServerManager;
import se.alkohest.irkksome.model.api.UnreadEntity;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;
import se.alkohest.irkksome.ui.fragment.connection.AbstractConnectionFragment;
import se.alkohest.irkksome.ui.fragment.connection.ConnectionItem;
import se.alkohest.irkksome.ui.fragment.connection.ConnectionsListFragment;

public class ChatActivity extends Activity implements ConnectionsListFragment.OnConnectionSelectedListener, AbstractConnectionFragment.OnConnectPressedListener {
    private static final Log LOG = Log.getInstance(ChatActivity.class);
    private static ServerManager serverManager = ServerManager.INSTANCE;
    private ExpandableListView connectionsList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawers);
        ConnectionListAdapter.setInstance(this, serverManager.getServers());
        connectionsList = (ExpandableListView) findViewById(R.id.left_drawer_list);

        if (savedInstanceState == null) {
//            serverManager.loadPersisted(); Either load from DB, or make connections static. Can we ensure all connections are kept alive?
            if (serverManager.getServers().isEmpty()) { // No sessions are running, cold start
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ConnectionsListFragment connectFragment = ConnectionsListFragment.newInstance();
                fragmentTransaction.add(R.id.fragment_container, connectFragment, ConnectionsListFragment.TAG);
                fragmentTransaction.commit();
            }
            else { // Back stack was emptied with sessions running, resume them (eller?)
                serverManager.getActiveServer().setListener(new CallbackHandler(this, serverManager.getUnreadStack()));
                serverManager.getActiveServer().showServer();
            }
        }
        else { // Device was tilted
            // This might require us to loop through all servers and set new CallbackHandlers
            if (serverManager.getActiveServer() != null) {
                serverManager.getActiveServer().setListener(new CallbackHandler(this, serverManager.getUnreadStack()));
            }
        }

        ChatActivityStatic.onCreate(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        final View leftDrawer = findViewById(R.id.left_drawer);

        connectionsList.setEmptyView(findViewById(android.R.id.empty));
        final ConnectionListAdapter listAdapter = ConnectionListAdapter.getInstance();
        connectionsList.setAdapter(listAdapter);
        for (int i = 0, serversSize = serverManager.getServers().size(); i < serversSize; i++) {
            connectionsList.expandGroup(i);
        }
        connectionsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPos, int childPos, long id) {
                drawerLayout.closeDrawer(leftDrawer);
                final Server selectedServer = listAdapter.getGroup(groupPos);
                if (selectedServer != serverManager.getActiveServer()) {
                    serverManager.setActiveServer(selectedServer);
                }
                IrcChannel channel = listAdapter.getChild(groupPos, childPos);
                serverManager.getUnreadStack().remove(channel, serverManager.getActiveServer().getBackingBean());
                serverManager.getActiveServer().setActiveChannel(channel);
                return true;
            }
        });
        connectionsList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPos, long id) {
                drawerLayout.closeDrawer(leftDrawer);
                final Server selectedServer = listAdapter.getGroup(groupPos);
                if (selectedServer != serverManager.getActiveServer()) {
                    serverManager.setActiveServer(selectedServer);
                }
                serverManager.getActiveServer().showServer();
                return true;
            }
        });

        final ListView userList = (ListView) findViewById(R.id.right_drawer_list);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String nick = ((TextView) view.findViewById(R.id.nick)).getText().toString();
                startQuery(nick);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_join_channel:
                ChatActivityStatic.showJoinChannelDialog(this, serverManager.getActiveServer());
                break;
            case R.id.action_leave_channel:
                serverManager.getActiveServer().leaveChannel(serverManager.getActiveServer().getActiveChannel());
                break;
            case R.id.action_change_nick:
                ChatActivityStatic.showNickChangeDialog(this, serverManager.getActiveServer());
                break;
            case R.id.action_drop_server:
                serverManager.getActiveServer().disconnect();
                break;
        }
        return true;
    }

    public void showHilight(View view) {
        if (serverManager.getUnreadStack().hasUnread()) {
            UnreadEntity entity = serverManager.getUnreadStack().pop();
            if (serverManager.getActiveServer() != entity.getServer()) {
                serverManager.setActiveServer(entity.getServer());
            }
            serverManager.getActiveServer().setActiveChannel(entity.getChannel());
        }
    }

    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.input_field);
        serverManager.getActiveServer().sendMessage(serverManager.getActiveServer().getActiveChannel(), editText.getText().toString());
        editText.getText().clear();
    }

    public void startQuery(String nickname) {
        serverManager.getActiveServer().startQuery(nickname);
        drawerLayout.closeDrawer(findViewById(R.id.right_drawer));
    }

    @Override
    public void onConnectionSelected(ConnectionItem connectionItem) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AbstractConnectionFragment connectFragment = connectionItem.getConnectionFragment();
        fragmentTransaction.replace(R.id.fragment_container, connectFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onConnectPressed(IrkksomeConnectionEB irkksomeConnection) {
        serverManager.setActiveServer(serverManager.addServer(irkksomeConnection));
        serverManager.getActiveServer().setListener(new CallbackHandler(this, serverManager.getUnreadStack()));
        ConnectionListAdapter.getInstance().notifyDataSetChanged();
        connectionsList.expandGroup(serverManager.getServers().indexOf(serverManager.getActiveServer()));
    }
}
