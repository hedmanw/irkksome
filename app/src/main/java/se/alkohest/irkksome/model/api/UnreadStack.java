package se.alkohest.irkksome.model.api;

import java.util.Stack;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.enumerations.HilightLevel;

public class UnreadStack {
    private HilightStack hilights;

    public UnreadStack() {
        HilightStack unread = new HilightStack(HilightLevel.UNREAD);
        hilights = new HilightStack(HilightLevel.NICKNAME, unread);
    }

    /**
     * Pushes a (server, channel) tuple to the stack, removing an existing such tuple of lower or same priority
     */
    public void pushOverwrite(IrcServer ircServer, IrcChannel ircChannel, HilightLevel hilightLevel) {
        UnreadEntity entity = new UnreadEntity(ircChannel, ircServer);
        hilights.push(entity);
    }

    public HilightLevel peekPriority() {
        return hilights.peekPriority();
    }

    public int stackSize() {
        return hilights.size();
    }

    public UnreadEntity pop() {
        return hilights.pop();
    }

    public boolean hasUnread() {
        return !hilights.empty();
    }

    public void remove(IrcChannel channel, IrcServer server) {
        UnreadEntity entity = new UnreadEntity(channel, server);
        hilights.remove(entity);
    }

    private class HilightStack extends Stack<UnreadEntity> {
        private final HilightStack child;
        private final HilightLevel level;

        public HilightStack(HilightLevel level) {
            child = null;
            this.level = level;
        }

        public HilightStack(HilightLevel level, HilightStack child) {
            this.child = child;
            this.level = level;
        }

        public HilightLevel peekPriority() {
            if (!isEmpty()) {
                return level;
            }
            else {
                if (child != null) {
                    return child.peekPriority();
                }
                else {
                    return HilightLevel.NONE;
                }
            }
        }

        @Override
        public synchronized int size() {
            if (!isEmpty()) {
                return super.size();
            }
            else {
                if (child != null) {
                    return child.size();
                }
                else {
                    return 0;
                }
            }
        }

        @Override
        public UnreadEntity pop() {
            UnreadEntity head = super.pop();
            if (head == null && child != null) {
                return child.pop();
            }
            else {
                return head;
            }
        }

        @Override
        public UnreadEntity push(UnreadEntity object) {
            remove(object);
            return super.push(object);
        }

        @Override
        public boolean remove(Object object) {
            if (child != null) {
                child.remove(object);
            }
            return super.remove(object);
        }
    }

    public class UnreadEntity {
        private final IrcChannel channel;
        private final IrcServer server;

        public UnreadEntity(IrcChannel channel, IrcServer server) {
            this.channel = channel;
            this.server = server;
        }

        public IrcChannel getChannel() {
            return channel;
        }

        public IrcServer getServer() {
            return server;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UnreadEntity that = (UnreadEntity) o;

            return !(channel != null ? !channel.equals(that.channel) : that.channel != null) &&
                    !(server != null ? !server.equals(that.server) : that.server != null);

        }

        @Override
        public int hashCode() {
            int result = channel != null ? channel.hashCode() : 0;
            result = 31 * result + (server != null ? server.hashCode() : 0);
            return result;
        }
    }
}
