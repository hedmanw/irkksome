package se.alkohest.irkksome.ui.fragment.connection;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.dao.IrkksomeConnectionDAO;
import se.alkohest.irkksome.model.api.local.IrkksomeConnectionDAOLocal;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_connection_container, container, false);
        View icon = inflatedView.findViewById(R.id.server_connect_icon);
        icon.setBackground(getResources().getDrawable(getIcon()));
        TextView heading = (TextView) inflatedView.findViewById(R.id.server_connect_type_name);
        heading.setText(getHeadingStringId());

        inflatedView.findViewById(R.id.server_connect_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectPressed();
            }
        });
        final ViewGroup fieldContainer = (ViewGroup) inflatedView.findViewById(R.id.connection_fields_container);
        inflater.inflate(getLayout(),fieldContainer, true);
        inflateConnectionView(fieldContainer);
        return inflatedView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    protected String getFieldValue(@IdRes int resourceId) {
        return ((TextView)getActivity().findViewById(resourceId)).getText().toString().trim();
    }

    protected void setFieldValue(View view, @IdRes int resourceId, String text) {
        ((TextView) view.findViewById(resourceId)).setText(text);
    }

    public abstract IrkksomeConnection getConnection();
    protected abstract @LayoutRes int getLayout();
    protected abstract void inflateConnectionView(ViewGroup parent);
    protected abstract @DrawableRes int getIcon();
    protected abstract @StringRes int getHeadingStringId();

    public interface OnConnectPressedListener {
        public void onConnectPressed(IrkksomeConnection irkksomeConnection);
    }
}
