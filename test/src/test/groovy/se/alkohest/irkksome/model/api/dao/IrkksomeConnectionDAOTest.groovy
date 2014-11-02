package se.alkohest.irkksome.model.api.dao

import spock.lang.Specification

public class IrkksomeConnectionDAOTest extends Specification {
    def irkksomeConnectionDAO = new IrkksomeConnectionDAO();

    def "Get server presentation for regular irkk"() {
        when:
        def connection = irkksomeConnectionDAO.create()
        connection.host = "irc.chalmers.se"
        connection.nickname = "Arnold"
        connection.username = "Arnold"
        connection.realname = "Arnold Schwartzenegger"
        connection.port = 6667
        connection.password = emptyField

        then:
        irkksomeConnectionDAO.getPresentation(connection) == "Arnold@irc.chalmers.se:6667"

        where:
        emptyField << ["", null]
    }

    def "Get server presentation for ssh (irssi proxy)"() {
        when:
        def connection = irkksomeConnectionDAO.create()
        connection.host = "localhost"
        connection.nickname = emptyField
        connection.username = "Terminator"
        connection.realname = emptyField
        connection.port = 2777
        connection.password = "password"
        connection.useSSH = true
        connection.sshHost = "hubben.chalmers.it"
        connection.sshUser = "arnold"
        connection.sshPass = "password"

        then:
        irkksomeConnectionDAO.getPresentation(connection) == "arnold@hubben.chalmers.it (Terminator:2777)"

        where:
        emptyField << ["", null]
    }

    def "Get server presentation for ssh (fallback if not irssi proxy)"() {
        when:
        def connection = irkksomeConnectionDAO.create()
        connection.host = "irc.openoed.org"
        connection.nickname = "korvryttarn"
        connection.username = "XM"
        connection.realname = "X-Man"
        connection.port = 1337
        connection.password = "password"
        connection.useSSH = true
        connection.sshHost = "hubben.chalmers.it"
        connection.sshUser = "arnold"
        connection.sshPass = "password"

        then:
        irkksomeConnectionDAO.getPresentation(connection) == "arnold@hubben.chalmers.it (korvryttarn[XM]@irc.openoed.org:1337)"

        where:
        emptyField << ["", null]
    }
}