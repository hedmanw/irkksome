package se.alkohest.irkksome.ui.fragment.channel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class ChannelArrayAdapter extends ArrayAdapter<ChannelChatItem> {
    private LayoutInflater inflater;

    public ChannelArrayAdapter(Context context, List<ChannelChatItem> listItems) {
        super(context, 0, listItems);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return ChannelChatItem.ChatItemTypeEnum.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType().ordinal();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(inflater, convertView);
    }
}
