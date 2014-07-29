package se.alkohest.irkksome.orm

import se.alkohest.irkksome.model.entity.IrcMessage
import se.alkohest.irkksome.model.impl.FuckYouGradle
import se.alkohest.irkksome.model.impl.GradleIsPrettyWorthless
import se.alkohest.irkksome.model.impl.GradleSucks
import se.alkohest.irkksome.model.impl.IrcChannelEB
import se.alkohest.irkksome.model.impl.IrcMessageEB
import se.alkohest.irkksome.model.impl.IrcServerEB
import se.alkohest.irkksome.model.impl.IrcUserEB
import spock.lang.Specification

public class AnnotationStripperTest extends Specification{
    def "Strips tablename correctly"() {
        when:
        def annotatedClass = new IrcServerEB();

        then:
        AnnotationStripper.getTable(annotatedClass) == "t_server";
    }

    def "Returns null for not annotated class"() {
        when:
        def notAnnotatedClass = new GradleSucks();

        then:
        AnnotationStripper.getTable(notAnnotatedClass) == null
    }

//    def "Gets all columns for the entity table with OTM and OTO"() {
//        when:
//        def heads = AnnotationStripper.getColumnHeads(IrcServerEB.class)
//
//        then:
//        heads[0] == "id"
//        heads[1] == "host"
//        heads[2] == "self"
//    }
//
//    def "Gets all columns for regular tables"() {
//        when:
//        def head = AnnotationStripper.getColumnHeads(IrcUserEB.class)
//
//        then:
//        head[0] == "id"
//        head[1] == "name"
//    }
//
//    def "Respects transient fields"() {
//        when:
//        def heads = AnnotationStripper.getColumnHeads(GradleSucks.class)
//
//        then:
//        !heads.contains("transientString")
//    }

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