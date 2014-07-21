package se.alkohest.irkksome.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrcMessage;

public class ChannelFragment extends Fragment {
    private ArrayAdapter<IrcMessage> arrayAdapter;

    public static ChannelFragment newInstance(ArrayAdapter<IrcMessage> arrayAdapter) {
        ChannelFragment fragment = new ChannelFragment();
        fragment.arrayAdapter = arrayAdapter;
        return fragment;
    }
    public ChannelFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_channel, container, false);
        ListView lv = (ListView) inflatedView.findViewById(R.id.listView);
        lv.setAdapter(arrayAdapter);
        return inflatedView;
    }

}
