package se.alkohest.irkksome.ui;

/**
 * Created by oed on 7/21/14.
 */
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrcUser;

public class UserMapAdapter extends UserAdapter {
    private Map<IrcUser, String> map;

    public UserMapAdapter(Map<IrcUser, String> map) {
        this.map = map;
        updateList();
    }

    private void updateList() {
        list = new ArrayList<>();
        for (IrcUser u : map.keySet()) {
            list.add(u);
        }
    }

    @Override
    public Map.Entry<IrcUser, String> getItem(int position) {
        IrcUser key = list.get(position);
        for (Map.Entry<IrcUser, String> e : map.entrySet()) {
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
        updateList();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        } else {
            result = convertView;
        }

        IrcUser key = list.get(position);
        ((TextView) result.findViewById(R.id.flag)).setText(map.get(key));
        ((TextView) result.findViewById(R.id.nick)).setText(key.getName());

        return result;
    }
}