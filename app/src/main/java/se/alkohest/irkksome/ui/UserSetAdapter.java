package se.alkohest.irkksome.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import se.alkohest.irkksome.R;

public class UserSetAdapter extends UserAdapter {
    private Set<String> set;

    public UserSetAdapter(Set<String> set) {
        this.set = set;
        updateList();
    }

    private void updateList() {
        list = new ArrayList<>();
        for (String u : set) {
            list.add(u);
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
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

        ((TextView) result.findViewById(R.id.nick)).setText(list.get(position));
        return result;
    }
}
