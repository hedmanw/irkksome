package se.alkohest.irkksome.ui.fragment.connection;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.dao.IrkksomeConnectionDAO;
import se.alkohest.irkksome.model.api.local.IrkksomeConnectionDAOLocal;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;
import se.alkohest.irkksome.model.impl.IrkksomeConnectionEB;

public abstract class AbstractConnectionFragment extends Fragment {
    public static final String CONNECTION_ARGUMENT = "CONNECTION";

    protected OnConnectPressedListener listener;
    protected IrkksomeConnection templateConnection;
    protected IrkksomeConnectionDAOLocal connectionDAO = new IrkksomeConnectionDAO();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            templateConnection = connectionDAO.findById(getArguments().getLong(CONNECTION_ARGUMENT));
        }
    }

    public void connectPressed() {
        if (listener != null) {
            IrkksomeConnection connection = getConnection();
            connection.setLastUsed(new Date());
            listener.onConnectPressed(connection);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnConnectPressedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnConnectPressedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public static AbstractConnectionFragment newInstance(int id) {
        switch (id) {
            case R.drawable.connection_icon_blue:
                return RegularConnectionFragment.newInstance();
            case R.drawable.connection_icon_purple:
                return IrssiProxyConnectionFragment.newInstance();
        }
        return null;
    }

    protected String getFieldValue(@IdRes int resourceId) {
        return ((TextView)getActivity().findViewById(resourceId)).getText().toString().trim();
    }

    protected void setFieldValue(View view, @IdRes int resourceId, String text) {
        ((TextView) view.findViewById(resourceId)).setText(text);
    }

    public abstract IrkksomeConnection getConnection();

    public interface OnConnectPressedListener {
        public void onConnectPressed(IrkksomeConnection irkksomeConnection);
    }
}
