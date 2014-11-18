package se.alkohest.irkksome.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.ui.NewConnectionActivity;

/**
 * Created by wilhelm 2014-11-18.
 */
public class NoConnectionsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_no_connections, container, false);
        Button newConnections = (Button) inflatedView.findViewById(R.id.button_new_connections);
        newConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), NewConnectionActivity.class), NewConnectionActivity.MAKE_CONNECTION);
            }
        });
        return inflatedView;
    }
}
