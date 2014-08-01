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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.dao.IrkksomeConnectionDAO;
import se.alkohest.irkksome.model.api.local.IrkksomeConnectionDAOLocal;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;

public class ServerConnectFragment extends Fragment {
    private OnFragmentInteractionListener listener;
    private ArrayAdapter<IrkksomeConnection> connectionsAdapter;
    private IrkksomeConnectionDAOLocal connectionDAO = new IrkksomeConnectionDAO();

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
        connectionsAdapter = new ConnectionsArrayAdapter(connectionDAO.getAll());
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
        ListView previouslyConnectedList = (ListView) inflatedView.findViewById(R.id.server_connect_list);
        previouslyConnectedList.setAdapter(connectionsAdapter);
        CheckBox sshSettings = (CheckBox) inflatedView.findViewById(R.id.server_connect_use_ssh);
        sshSettings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    inflatedView.findViewById(R.id.server_connect_ssh_settings).setVisibility(View.VISIBLE);
                }
                else {
                    inflatedView.findViewById(R.id.server_connect_ssh_settings).setVisibility(View.GONE);
                }
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
            IrkksomeConnection data = connectionDAO.create();
            data.setHost(getOptionValue(R.id.server_connect_host));
            data.setNickname(getOptionValue(R.id.server_connect_nickname));
            data.setRealname(getOptionValue(R.id.server_connect_realname));
            data.setUsername(getOptionValue(R.id.server_connect_username));
            data.setPassword(getOptionValue(R.id.server_connect_password));
            final String portNumber = getOptionValue(R.id.server_connect_port);
            if (!portNumber.isEmpty()) {
                data.setPort(Integer.parseInt(portNumber));
            } else {
                data.setPort(0);
            }
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

    private class ConnectionsArrayAdapter extends ArrayAdapter<IrkksomeConnection> {
        private List<IrkksomeConnection> connections;

        public ConnectionsArrayAdapter(List<IrkksomeConnection> connections) {
            super(getActivity(), R.layout.server_connect_list_item, connections);
            this.connections = connections;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.server_connect_list_item, parent, false);
            }

            TextView text = (TextView) convertView.findViewById(android.R.id.text1);
            final IrkksomeConnection item = getItem(position);
            text.setText(connectionDAO.getPresentation(item));

            View button = convertView.findViewById(R.id.server_connect_list_delete);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "DELETE " + item.getId(), Toast.LENGTH_SHORT).show();
                    connectionDAO.remove(item);
                    connections.remove(item); // Fan vad android är runkigt ibland. Här hade man ju velat läsa upp allt från databasen igen.
                    notifyDataSetChanged();
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "CLICK " + item.getId(), Toast.LENGTH_SHORT).show();
                }
            });
            convertView.setLongClickable(true);
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return true;
                }
            });
            return convertView;
        }
    }
}
