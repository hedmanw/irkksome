package se.alkohest.irkksome.ui.interaction.server;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.Server;
import se.alkohest.irkksome.model.api.ServerManager;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;

/**
 * Created by wilhelm 2015-01-06.
 */
public class ConnectedServersAdapter extends BaseAdapter {
    private final List<Server> servers;
    private LayoutInflater inflater;

    public ConnectedServersAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.servers = ServerManager.INSTANCE.getServers();
    }

    @Override
    public int getCount() {
        return servers.size();
    }

    @Override
    public Server getItem(int position) {
        return servers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return servers.get(position).getBackingBean().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.server_connect_list_item, parent, false);
        }

        IrkksomeConnection connectionData = getItem(position).getConnectionData();
        TextView nickname = (TextView) convertView.findViewById(android.R.id.title);
        nickname.setText(connectionData.getNickname());
        TextView serverName = (TextView) convertView.findViewById(android.R.id.text1);
        serverName.setText(getItem(position).getBackingBean().getServerName() + " (" + connectionData.toString() + ")");
        TextView infoText = (TextView) convertView.findViewById(android.R.id.text2);
        final int channels = getItem(position).getBackingBean().getConnectedChannels().size();
        String channelLabel = " connected channels";
        if (channels == 1) {
            channelLabel = " connected channel";
        }
        infoText.setText(channels + channelLabel);
        return convertView;
    }
}
