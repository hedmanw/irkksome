package se.alkohest.irkksome.model.entity;

import java.util.List;
import java.util.Map;

import se.alkohest.irkksome.orm.BeanEntity;

public interface IrcChannel extends BeanEntity {
    public String getTopic();

    public void setTopic(String string);

    public void setName(String name);

    public String getName();

    public Map<IrcUser, String> getUsers();

    public void setUsers(Map<IrcUser, String> users);

    public List<IrcMessage> getMessages();

    public void setMessages(List<IrcMessage> messages);
}
