package se.alkohest.irkksome.db

import se.alkohest.irkksome.model.impl.*
import spock.lang.Ignore
import spock.lang.Specification

public class SQLMapperTest extends Specification {
    @Ignore
    def "Schema creation"() {
        when:
        Class[] classes = new Class[6];
        classes[0] = IrcServerEB.class;
        classes[1] = IrcChannelEB.class;
        classes[2] = IrcChatMessageEB.class;
        classes[3] = IrcMessageEB.class;
        classes[4] = IrcUserEB.class;
        classes[5] = IrkksomeConnectionEB.class;
        String[] createStatement = SQLMapper.getFullCreateStatement(classes);

        def user = "CREATE TABLE t_user(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);"
        def server = "CREATE TABLE t_server(id INTEGER PRIMARY KEY AUTOINCREMENT, host TEXT NOT NULL, self INTEGER NOT NULL);"
        def channel = "CREATE TABLE t_channel(id INTEGER PRIMARY KEY AUTOINCREMENT, topic TEXT NOT NULL, name TEXT NOT NULL, server_id INTEGER NOT NULL);"
        def message = "CREATE TABLE t_message(id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT NOT NULL, channel_id INTEGER NOT NULL);"
        def chatmessage = "CREATE TABLE t_chatmessage(id INTEGER PRIMARY KEY AUTOINCREMENT, author INTEGER NOT NULL, hilight INTEGER NOT NULL);"
        def irkksomeconn = "CREATE TABLE t_connection(id INTEGER PRIMARY KEY AUTOINCREMENT, host TEXT NOT NULL, port INTEGER NOT NULL, nickname TEXT NOT NULL, username TEXT, realname TEXT, useSSL INTEGER NOT NULL, useSSH INTEGER NOT NULL, sshHost TEXT, sshUser TEXT, sshPort INTEGER NOT NULL);"
        int length = 0;
        createStatement.each {
            length += it.length()
        }

        then:
        createStatement.contains(user)
        createStatement.contains(server)
        createStatement.contains(channel)
        createStatement.contains(message)
        createStatement.contains(chatmessage)
        createStatement.contains(irkksomeconn)
        length == user.length() + server.length() + channel.length() + message.length() + chatmessage.length() + irkksomeconn.length()
    }

    def "getCreateStatement works for plain object"() {
        when:
        String userEB = SQLMapper.getCreateStatement(IrcUserEB.class)

        then:
        userEB == "CREATE TABLE User(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);"
    }

    def "getCreateStatement works for multiple types/nullable"() {
        when:
        String irkksomeConnectionEB = SQLMapper.getCreateStatement(IrkksomeConnectionEB.class)

        then:
        irkksomeConnectionEB == "CREATE TABLE Connection(id INTEGER PRIMARY KEY AUTOINCREMENT, host TEXT NOT NULL, port INTEGER NOT NULL, nickname TEXT NOT NULL, username TEXT, realname TEXT, useSSL INTEGER NOT NULL, useSSH INTEGER NOT NULL, sshHost TEXT, sshUser TEXT, sshPort INTEGER NOT NULL);"
    }

    def "getCreateStatement works for one to one"() {
        when:
        String serverEB = SQLMapper.getCreateStatement(IrcServerEB)

        then:
        serverEB == "CREATE TABLE Server(id INTEGER PRIMARY KEY AUTOINCREMENT, host TEXT NOT NULL, self INTEGER NOT NULL);"
    }

    def "getCreateStatement works for one to many"() {
        when:
        SQLMapper.getCreateStatement(IrcServerEB)
        def channelEB = SQLMapper.getCreateStatement(IrcChannelEB)

        then:
        channelEB == "CREATE TABLE Channel(id INTEGER PRIMARY KEY AUTOINCREMENT, topic TEXT NOT NULL, name TEXT NOT NULL, server_id INTEGER NOT NULL);"
    }

    def "getCreateStatement works for IS A"() {
        when:
        String chatMessageEB = SQLMapper.getCreateStatement(IrcChatMessageEB)

        then:
        chatMessageEB == "CREATE TABLE ChatMessage(id INTEGER PRIMARY KEY AUTOINCREMENT, message_id INTEGER NOT NULL, author_id INTEGER NOT NULL, hilight INTEGER NOT NULL);"
    }
}