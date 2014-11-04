package se.alkohest.irkksome.model.impl

import se.alkohest.irkksome.ColorProviderMockSpecification
import se.alkohest.irkksome.model.entity.IrcMessage

public class IrcMessageEBTest extends ColorProviderMockSpecification {
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

    def "getSetMessageType"() {
        when:
        def message = new IrcMessageEB();
        message.setMessageType(messageType)

        then:
        message.getMessageType() == messageType

        where:
        messageType << [
                IrcMessage.MessageTypeEnum.RECEIVED,
                IrcMessage.MessageTypeEnum.SENT,
                IrcMessage.MessageTypeEnum.JOIN,
                IrcMessage.MessageTypeEnum.PART,
                IrcMessage.MessageTypeEnum.QUIT,
                IrcMessage.MessageTypeEnum.NICKCHANGE
        ]
    }
}
