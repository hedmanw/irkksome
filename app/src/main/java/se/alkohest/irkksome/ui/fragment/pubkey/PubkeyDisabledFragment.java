package se.alkohest.irkksome.ui.fragment.pubkey;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import se.alkohest.irkksome.R;

public class PubkeyDisabledFragment extends Fragment {
    private static CreatePubkeyPressListener listener;

    public static PubkeyDisabledFragment newInstance(CreatePubkeyPressListener listener) {
        PubkeyDisabledFragment.listener = listener;
        return new PubkeyDisabledFragment();
    }

    public PubkeyDisabledFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_pubkey_disabled, container, false);
        inflatedView.findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.createPubkey();
            }
        });
        return inflatedView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_pubkey_copy).setVisible(false);
        menu.findItem(R.id.action_upload_pubkey).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public interface CreatePubkeyPressListener {
        public void createPubkey();
    }
}
