package se.alkohest.irkksome.model.impl;

import java.util.List;
import java.util.Map;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.orm.AbstractBean;

public class IrcChannelEB extends AbstractBean implements IrcChannel {
    private String topic;
    private Map<IrcUser, String> users;
    private List<IrcMessage> messages;
    private String name;

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<IrcUser, String> getUsers() {
        return users;
    }

    @Override
    public void setUsers(Map<IrcUser, String> users) {
        this.users = users;
    }

    @Override
    public List<IrcMessage> getMessages() {
        return messages;
    }

    @Override
    public void setMessages(List<IrcMessage> messages) {
        this.messages = messages;
    }
}
