package se.alkohest.irkksome.model.api;

import java.util.Stack;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;

/**
 * Created by oed on 7/25/14.
 */
public class UnreadStack {

    private Stack<UnreadEntity> hilights;
    private Stack<UnreadEntity> messages;

    public UnreadStack() {
        hilights = new Stack<>();
        messages = new Stack<>();
    }

    public void push(UnreadEntity entity, boolean isHiligth) {
        if (isHiligth) {
            hilights.push(entity);
            messages.remove(entity);
        } else {
            messages.push(entity);
        }
    }

    public UnreadEntity pop() {
        if (!hilights.empty()) {
            return hilights.pop();
        } else if (!messages.empty()) {
            return messages.pop();
        }
        return null;
    }

    public boolean hasUnread() {
        return !hilights.empty() || !messages.empty();
    }

    public void remove(IrcChannel channel, IrcServer server) {
        UnreadEntity entity = new UnreadEntity(channel, server);
        for (UnreadEntity e : hilights) {
            if (e.equals(entity)) {
                hilights.remove(e);
                return;
            }
        }
        for (UnreadEntity e : messages) {
            if (e.equals(entity)) {
                messages.remove(e);
                return;
            }
        }
    }

    public int getHilightCount() {
        return hilights.size();
    }

    public int getMessageCount() {
        return messages.size();
    }
}
