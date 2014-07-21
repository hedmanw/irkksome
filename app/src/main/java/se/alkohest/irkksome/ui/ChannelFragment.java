package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;

public class ChannelFragment extends Fragment {
    private static ArrayAdapter<IrcMessage> arrayAdapter;
    private static IrcChannel ircChannel;

    public static ChannelFragment newInstance(IrcChannel ircChannel) {
        ChannelFragment fragment = new ChannelFragment();
        ChannelFragment.ircChannel = ircChannel;
        return fragment;
    }
    public ChannelFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(ircChannel.getName());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ChannelFragment.arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, ircChannel.getMessages());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_channel, container, false);
        ListView lv = (ListView) inflatedView.findViewById(R.id.listView);
        lv.setAdapter(arrayAdapter);
        return inflatedView;
    }

    public static ArrayAdapter<IrcMessage> getAdapter() {
        return arrayAdapter;
    }
}
