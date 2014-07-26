package se.alkohest.irkksome.db

import se.alkohest.irkksome.model.impl.IrcChannelEB
import se.alkohest.irkksome.model.impl.IrcMessageEB
import se.alkohest.irkksome.model.impl.IrcServerEB
import se.alkohest.irkksome.model.impl.IrcUserEB
import spock.lang.Specification

public class SQLMapperTest extends Specification {
    def "Maps simple object"() {
        when:
        Class[] classes = new Class[4];
        classes[0] = IrcServerEB.class;
        classes[1] = IrcChannelEB.class;
        classes[2] = IrcMessageEB.class;
        classes[3] = IrcUserEB.class;
        String createStatement = SQLMapper.getFullCreateStatement(classes);
        def user = "CREATE TABLE t_user(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);"
        def server = "CREATE TABLE t_server(id INTEGER PRIMARY KEY AUTOINCREMENT, url TEXT NOT NULL, self INTEGER NOT NULL);"
        def channel = "CREATE TABLE t_channel(id INTEGER PRIMARY KEY AUTOINCREMENT, topic TEXT NOT NULL, name TEXT NOT NULL, server_id INTEGER NOT NULL);"
        def message = "CREATE TABLE t_message(id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT NOT NULL, author INTEGER NOT NULL, hilight BOOLEAN NOT NULL, channel_id INTEGER NOT NULL);"

        then:
        createStatement.contains(user)
        createStatement.contains(server)
        createStatement.contains(channel)
        createStatement.contains(message)
        createStatement.length() == user.length() + server.length() + channel.length() + message.length()
    }
}