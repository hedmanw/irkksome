package se.alkohest.irkksome.ui;

import android.widget.BaseAdapter;

import java.util.List;

import se.alkohest.irkksome.model.entity.IrcUser;

/**
 * Created by oed on 7/21/14.
 */
public abstract class UserAdapter extends BaseAdapter {
    protected List<IrcUser> list;

    @Override
    public int getCount() {
        return list.size();
    }
}
