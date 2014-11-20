package se.alkohest.irkksome.ui.fragment.connection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ConnectionsArrayAdapter extends ArrayAdapter<ConnectionItem> {
    private LayoutInflater inflater;

    public ConnectionsArrayAdapter(Context context, ConnectionController connectionController) {
        super(context, 0, connectionController.getDataset());
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(inflater, convertView);
    }
}
