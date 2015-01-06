package se.alkohest.irkksome.ui.fragment.connection;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.List;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;
import se.alkohest.irkksome.util.DateFormatUtil;

public class LegacyConnectionsAdapter extends BaseAdapter implements UndoAdapter {
    private final List<IrkksomeConnectionEB> dataset;
    private final LayoutInflater inflater;

    public LegacyConnectionsAdapter(Context context, List<IrkksomeConnectionEB> connectionsList) {
        this.inflater = LayoutInflater.from(context);
        this.dataset = connectionsList;
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public IrkksomeConnectionEB getItem(int position) {
        return dataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.server_connect_list_item, parent, false);
        }

        TextView nickname = (TextView) convertView.findViewById(android.R.id.title);
        TextView hostname = (TextView) convertView.findViewById(android.R.id.text1);
        TextView lastAccessed = (TextView) convertView.findViewById(android.R.id.text2);
        final IrkksomeConnectionEB connection = getItem(position);
        nickname.setText(connection.getNickname());
        hostname.setText(connection.toString());
        lastAccessed.setText("Last synced " + DateFormatUtil.getTimeDay(connection.getLastUsed()));

        return convertView;
    }

    @NonNull
    @Override
    public View getUndoView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.undo_row, parent, false);
        }
        return view;
    }

    @NonNull
    @Override
    public View getUndoClickView(@NonNull View view) {
        return view.findViewById(R.id.undo_row_undobutton);
    }
}
