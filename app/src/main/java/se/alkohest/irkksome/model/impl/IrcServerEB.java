package se.alkohest.irkksome.model.impl;

import android.content.ContentValues;

import java.util.List;
import java.util.Set;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcServer;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.orm.AbstractBean;

public class IrcServerEB extends AbstractBean implements IrcServer {
    private String url;
    private List<IrcChannel> connectedChannels;
    private Set<IrcUser> knownUsers;
    private IrcUser self;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
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
    public List<IrcChannel> getConnectedChannels() {
        return connectedChannels;
    }

    @Override
    public void setConnectedChannels(List<IrcChannel> connectedChannels) {
        this.connectedChannels = connectedChannels;
    }

    @Override
    public ContentValues createRow(long dependentPK) {
        return null;
    }
}
