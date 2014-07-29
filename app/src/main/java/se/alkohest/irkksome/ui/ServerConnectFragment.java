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
import android.widget.CheckBox;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.dao.IrkksomeConnectionDAO;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;

public class ServerConnectFragment extends Fragment {
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
        getActivity().setTitle(R.string.title_connect_to_server);
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
            IrkksomeConnection data = new IrkksomeConnectionDAO().create();
            data.setHost(getOptionValue(R.id.server_connect_host));
            data.setNickname(getOptionValue(R.id.server_connect_nickname));
            data.setRealname(getOptionValue(R.id.server_connect_realname));
            data.setUsername(getOptionValue(R.id.server_connect_username));
            data.setPassword(getOptionValue(R.id.server_connect_password));
            data.setPort(Integer.parseInt(getOptionValue(R.id.server_connect_port)));
            data.setUseSSH(((CheckBox) getView().findViewById(R.id.server_connect_use_ssh)).isChecked());
            data.setSshHost(getOptionValue(R.id.server_connect_sshHost));
            data.setSshUser(getOptionValue(R.id.server_connect_sshUser));
            data.setSshPass(getOptionValue(R.id.server_connect_sshPass));
            data.setUseSSL(false);
            listener.onFragmentInteraction(data);
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
        public void onFragmentInteraction(IrkksomeConnection data);
    }
}
