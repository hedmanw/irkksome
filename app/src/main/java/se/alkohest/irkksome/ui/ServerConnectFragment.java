package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.alkohest.irkksome.R;

public class ServerConnectFragment extends Fragment {
    public static final String ARG_HOSTNAME = "hostname";
    public static final String ARG_NICKNAME = "nickname";

    private OnFragmentInteractionListener listener;

    public static ServerConnectFragment newInstance() {
        ServerConnectFragment fragment = new ServerConnectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ServerConnectFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_join_channel).setEnabled(false);
        menu.findItem(R.id.action_leave_channel).setEnabled(false);
        menu.findItem(R.id.action_change_nick).setEnabled(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_server_connect, container, false);
        inflatedView.findViewById(R.id.server_connect_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectPressed();
            }
        });
        return inflatedView;
    }

    public void connectPressed() {
        View connectButton = getView().findViewById(R.id.server_connect_button);
        connectButton.setVisibility(View.GONE);
        View progressBar = getView().findViewById(R.id.server_connect_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        if (listener != null) {
            Bundle bundle = new Bundle();
            bundle.putString(ARG_HOSTNAME, getOptionValue(R.id.server_connect_host));
            bundle.putString(ARG_NICKNAME, getOptionValue(R.id.server_connect_nickname));
            listener.onFragmentInteraction(bundle);
        }
    }

    private String getOptionValue(@IdRes int resourceId) {
        return ((TextView)getView().findViewById(resourceId)).getText().toString();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Bundle bundle);
    }
}
