package se.alkohest.irkksome.model.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.OneToMany;
import se.alkohest.irkksome.orm.OneToOne;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

@Table("Server")
public class IrcServerEB extends AbstractBean implements IrcServer {
    @Column("serverName")
    private String serverName;

    @OneToMany(IrcChannelEB.class)
    private List<IrcChannel> connectedChannels;

    private Set<IrcUser> knownUsers;

    @Column("self")
    @OneToOne(IrcUserEB.class)
    private IrcUser self;

    private Date lastMessageTime;

    @Override
    public String getServerName() {
        return serverName;
    }

    @Override
    public void setServerName(String host) {
        this.serverName = host;
    }

    @Override
    public void setSelf(IrcUser user) {
        self = user;
    }

    @Override
    public IrcUser getSelf() {
        return self;
    }

    @Override
    public Set<IrcUser> getKnownUsers() {
        return knownUsers;
    }

    @Override
    public void setKnownUsers(Set<IrcUser> users) {
        knownUsers = users;
    }

    @Override
    public void setLastMessageTime(Date time) {
        lastMessageTime = time;
    }

    @Override
    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    @Override
    public List<IrcChannel> getConnectedChannels() {
        return connectedChannels;
    }

    @Override
    public void setConnectedChannels(List<IrcChannel> connectedChannels) {
        this.connectedChannels = connectedChannels;
    }

    @Override
    public String toString() {
        return serverName + ":" + self.getName() + ":" + connectedChannels.get(0).getName();
    }
}
