package se.alkohest.irkksome.model.impl

import spock.lang.Specification

public class IrcServerEBTest extends Specification {
    def "getSetUrl"() {
        when:
        def server = new IrcServerEB()
        server.setUrl("localhost:8080")

        then:
        server.url == "localhost:8080"
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
        server.knownUsers.add(new IrcUserEB())
        server.knownUsers.add(new IrcUserEB())

        then:
        server.knownUsers
        server.knownUsers.size() == 2
    }
}
