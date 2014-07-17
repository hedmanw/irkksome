package se.alkohest.irkksome

import spock.lang.Specification

public class DummyClassTest extends Specification {
    def "Stuff"() {
        when:
        DummyClass d = new DummyClass()
        int i = d.test()

        then:
        i == 42
    }

    def "Not stuff"() {
        when:
        DummyClass d = new DummyClass()
        int i = d.test()

        then:
        i != 41
    }
}