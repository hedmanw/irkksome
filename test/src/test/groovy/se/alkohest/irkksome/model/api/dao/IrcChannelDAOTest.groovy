package se.alkohest.irkksome.model.api.dao

import se.alkohest.irkksome.model.entity.IrcChannel
import se.alkohest.irkksome.model.entity.IrcMessage
import spock.lang.Specification

public class IrcChannelDAOTest extends Specification {
    def ircChannelDAO = new IrcChannelDAO()
    def ircMessageDAO = new IrcMessageDAO()
    def ircUserDAO = new IrcUserDAO()

    def "Create initializes fields"() {
        when:
        IrcChannel ircChannel = ircChannelDAO.create("#festkanalen")

        then:
        ircChannel.name == "#festkanalen"
        ircChannel.messages != null
        ircChannel.users != null
        ircChannel.topic == ""
    }

    def "Add message to channel"() {
        when:
        def ircChannel = ircChannelDAO.create("#fest")
        def user = ircUserDAO.create("Heissman")
        def message = ircMessageDAO.create(IrcMessage.MessageTypeEnum.SENT, user, "fest", new Date())
        ircChannelDAO.addMessage(ircChannel, message)

        then:
        ircChannel.messages.size() == 1
        ircChannel.messages.get(0) == message

        when:
        def someOtherUser = ircUserDAO.create("korv")
        def secondMessage = ircMessageDAO.create(IrcMessage.MessageTypeEnum.RECEIVED, someOtherUser, "ja!", new Date())
        ircChannelDAO.addMessage(ircChannel, secondMessage)

        then:
        ircChannel.messages.size() == 2
        ircChannel.messages == [message, secondMessage]
    }

    def "Compare user"() {
        when:
        def ircChannel = ircChannelDAO.create(channel1)

        then:
        ircChannelDAO.compare(ircChannel, channel2)

        where:
        channel1 << ["#IrCsEx-asp", "#fest"]
        channel2 << ["#ircsex-asp", "#fest"]
    }
}