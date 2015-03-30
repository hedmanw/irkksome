package se.alkohest.irkksome.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by wilhelm 2014-11-06.
 */
public class ChatRecylerView extends RecyclerView {
    private OnSizeChangedListener onSizeChangedListener;

    public ChatRecylerView(Context context) {
        super(context);
    }

    public ChatRecylerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatRecylerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener) {
        this.onSizeChangedListener = onSizeChangedListener;
    }

    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        onSizeChangedListener.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
    }

    public interface OnSizeChangedListener {
        void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight);
    }
}
