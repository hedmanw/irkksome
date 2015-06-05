package se.alkohest.irkksome.model.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.OneToMany;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

@Table("Server")
public class IrcServerEB extends AbstractBean implements IrcServer {
    @Column("serverName")
    private String serverName;

    @OneToMany(IrcChannelEB.class)
    private List<IrcChannel> connectedChannels;

    private Set<String> knownUsers;

    private String self;

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
    public void setSelf(String user) {
        self = user;
    }

    @Override
    public String getSelf() {
        return self;
    }

    @Override
    public Set<String> getKnownUsers() {
        return knownUsers;
    }

    @Override
    public void setKnownUsers(Set<String> users) {
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
        return serverName + ":" + self + ":" + connectedChannels.get(0).getName();
    }
}
