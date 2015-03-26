package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.ServerManager;
import se.alkohest.irkksome.model.api.UnreadStack;
import se.alkohest.irkksome.model.enumerations.HilightLevel;

public class HilightHandler {
    private static HilightHandler instance;
    private Activity context;
    private UnreadStack unreadStack;

    public static void setInstance(Activity context, UnreadStack unreadStack) {
        instance = new HilightHandler(context, unreadStack);
    }

    public static HilightHandler getInstance() {
        if (instance == null) {
            throw new RuntimeException("HilightManager not initialized");
        }
        return instance;
    }

    private HilightHandler(Activity context, UnreadStack unreadStack) {
        this.context = context;
        this.unreadStack = unreadStack;
    }



    public void showHilight() {
        ServerManager serverManager = ServerManager.INSTANCE;
        if (serverManager.getUnreadStack().hasUnread()) {
            UnreadStack.UnreadEntity entity = serverManager.getUnreadStack().pop();
            if (serverManager.getActiveServer() != entity.getServer()) {
                serverManager.setActiveServer(entity.getServer());
            }
            serverManager.getActiveServer().setActiveChannel(entity.getChannel());
        }
    }

    public void updateHilightButton() {
        Button number = (Button) context.findViewById(R.id.hilight_button);
        if (number != null) {
            // TODO: optimize
            final HilightLevel level = unreadStack.peekPriority();
            if (level == HilightLevel.NICKNAME) {
                number.setVisibility(View.VISIBLE);
                number.setText(unreadStack.stackSize() + "");
                number.setBackground(context.getDrawable(R.drawable.highlightbadge_background_highlight));
            }
            else if (level == HilightLevel.UNREAD) {
                number.setVisibility(View.VISIBLE);
                number.setText(unreadStack.stackSize() + "");
                number.setBackground(context.getDrawable(R.drawable.highlightbadge_background));
            }
            else {
                number.setVisibility(View.GONE);
            }
        }
    }
}
