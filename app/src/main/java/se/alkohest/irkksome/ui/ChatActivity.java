package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.irc.Log;
import se.alkohest.irkksome.model.api.ServerManager;
import se.alkohest.irkksome.model.api.UnreadEntity;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.ui.fragment.NoConnectionsFragment;
import se.alkohest.irkksome.ui.fragment.channel.ChannelFragment;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ChatActivity extends Activity implements ChannelFragment.OnMessageSendListener {
    private static final Log LOG = Log.getInstance(ChatActivity.class);
    private static ServerManager serverManager = ServerManager.INSTANCE;
    private StickyListHeadersListView connectionsList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawers);
        ConnectionListAdapter.setInstance(this, serverManager.getServers());
        CallbackHandler.setInstance(this);
        HilightManager.setInstance(this, serverManager.getUnreadStack());
        connectionsList = (StickyListHeadersListView) findViewById(R.id.left_drawer_list);

        if (savedInstanceState == null) {
//            serverManager.loadPersisted(); Either load from DB, or make connections static. Can we ensure all connections are kept alive?
            if (serverManager.getServers().isEmpty()) { // No sessions are running, cold start => "Fresh startup"

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NoConnectionsFragment emptynessFragment = new NoConnectionsFragment();
                fragmentTransaction.add(R.id.fragment_container, emptynessFragment);
                fragmentTransaction.commit();

                startActivityForResult(new Intent(this, NewConnectionActivity.class), NewConnectionActivity.FRESH_STARTUP_CONNECTION);
            }
            else { // Back stack was emptied with sessions running, resume them (eller?)
                serverManager.getActiveServer().setListener(CallbackHandler.getInstance());
                serverManager.getActiveServer().showServer();
            }
        }
        else { // Device was tilted
            // This might require us to loop through all servers and set new CallbackHandlers
            if (serverManager.getActiveServer() != null) {
                serverManager.getActiveServer().setListener(CallbackHandler.getInstance());
                if (serverManager.getActiveServer().getActiveChannel() == null) {
                    serverManager.getActiveServer().showServer();
                }
                else {
                    serverManager.getActiveServer().setActiveChannel(serverManager.getActiveServer().getActiveChannel());
                }
            }
        }

        ChatActivityStatic.onCreate(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow_right, GravityCompat.END);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (serverManager.getActiveServer() != null && serverManager.getActiveServer().getActiveChannel() != null) {
                    findViewById(R.id.input_field).requestFocus();
                }
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        final View leftDrawer = findViewById(R.id.left_drawer);

        connectionsList.setEmptyView(findViewById(android.R.id.empty));
        final ConnectionListAdapter listAdapter = ConnectionListAdapter.getInstance();
        connectionsList.setAdapter(listAdapter);

        connectionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                drawerLayout.closeDrawer(leftDrawer);
//                final Server selectedServer = listAdapter.getHeader(position);
//                if (selectedServer != serverManager.getActiveServer()) {
//                    serverManager.setActiveServer(selectedServer);
//                }
                IrcChannel channel = listAdapter.getItem(position);
                serverManager.getUnreadStack().remove(channel, serverManager.getActiveServer().getBackingBean());
                serverManager.getActiveServer().setActiveChannel(channel);
            }
        });
        connectionsList.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView stickyListHeadersListView, View view, int position, long id, boolean currentlySticky) {
                drawerLayout.closeDrawer(leftDrawer);
//                final Server selectedServer = listAdapter.getGroup(groupPos);
//                if (selectedServer != serverManager.getActiveServer()) {
//                    serverManager.setActiveServer(selectedServer);
//                }
                serverManager.getActiveServer().showServer();
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
                serverManager.shutDownServer(serverManager.getActiveServer());
                break;
        }
        return true;
    }

    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.input_field);
        if (!editText.getText().toString().isEmpty()) {
            serverManager.getActiveServer().sendMessage(serverManager.getActiveServer().getActiveChannel(), editText.getText().toString());
            editText.getText().clear();
        }
    }

    public void startQuery(String nickname) {
        serverManager.getActiveServer().startQuery(nickname);
        drawerLayout.closeDrawer(findViewById(R.id.right_drawer));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NewConnectionActivity.FRESH_STARTUP_CONNECTION) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }
    }
}
