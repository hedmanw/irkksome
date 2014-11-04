package se.alkohest.irkksome.model.impl

import se.alkohest.irkksome.ColorProviderMockSpecification

public class IrcMessageEBTest extends ColorProviderMockSpecification {
    def "getSetMessage"() {
        when:
        def message = new IrcChatMessageEB()
        message.setMessage("This is a message")

        then:
        message.getMessage() == "This is a message"
    }

    def "getSetTimestamp"() {
        when:
        def message = new IrcChatMessageEB()
        def date = new Date()
        message.setTimestamp(date)

        then:
        message.getTimestamp() == date
    }

    def "getSetAuthor"() {
        when:
        def message = new IrcChatMessageEB()
        def author = new IrcUserEB()
        author.setName("Korvryttarn")
        message.setAuthor(author)

        then:
        message.getAuthor() == author
    }
}
