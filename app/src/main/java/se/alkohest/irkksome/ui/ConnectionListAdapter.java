package se.alkohest.irkksome.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.Server;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;

/**
 * Look into optimizing the getChild and getGroup calls.
 * They get called a lot, so they could benefit from some sort of caching.
 */
public class ConnectionListAdapter extends BaseExpandableListAdapter {
    private static ConnectionListAdapter INSTANCE;
    private Context context;
    private List<Server> servers;

    public static ConnectionListAdapter getInstance() {
        return INSTANCE;
    }

    public static void setInstance(Context context, List<Server> servers) {
        INSTANCE = new ConnectionListAdapter(context, servers);
    }

    private ConnectionListAdapter(Context context, List<Server> servers) {
        this.context = context;
        this.servers = servers;
        notifyDataSetChanged();
    }

    @Override
    public IrcChannel getChild(int groupPosition, int childPosition) {
        final IrcServer backingBean = getGroup(groupPosition).getBackingBean();
        return backingBean.getConnectedChannels().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final IrcChannel channel = getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.connection_list_channel, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.drawer_label_channel);

        txtListChild.setText(channel.getName());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        final IrcServer backingBean = getGroup(groupPosition).getBackingBean();
        return backingBean.getConnectedChannels().size();
    }

    @Override
    public Server getGroup(int groupPosition) {
        return servers.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return servers.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Server server = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.connection_list_server, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.drawer_label_server);
        lblListHeader.setText(server.getBackingBean().getHost().toUpperCase());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public int getPosition(IrcChannel channel) {
        int cummulativePosition = 0;
        for (Server server : servers) {
            cummulativePosition++;
            IrcServer bean = server.getBackingBean();
            final int indexOfChannel = bean.getConnectedChannels().indexOf(channel);
            if (indexOfChannel >= 0) {
                cummulativePosition += indexOfChannel;
                return cummulativePosition;
            }
            cummulativePosition += bean.getConnectedChannels().size();
        }
        return 0;
    }
}
