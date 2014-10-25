package se.alkohest.irkksome.model.impl;

import android.content.ContentValues;

import java.util.Date;
import java.util.List;
import java.util.Set;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.orm.AbstractBean;
import se.emilsjolander.sprinkles.annotations.Table;

@Table("Server")
public class IrcServerEB extends AbstractBean implements IrcServer {
    private String host;
//    @OneToMany(IrcChannelEB.class)
    private List<IrcChannel> connectedChannels;
//    @Transient
    private Set<IrcUser> knownUsers;
//    @OneToOne(IrcUserEB.class)
    private IrcUser self;
//    @Transient
    private Date lastMessageTime;

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
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

//    @Override
    public ContentValues createRow(long dependentPK) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("host", host);
        contentValues.put("self", self.getId());
        return contentValues;
    }

    @Override
    public String toString() {
        return host + ":" + self.getName() + ":" + connectedChannels.get(0).getName();
    }
}
