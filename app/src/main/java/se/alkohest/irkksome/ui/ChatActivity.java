package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.Server;
import se.alkohest.irkksome.model.api.ServerManager;
import se.alkohest.irkksome.model.entity.IrcChannel;

public class ChatActivity extends Activity {
    private ServerManager serverManager;
    private Server activeServer;
    private IrcChannel activeChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        serverManager = new ServerManager();
        activeServer = serverManager.addServer("irc.chalmers.it");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view) {
        IrcChannel channel = activeServer.joinChannel("#saltholmen2.0");
        EditText editText = (EditText) findViewById(R.id.input_field);
        activeServer.sendMessage(channel, editText.getText().toString());
        editText.getText().clear();
    }
}
