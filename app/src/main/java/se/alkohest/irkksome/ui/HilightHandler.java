package se.alkohest.irkksome.ui;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.api.ServerManager;
import se.alkohest.irkksome.model.api.UnreadStack;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.enumerations.HilightLevel;
import se.alkohest.irkksome.ui.widget.HilightButton;

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
        HilightButton hilightButton = (HilightButton) context.findViewById(R.id.hilight_button);
        if (hilightButton != null) {
            // TODO: optimize
            final HilightLevel level = unreadStack.peekPriority();
            if (level == HilightLevel.NICKNAME) {
                hilightButton.setCounter(unreadStack.stackSize());
                hilightButton.setBackground(context.getDrawable(R.drawable.highlightbadge_background_highlight));
                hilightButton.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
            else if (level == HilightLevel.UNREAD) {
                hilightButton.setCounter(unreadStack.stackSize());
                hilightButton.setBackground(context.getDrawable(R.drawable.highlightbadge_background));
                hilightButton.setTextColor(ContextCompat.getColor(context, R.color.text_color_secondary));
            }
            else {
                hilightButton.setCounter(unreadStack.stackSize());
                hilightButton.setBackground(context.getDrawable(R.drawable.highlightbadge_background_inactive));
                hilightButton.setTextColor(ContextCompat.getColor(context, R.color.text_color_black_hint));
            }
        }
    }

    public String getHilightLevel(IrcServer ircServer, IrcChannel channel) {
        return unreadStack.peekPriorityForChannel(ircServer, channel).ordinal() + "";
    }
}
