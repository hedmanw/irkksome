package se.alkohest.irkksome.ui.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ConnectionsArrayAdapter extends ArrayAdapter<ConnectionController.ConnectionItem> {
    private LayoutInflater inflater;

    public ConnectionsArrayAdapter(Context context) {
        super(context, 0, ConnectionController.ITEMS);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return ConnectionController.ConnectionTypeEnum.values().length;
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
