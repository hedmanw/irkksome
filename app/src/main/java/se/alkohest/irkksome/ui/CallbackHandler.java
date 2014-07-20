package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.ArrayAdapter;

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
    public void channelJoined(final IrcChannel channel) {
        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, channel.getMessages());
        final FragmentManager fragmentManager = context.getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                context.setTitle(channel.getName());

                ChannelFragment channelFragment = ChannelFragment.newInstance("arbitrary", "stuff");
                fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.fragment_container));
                fragmentTransaction.add(R.id.fragment_container, channelFragment);
                fragmentTransaction.commit();

                channelFragment.setArrayAdapter(arrayAdapter);
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
    public void messageReceived() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }
}
