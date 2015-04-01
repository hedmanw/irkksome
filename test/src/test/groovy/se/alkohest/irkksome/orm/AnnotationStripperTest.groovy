package se.alkohest.irkksome.orm

import se.alkohest.irkksome.model.impl.*
import spock.lang.Specification

public class AnnotationStripperTest extends Specification{
    def "Strips tablename correctly"() {
        when:
        def annotatedClass = new IrcServerEB();

        then:
        AnnotationStripper.getTable(annotatedClass) == "Server";
    }

    def "Gets OTM class"() {
        when:
        def oneToMany = AnnotationStripper.getOneToMany(new IrcChannelEB())

        then:
        oneToMany == IrcMessageEB.class
    }

    def "Gets OTO class"() {
        when:
        def oneToOne = AnnotationStripper.getOneToOne(new IrcMessageEB())

        then:
        oneToOne == IrcUserEB.class
    }
}