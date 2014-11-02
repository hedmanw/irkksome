package se.alkohest.irkksome.model.api.dao

import spock.lang.Specification

public class IrcServerDAOTest extends Specification {
    def ircServerDAO = new IrcServerDAO()

    def "Create a server"() {
        when:
        def ircServer = ircServerDAO.create("localhost")

        then:
        ircServer.connectedChannels != null
        ircServer.host == "localhost"
    }
}