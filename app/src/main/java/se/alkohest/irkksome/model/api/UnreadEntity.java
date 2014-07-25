package se.alkohest.irkksome.model.api;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;

/**
 * Created by oed on 7/25/14.
 */
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
