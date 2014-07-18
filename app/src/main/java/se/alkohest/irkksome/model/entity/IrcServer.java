package se.alkohest.irkksome.model.entity;

import java.util.List;

import se.alkohest.irkksome.orm.BeanEntity;

public interface IrcServer extends BeanEntity {
    public List<IrcChannel> getConnectedChannels();

    public void setConnectedChannels(List<IrcChannel> channels);

    public String getUrl();

    public void setUrl(String string);
}
