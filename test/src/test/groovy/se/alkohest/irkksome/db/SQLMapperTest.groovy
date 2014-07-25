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
        createStatement
    }
}