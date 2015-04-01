package se.alkohest.irkksome.ui.pubkey.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import se.alkohest.irkksome.R;

public class PubkeyEnabledFragment extends Fragment {
    private static PubkeyManagementListener listener;

    public static PubkeyEnabledFragment newInstance(PubkeyManagementListener listener) {
        PubkeyEnabledFragment.listener = listener;
        return new PubkeyEnabledFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_pubkey_enabled, container, false);
        Button uploadButton = (Button) inflatedView.findViewById(android.R.id.button1);
//        uploadButton.setText("Upload to host data things yes?");
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.uploadPressed();
            }
        });
        return inflatedView;
    }

    public interface PubkeyManagementListener {
        public void uploadPressed();
    }
}
