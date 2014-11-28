package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.ServerManager;
import se.alkohest.irkksome.model.api.UnreadEntity;
import se.alkohest.irkksome.model.api.UnreadStack;

public class HilightManager {
    private static HilightManager instance;
    private Activity context;
    private UnreadStack unreadStack;

    public static void setInstance(Activity context, UnreadStack unreadStack) {
        instance = new HilightManager(context, unreadStack);
    }

    public static HilightManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("HilightManager not initialized");
        }
        return instance;
    }

    private HilightManager(Activity context, UnreadStack unreadStack) {
        this.context = context;
        this.unreadStack = unreadStack;
    }



    public void showHilight() {
        ServerManager serverManager = ServerManager.INSTANCE;
        if (serverManager.getUnreadStack().hasUnread()) {
            UnreadEntity entity = serverManager.getUnreadStack().pop();
            if (serverManager.getActiveServer() != entity.getServer()) {
                serverManager.setActiveServer(entity.getServer());
            }
            serverManager.getActiveServer().setActiveChannel(entity.getChannel());
        }
    }

    public void updateHilightButton() {
        ImageButton number = (ImageButton) context.findViewById(R.id.hilight_button);
        if (number != null) {
            if (unreadStack.getHilightCount() > 0) {
                number.setVisibility(View.VISIBLE);
                number.setBackground(context.getDrawable(R.drawable.highlightbadge_background_highlight));
            }
            else if (unreadStack.getMessageCount() > 0) {
                number.setVisibility(View.VISIBLE);
                number.setBackground(context.getDrawable(R.drawable.highlightbadge_background));
            }
            else {
                number.setVisibility(View.GONE);
            }
        }
    }
}
