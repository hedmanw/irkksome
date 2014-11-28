package se.alkohest.irkksome.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.ui.HilightManager;

public class HilightContainedFragment extends Fragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton hilights = (ImageButton) view.findViewById(R.id.hilight_button);
        if (hilights != null) {
            hilights.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HilightManager.getInstance().showHilight();
                }
            });
        }
        HilightManager.getInstance().updateHilightButton();
    }
}
