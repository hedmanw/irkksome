package se.alkohest.irkksome.ui.connection;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrkksomeConnection;

public abstract class AbstractConnectionFragment extends Fragment implements AbstractConnectionView {
    public static final String CONNECTION_ARGUMENT = "CONNECTION";
    public static final int CONNECTION_ESTABLISHED = 1337;

    protected ConnectionPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new AbstractConnectionPresenter(this);
        if (getArguments() != null) {
            presenter.setTemplateConnection(getArguments().getLong(CONNECTION_ARGUMENT));
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
                presenter.connect(getConnection());
            }
        });
        final ViewGroup fieldContainer = (ViewGroup) inflatedView.findViewById(R.id.connection_fields_container);
        inflater.inflate(getLayout(),fieldContainer, true);
        inflateConnectionView(fieldContainer);
        return inflatedView;
    }

    @Override
    public void showProgress() {
        final Button button = (Button) getActivity().findViewById(R.id.server_connect_button);
        button.setText("Connecting...");
        button.setEnabled(false);
        getActivity().findViewById(R.id.server_connect_progress).setVisibility(View.VISIBLE);
    }

    @Override
    public void connectionSuccess() {
        getActivity().setResult(CONNECTION_ESTABLISHED);
        getActivity().finish();
    }

    @Override
    public void connectionFailure() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Button button = (Button) getActivity().findViewById(R.id.server_connect_button);
                if (button != null) {
                    button.setText("Connect");
                    button.setEnabled(true);
                    getActivity().findViewById(R.id.server_connect_progress).setVisibility(View.GONE);
                }
            }
        });
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
}
