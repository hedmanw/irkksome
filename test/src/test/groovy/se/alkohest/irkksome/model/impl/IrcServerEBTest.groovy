package se.alkohest.irkksome.model.impl

import se.alkohest.irkksome.model.api.dao.IrcUserDAO
import spock.lang.Specification

public class IrcServerEBTest extends Specification {
    def userDAO = new IrcUserDAO()

    def "getSetUrl"() {
        when:
        def server = new IrcServerEB()
        server.setHost("localhost:8080")

        then:
        server.host == "localhost:8080"
    }

    def "getSetConnectedChannels"() {
        when:
        def server = new IrcServerEB()
        server.connectedChannels = new ArrayList<>()
        server.connectedChannels.add(new IrcChannelEB())
        server.connectedChannels.add(new IrcChannelEB())

        then:
        server.connectedChannels
        server.connectedChannels.size() == 2
    }

    def "getSetKnownUsers"() {
        when:
        def server = new IrcServerEB()
        server.knownUsers = new ArrayList<>()
        server.knownUsers.add(userDAO.create("fest"))
        server.knownUsers.add(userDAO.create("test"))

        then:
        server.knownUsers
        server.knownUsers.size() == 2
    }
}
