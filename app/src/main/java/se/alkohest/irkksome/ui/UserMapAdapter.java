package se.alkohest.irkksome.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import se.alkohest.irkksome.R;

// TODO: Migrate usage of this to UserSetAdapter by including channel status flags in some wayn
public class UserMapAdapter extends UserAdapter {
    private Map<String, String> map;

    public UserMapAdapter(Map<String, String> map) {
        this.map = map;
        updateList();
    }

    private void updateList() {
        list = new ArrayList<>();
        for (String u : map.keySet()) {
            list.add(u);
        }
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        String key = list.get(position);
        for (Map.Entry<String, String> e : map.entrySet()) {
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

        String key = list.get(position);
        ((TextView) result.findViewById(R.id.flag)).setText(map.get(key));
        ((TextView) result.findViewById(R.id.nick)).setText(key);

        return result;
    }
}