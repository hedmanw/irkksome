package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.ServerCallback;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;

/**
 * Created by oed on 7/20/14.
 */
public class CallbackHandler implements ServerCallback{

    private ArrayAdapter<IrcMessage> arrayAdapter;
    private Activity context;

    public CallbackHandler(Activity context) {
        this.context = context;
    }

    @Override
    public void serverConnected() {

    }

    @Override
    public void serverDisconnected() {

    }

    @Override
    public void channelJoined(IrcChannel channel) {
        arrayAdapter = new ArrayAdapter<IrcMessage>(
                context,
                android.R.layout.simple_list_item_1,
                channel.getMessages()
        );

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ListView) context.findViewById(R.id.listView)).setAdapter(arrayAdapter);
            }
        });
    }

    @Override
    public void channelJoinFailed() {

    }

    @Override
    public void userJoinedChannel(IrcChannel channel, IrcUser user) {

    }

    @Override
    public void messageRecived() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }
}
