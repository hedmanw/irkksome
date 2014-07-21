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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.db.SQLitePersistenceContext;
import se.alkohest.irkksome.irc.Log;
import se.alkohest.irkksome.model.api.Server;
import se.alkohest.irkksome.model.api.ServerManager;
import se.alkohest.irkksome.orm.GenericDAO;

public class ChatActivity extends Activity implements ServerConnectFragment.OnFragmentInteractionListener {
    private static final Log LOG = Log.getInstance(ChatActivity.class);
    private static ServerManager serverManager = ServerManager.INSTANCE;
    private ExpandableListView connectionsList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GenericDAO.persistenceContext = new SQLitePersistenceContext(this);
        setContentView(R.layout.activity_main_drawers);
        ConnectionListAdapter.setInstance(this, serverManager.getServers());

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ServerConnectFragment connectFragment = ServerConnectFragment.newInstance();
            fragmentTransaction.add(R.id.fragment_container, connectFragment);
            fragmentTransaction.commit();
        }
        else {
            // This might require us to loop through all servers and set new CallbackHandlers
            serverManager.getActiveServer().setListener(new CallbackHandler(this));
        }

        ChatActivityStatic.onCreate(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        final View leftDrawer = findViewById(R.id.left_drawer);

        connectionsList = (ExpandableListView) findViewById(R.id.left_drawer_list);
        connectionsList.setEmptyView(findViewById(android.R.id.empty));
        final ConnectionListAdapter listAdapter = ConnectionListAdapter.getInstance();
        connectionsList.setAdapter(listAdapter);
        connectionsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPos, int childPos, long id) {
                drawerLayout.closeDrawer(leftDrawer);
                final Server selectedServer = listAdapter.getGroup(groupPos);
                if (selectedServer != serverManager.getActiveServer()) {
                    serverManager.setActiveServer(selectedServer);
                }
                serverManager.getActiveServer().setActiveChannel(listAdapter.getChild(groupPos, childPos));
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
        }
        return true;
    }

    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.input_field);
        serverManager.getActiveServer().sendMessage(serverManager.getActiveServer().getActiveChannel(), editText.getText().toString());
        editText.getText().clear();
    }

    public void startQuery(View view) {
        String nick = (String) ((TextView) view.findViewById(R.id.nick)).getText();
        serverManager.getActiveServer().startQuery(nick);
        drawerLayout.closeDrawer(findViewById(R.id.right_drawer));
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {
        String hostName = bundle.getString(ServerConnectFragment.ARG_HOSTNAME);
        String nickname = bundle.getString(ServerConnectFragment.ARG_NICKNAME);
        serverManager.setActiveServer(serverManager.addServer(hostName, nickname));
        serverManager.getActiveServer().setListener(new CallbackHandler(this));
        ConnectionListAdapter.getInstance().notifyDataSetChanged();
        connectionsList.expandGroup(serverManager.getServers().indexOf(serverManager.getActiveServer()));
    }
}
