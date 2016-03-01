package se.alkohest.irkksome.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.alkohest.irkksome.R;

/**
 * Created by Johan on 2/29/2016.
 */
public class HilightButton extends LinearLayout {
    private final TextView counter;
    private final LinearLayout container;

    public HilightButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.hilight_button, this, true);

        counter = (TextView) findViewById(R.id.hilight_button_counter);
        container = (LinearLayout) findViewById(R.id.container);
    }

    public void setCounter(int numberOfUnreadHilights) {
        counter.setText(String.valueOf(numberOfUnreadHilights));
    }

    @Override
    public void setBackground(Drawable background) {
        container.setBackground(background);
    }

    public void setTextColor(int color) {
        counter.setTextColor(color);
    }
}
