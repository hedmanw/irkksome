package se.alkohest.irkksome.model.impl

import se.alkohest.irkksome.ColorProviderMockSpecification
import se.alkohest.irkksome.model.entity.IrcMessage
import se.alkohest.irkksome.model.entity.IrcUser

public class IrcChannelEBTest extends ColorProviderMockSpecification {
    def "getSetUsers"() {
        when:
        def ircChannel = new IrcChannelEB()
        def users = new HashMap<IrcUser, Short>()

        def korv = new IrcUserEB()
        korv.setName("Korv")
        def ryttarn = new IrcUserEB()
        ryttarn.setName("Ryttarn")
        def admin = new IrcUserEB()
        admin.setName("Adminguy")

        users.put(korv, (Short)0)
        users.put(ryttarn, (Short)0)
        users.put(admin, (Short)3)

        ircChannel.setUsers(users)

        then:
        ircChannel.getUsers()
        ircChannel.getUsers().get(admin) == 3
        ircChannel.getUsers().get(korv) == 0
    }

    def "getSetMessages"() {
        when:
        def fest = new IrcMessageEB()
        fest.setMessage("fest?")
        def ligga = new IrcMessageEB()
        ligga.setMessage("ligga!")

        def messages = new ArrayList<IrcMessage>()
        messages.add(fest)
        messages.add(ligga)

        def ircChannel = new IrcChannelEB()
        ircChannel.setMessages(messages)

        then:
        ircChannel.getMessages()
        ircChannel.getMessages().get(0) == fest
        ircChannel.getMessages().get(1) == ligga
    }

    def "get set topic"() {
        when:
        def ircChannel = new IrcChannelEB()
        ircChannel.topic = "stuff, man"

        then:
        ircChannel.topic == "stuff, man"
    }
}
