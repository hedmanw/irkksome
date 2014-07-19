package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.Server;
import se.alkohest.irkksome.model.api.ServerManager;

public class ChatActivity extends Activity {
    private ServerManager serverManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        serverManager = new ServerManager();
        Server server = serverManager.addServer("irc.chalmers.it");
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
}
