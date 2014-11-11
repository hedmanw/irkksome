package se.alkohest.irkksome.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.Server;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class ConnectionListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private static ConnectionListAdapter INSTANCE;
    private LayoutInflater inflater;
    private List<Server> servers;

    public static ConnectionListAdapter getInstance() {
        return INSTANCE;
    }

    public static void setInstance(Context context, List<Server> servers) {
        INSTANCE = new ConnectionListAdapter(context, servers);
    }

    private ConnectionListAdapter(Context context, List<Server> servers) {
        this.inflater = LayoutInflater.from(context);
        this.servers = servers;
        notifyDataSetChanged();
    }

    @Override
    public long getHeaderId(int position) {
        return servers.get(0).getBackingBean().getId();
    }

    @Override
    public int getCount() {
        return servers.isEmpty() ? 0 : servers.get(0).getBackingBean().getConnectedChannels().size();
    }

    @Override
    public IrcChannel getItem(int position) {
        return servers.get(0).getBackingBean().getConnectedChannels().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.connection_list_server, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.drawer_label_server);
        textView.setText(servers.get(0).getBackingBean().getHost());
        convertView.setBackgroundColor(0xfff3f3f3);
        return convertView;
    }
}
