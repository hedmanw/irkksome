package se.alkohest.irkksome.model.api.dao

import spock.lang.Specification

public class IrcUserDAOTest extends Specification {
    def ircUserDAO = new IrcUserDAO()

    def "Create user"() {
        when:
        def ircUser = ircUserDAO.create("LarsUrban")

        then:
        ircUser.name == "LarsUrban"
    }
}