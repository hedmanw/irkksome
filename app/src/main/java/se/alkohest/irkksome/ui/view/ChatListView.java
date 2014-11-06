package se.alkohest.irkksome.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by wilhelm 2014-11-06.
 */
public class ChatListView extends ListView {
    private OnSizeChangedListener onSizeChangedListener;

    public ChatListView(Context context) {
        super(context);
    }

    public ChatListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatListView(Context context, AttributeSet attrs, int defStyle) {
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
