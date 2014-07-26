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
        println createStatement

        then:
        createStatement == "CREATE TABLE t_channel(id INTEGER PRIMARY KEY AUTOINCREMENT, topic TEXT NOT NULL, name TEXT NOT NULL, server_id INTEGER NOT NULL);CREATE TABLE t_server(id INTEGER PRIMARY KEY AUTOINCREMENT, url TEXT NOT NULL, self INTEGER NOT NULL);CREATE TABLE t_user(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);CREATE TABLE t_message(id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT NOT NULL, author INTEGER NOT NULL, hilight INTEGER NOT NULL, channel_id INTEGER NOT NULL);"
    }
}