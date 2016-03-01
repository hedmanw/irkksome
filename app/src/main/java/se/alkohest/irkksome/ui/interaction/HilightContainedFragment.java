package se.alkohest.irkksome.ui.interaction;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import se.alkohest.irkksome.ui.HilightHandler;

public class HilightContainedFragment extends Fragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HilightHandler.getInstance().updateHilightButton();
    }
}
