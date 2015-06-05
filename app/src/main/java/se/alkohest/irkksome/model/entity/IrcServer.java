package se.alkohest.irkksome.model.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import se.alkohest.irkksome.orm.BeanEntity;

public interface IrcServer extends BeanEntity {
    public List<IrcChannel> getConnectedChannels();

    public void setConnectedChannels(List<IrcChannel> channels);

    public String getServerName();

    public void setServerName(String string);

    public void setSelf(String user);

    public String getSelf();

    public Set<String> getKnownUsers();

    public void setKnownUsers(Set<String> users);

    public void setLastMessageTime(Date time);

    public Date getLastMessageTime();
}
