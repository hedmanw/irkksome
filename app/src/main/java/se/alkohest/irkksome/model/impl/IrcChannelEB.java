package se.alkohest.irkksome.model.impl;

import android.content.ContentValues;

import java.util.List;
import java.util.Map;

import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.model.entity.IrcUser;
import se.alkohest.irkksome.orm.AbstractBean;
import se.alkohest.irkksome.orm.OneToMany;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Table;

@Table("Channel")
public class IrcChannelEB extends AbstractBean implements IrcChannel {
    @Column("topic")
    private String topic = "";
    @Column("name")
    private String name;
    private Map<IrcUser, String> users;
    @OneToMany(IrcMessageEB.class)
    private List<IrcMessage> messages;

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

//    @Override
    public ContentValues createRow(long dependentPK) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("server_id", dependentPK);
        contentValues.put("topic", topic);
        contentValues.put("name", name);
        return contentValues;
    }
}
