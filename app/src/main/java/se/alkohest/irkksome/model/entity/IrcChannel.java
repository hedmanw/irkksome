package se.alkohest.irkksome.model.entity;

import java.util.List;
import java.util.Map;
import java.util.Queue;

import se.alkohest.irkksome.orm.BeanEntity;

public interface IrcChannel extends BeanEntity {
    public String getTopic();

    public void setTopic(String string);

    public void setName(String name);

    public String getName();

    public Map<IrcUser, Short> getUsers();

    public void setUsers(Map<IrcUser, Short> users);

    public List<IrcMessage> getMessages();

    public void setMessages(List<IrcMessage> messages);
}
