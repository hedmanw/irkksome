package se.alkohest.irkksome.ui.interaction.server;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.ui.connection.NewConnectionActivity;
import se.alkohest.irkksome.ui.interaction.HilightContainedFragment;

public class ServerSplashFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_server_list, container, false);
        Button newConnections = (Button) inflatedView.findViewById(R.id.button_new_connections);
        newConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), NewConnectionActivity.class);
                intent.putExtra(NewConnectionActivity.REQUEST_CODE, NewConnectionActivity.MAKE_CONNECTION);
                startActivityForResult(intent, NewConnectionActivity.MAKE_CONNECTION);
            }
        });
        return inflatedView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Splash screen");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_leave_channel).setEnabled(false);
        menu.findItem(R.id.action_join_channel).setEnabled(false);
        menu.findItem(R.id.action_change_nick).setEnabled(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
