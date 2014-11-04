package se.alkohest.irkksome.model.api.dao

import se.alkohest.irkksome.ColorProviderMockSpecification

public class IrcUserDAOTest extends ColorProviderMockSpecification {
    def ircUserDAO = new IrcUserDAO()

    def "Create user"() {
        when:
        def ircUser = ircUserDAO.create("LarsUrban")

        then:
        ircUser.name == "LarsUrban"
    }

    def "Compare user"() {
        when:
        def ircUser = ircUserDAO.create(nick1)

        then:
        ircUserDAO.compare(ircUser, nick2)

        where:
        nick1 << ["lostOne", "FeSt"]
        nick2 << ["lostOne", "fest"]
    }
}