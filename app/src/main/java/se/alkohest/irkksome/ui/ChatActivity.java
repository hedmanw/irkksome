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
    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        serverManager = new ServerManager();
        server = serverManager.addServer("irc.chalmers.it");
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
        IrcChannel channel = server.joinChannel("#irkksome");
        EditText editText = (EditText) findViewById(R.id.input_field);
        server.sendMessage(channel, editText.getText().toString());
    }
}
