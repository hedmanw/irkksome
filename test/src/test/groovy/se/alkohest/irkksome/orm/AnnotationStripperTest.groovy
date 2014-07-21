package se.alkohest.irkksome.orm

import se.alkohest.irkksome.model.impl.FuckYouGradle
import se.alkohest.irkksome.model.impl.GradleSucks
import spock.lang.Specification

public class AnnotationStripperTest extends Specification{
    def "Strips tablename correctly"() {
        when:
        def annotatedClass = new FuckYouGradle();

        then:
        AnnotationStripper.getTable(annotatedClass) == "some_table";
    }

    def "Returns null for not annotated class"() {
        when:
        def notAnnotatedClass = new GradleSucks();

        then:
        AnnotationStripper.getTable(notAnnotatedClass) == null
    }

    def "Gets all fields"() {

    }

    def "Respects transient fields"() {

    }
}