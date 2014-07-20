package se.alkohest.irkksome.model.api

import se.alkohest.irkksome.irc.IrcProtocol
import se.alkohest.irkksome.model.api.dao.IrcServerDAO
import spock.lang.Specification

public class ServerImplTest extends Specification {
    def backingServer = new IrcServerDAO().create("localhost")
    def server = new ServerImpl(backingServer)

    def setup() {
        server.ircProtocol = Mock(IrcProtocol)
    }

    def "prepends hash on channel name"() {
        when:
        server.joinChannel("fest")

        then:
        1 * server.ircProtocol.joinChannel("#fest")
    }

    def "does not prepend hash on complete channel name"() {
        when:
        server.joinChannel("#asdf")

        then:
        1 * server.ircProtocol.joinChannel("#asdf")
    }
}