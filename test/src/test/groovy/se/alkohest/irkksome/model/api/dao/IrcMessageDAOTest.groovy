package se.alkohest.irkksome.model.api.dao

import se.alkohest.irkksome.ColorProviderMockSpecification

public class IrcMessageDAOTest extends ColorProviderMockSpecification {
    def ircMessageDAO = new IrcMessageDAO();
    def ircUserDAO = new IrcUserDAO();

    def "Create a message"() {
        when:
        def lars = ircUserDAO.create("Lars")
        def date = new Date()
        def ircMessage = ircMessageDAO.create(lars, "fest?", date)

        then:
        ircMessage.author == lars
        ircMessage.message == "fest?"
        ircMessage.timestamp == date
    }


}