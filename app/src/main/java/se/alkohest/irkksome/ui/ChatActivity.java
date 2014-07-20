package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.irc.Log;
import se.alkohest.irkksome.model.api.Server;
import se.alkohest.irkksome.model.api.ServerManager;

public class ChatActivity extends Activity implements ServerConnectFragment.OnFragmentInteractionListener {
    private static final Log LOG = Log.getInstance(ChatActivity.class);
    private ServerManager serverManager;
    private Server activeServer;
    private ExpandableListView connectionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawers);
        serverManager = new ServerManager();
        ConnectionListAdapter.setInstance(this, serverManager.getServers());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ServerConnectFragment connectFragment = ServerConnectFragment.newInstance();
        fragmentTransaction.add(R.id.fragment_container, connectFragment);
        fragmentTransaction.commit();

        connectionsList = (ExpandableListView) findViewById(R.id.left_drawer_list);
        BaseExpandableListAdapter listAdapter = ConnectionListAdapter.getInstance();
        connectionsList.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_join_channel:
                showJoinChannel();
                break;
            case R.id.action_leave_channel:
                activeServer.leaveChannel(activeServer.getActiveChannel());
                ConnectionListAdapter.getInstance().notifyDataSetChanged();
                break;
            case R.id.action_change_nick:
                showNickChangeDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNickChangeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText popup = new EditText(this);
        builder.setTitle("Change nick on " + activeServer.getBackingBean().getUrl());
        builder.setView(popup);
        builder.setPositiveButton(R.string.change_nick, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activeServer.changeNick(popup.getText().toString());
            }
        });
        builder.create().show();
    }

    private void showJoinChannel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText popup = new EditText(this);
        builder.setTitle("Join channel on " + activeServer.getBackingBean().getUrl());
        builder.setView(popup);
        builder.setPositiveButton(R.string.join_channel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activeServer.joinChannel(popup.getText().toString());
            }
        });
        builder.create().show();
    }

    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.input_field);
        activeServer.sendMessage(activeServer.getActiveChannel(), editText.getText().toString());
        editText.getText().clear();
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {
        String hostName = bundle.getString(ServerConnectFragment.ARG_HOSTNAME);
        String nickname = bundle.getString(ServerConnectFragment.ARG_NICKNAME);
        activeServer = serverManager.addServer(hostName, nickname);
        activeServer.setListener(new CallbackHandler(this));
        ConnectionListAdapter.getInstance().notifyDataSetChanged();
        connectionsList.expandGroup(serverManager.getServers().indexOf(activeServer));
    }
}
