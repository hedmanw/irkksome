package se.alkohest.irkksome.model.api.dao

import se.alkohest.irkksome.model.entity.IrcChannel
import spock.lang.Specification

public class IrcChannelDAOTest extends Specification {
    def ircChannelDAO = new IrcChannelDAO()
    def ircMessageDAO = new IrcMessageDAO()

    def "Create initializes fields"() {
        when:
        IrcChannel ircChannel = ircChannelDAO.create()

        then:
        ircChannel.messages != null
        ircChannel.users != null
        ircChannel.topic == null
    }

    def "Add message to channel"() {
        when:
        def ircChannel = ircChannelDAO.create()
        def message = ircMessageDAO.create(null, "fest", null)
        ircChannelDAO.addMessage(ircChannel, message)

        then:
        ircChannel.messages.size() == 1
        ircChannel.messages.get(0) == message
    }
}