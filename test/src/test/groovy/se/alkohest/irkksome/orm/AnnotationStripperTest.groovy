package se.alkohest.irkksome.orm

import se.alkohest.irkksome.model.impl.FuckYouGradle
import se.alkohest.irkksome.model.impl.GradleIsPrettyWorthless
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

    def "Gets all regular fields"() {
        when:
        def heads = AnnotationStripper.getColumnHeads(FuckYouGradle.class)

        then:
        heads[0] == "id"
        heads[1] == "string"
        heads[2] == "count"
    }

    def "Does not include with OneToMany"() {
        when:
        def head = AnnotationStripper.getColumnHeads(GradleIsPrettyWorthless.class)

        then:
        head[0] == "id"
        head[1] == "someRelation"
    }

    def "Respects transient fields"() {
        when:
        def heads = AnnotationStripper.getColumnHeads(GradleSucks.class)

        then:
        !heads.contains("transientString")
    }

    def "Finds oneToMany field"() {
        when:
        def oneToMany = AnnotationStripper.getOneToMany(new GradleIsPrettyWorthless())

        then:
        oneToMany == FuckYouGradle.class
    }
}