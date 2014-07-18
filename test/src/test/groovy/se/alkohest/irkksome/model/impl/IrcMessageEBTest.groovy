package se.alkohest.irkksome.model.impl

import spock.lang.Specification

public class IrcMessageEBTest extends Specification {
    def "getSetMessage"() {
        when:
        def message = new IrcMessageEB()
        message.setMessage("This is a message")

        then:
        message.getMessage() == "This is a message"
    }

    def "getSetTimestamp"() {
        when:
        def message = new IrcMessageEB()
        def date = new Date()
        message.setTimestamp(date)

        then:
        message.getTimestamp() == date
    }

    def "getSetAuthor"() {
        when:
        def message = new IrcMessageEB()
        def author = new IrcUserEB()
        author.setName("Korvryttarn")
        message.setAuthor(author)

        then:
        message.getAuthor() == author
    }
}
