package se.alkohest.irkksome.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;

public class RegularConnectionFragment extends AbstractConnectionFragment {

    public static AbstractConnectionFragment newInstance() {
        return new RegularConnectionFragment();
    }

    public static AbstractConnectionFragment newInstance(IrkksomeConnection irkksomeConnection) {
        AbstractConnectionFragment fragment = newInstance();
        Bundle args = new Bundle();
        args.putLong(CONNECTION_ARGUMENT, irkksomeConnection.getId());
        fragment.setArguments(args);
        return fragment;
    }

    public RegularConnectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_regular_connection, container, false);
    }

}
