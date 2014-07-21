package se.alkohest.irkksome.ui;

/**
 * Created by oed on 7/21/14.
 */
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrcUser;

public class UserAdapter extends BaseAdapter {
    private List<IrcUser> data;
    private Map<IrcUser, String> map;

    public UserAdapter(Map<IrcUser, String> map) {
        this.map = map;
        updateData();
    }

    private void updateData() {
        data = new ArrayList<>();
        for (IrcUser u : map.keySet()) {
            data.add(u);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        IrcUser key = data.get(position);
        Iterator<Map.Entry<IrcUser, String>> i = map.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<IrcUser, String> e = i.next();
            if (e.getKey().equals(key)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        updateData();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        } else {
            result = convertView;
        }

        IrcUser key = data.get(position);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(R.id.nick)).setText(key.getName());
        ((TextView) result.findViewById(R.id.flag)).setText(map.get(key));

        return result;
    }
}