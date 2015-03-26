package se.alkohest.irkksome.ui.fragment;

import android.app.Fragment;
import android.graphics.Outline;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.ui.HilightHandler;

public class HilightContainedFragment extends Fragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button hilights = (Button) view.findViewById(R.id.hilight_button);
        hilights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HilightHandler.getInstance().showHilight();
            }
        });

        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int size = getResources().getDimensionPixelSize(R.dimen.hilight_button_size);
                outline.setOval(0, 0, size, size);
            }
        };
        hilights.setOutlineProvider(viewOutlineProvider);
        HilightHandler.getInstance().updateHilightButton();
    }
}
