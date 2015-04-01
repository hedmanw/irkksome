package se.alkohest.irkksome.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;

public class ChannelsAdapter extends BaseAdapter {
    private static ChannelsAdapter INSTANCE;
    private LayoutInflater inflater;
    private IrcServer server;

    public static ChannelsAdapter getInstance() {
        return INSTANCE;
    }

    public static void setInstance(Context context, IrcServer server) {
        INSTANCE = new ChannelsAdapter(context, server);
    }

    private ChannelsAdapter(Context context, IrcServer server) {
        this.inflater = LayoutInflater.from(context);
        this.server = server;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (server == null) {
            return 0;
        }
        else {
            return server.getConnectedChannels().size();
        }
    }

    @Override
    public IrcChannel getItem(int position) {
        return server.getConnectedChannels().get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.connection_list_channel, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.drawer_label_channel);
        textView.setText(getItem(position).getName());

        return convertView;
    }

    public int getPosition(IrcChannel channel) {
        return server.getConnectedChannels().indexOf(channel);
    }
}
