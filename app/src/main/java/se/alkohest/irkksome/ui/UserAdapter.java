package se.alkohest.irkksome.ui;

import android.widget.BaseAdapter;

import java.util.List;

public abstract class UserAdapter extends BaseAdapter {
    protected List<String> list;

    @Override
    public int getCount() {
        return list.size();
    }
}
