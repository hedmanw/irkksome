package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.irc.Log;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServerConnectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServerConnectFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ServerConnectFragment extends Fragment {
    public static final String ARG_HOSTNAME = "hostname";
    public static final String ARG_NICKNAME = "nickname";

    private OnFragmentInteractionListener listener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServerConnectFragment.
     */
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
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflatedView = inflater.inflate(R.layout.fragment_server_connect, container, false);
        inflatedView.findViewById(R.id.server_connect_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectPressed();
            }
        });
        return inflatedView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void connectPressed() {
        Log.getInstance(getClass()).i("Pressed button!");
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
