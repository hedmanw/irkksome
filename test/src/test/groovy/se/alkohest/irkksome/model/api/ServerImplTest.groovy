package se.alkohest.irkksome.model.api

import se.alkohest.irkksome.irc.IrcProtocol
import se.alkohest.irkksome.model.api.dao.IrcChannelDAO
import se.alkohest.irkksome.model.api.dao.IrcMessageDAO
import se.alkohest.irkksome.model.api.dao.IrcServerDAO
import se.alkohest.irkksome.model.api.dao.IrcUserDAO
import spock.lang.Specification

public class ServerImplTest extends Specification {
    def backingServer = new IrcServerDAO().create("localhost")
    def server = new ServerImpl(backingServer, "fest")
    def serverDAO = new IrcServerDAO()
    def channelDAO = new IrcChannelDAO()
    def messageDAO = new IrcMessageDAO()
    def userDAO = new IrcUserDAO()

    def setup() {
        server.ircProtocol = Mock(IrcProtocol)
        server.listener = Mock(ServerCallback)
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

    def "change nick"() {
        when:
        server.changeNick("fest")

        then:
        server.ircProtocol.setNick("fest")
    }

    def "leave channel"() {
        when:
        server.leaveChannel(channelDAO.create("#fest"))

        then:
        1 * server.ircProtocol.partChannel("#fest")
    }

    def "send message"() {
        def channel = channelDAO.create("#fest")
        when:
        server.sendMessage(channel, "Äre fest?")

        then:
        1 * server.ircProtocol.sendChannelMessage("#fest", "Äre fest?")
        1 * server.listener.messageReceived()
        channel.messages.size() == 1
    }

    def "server connected"() {
        when:
        server.serverConnected()

        then:
        0 * server.listener._
    }

    def "server Registered"() {
        when:
        server.serverRegistered("irc.chalmers.it", "fest")

        then:
        server.ircServer.getSelf().name == "fest"
        1 * server.listener.showServerInfo(server.ircServer)
    }

    def "nick changed"() {
        when:
        server.ircServer.self = userDAO.create("fest")
        server.nickChanged("fest", "slutfest")
        server.nickChanged("old", "new")

        then:
        server.ircServer.getSelf().name == "slutfest"
        1 * server.listener.nickChanged("old", {it.name == "new"})
    }

    def "users in channel"() {
        when:
        server.usersInChannel("#fest", ["test", "hasse", "%hej"])
        def users = serverDAO.getChannel(server.ircServer, "#fest").users

        then:
        server.ircServer.knownUsers.size() == 3
        users.size() == 3
        users.get(serverDAO.getUser(server.ircServer, "test")) == ""
        users.get(serverDAO.getUser(server.ircServer, "hej")) == "%"
    }

    def "user joined" () {
        when:
        server.ircServer.self = userDAO.create("palle")
        server.userJoined("#fest", "palle")
        server.userJoined("#fest", "pelle")

        then:
        1 * server.listener.userJoinedChannel(serverDAO.getChannel(server.ircServer, "#fest"),
                                            userDAO.create("pelle"))
        1 * server.listener.setActiveChannel(serverDAO.getChannel(server.ircServer, "#fest"))
        server.activeChannel == serverDAO.getChannel(server.ircServer, "#fest")
    }

    def "user parted"() {
        when:
        server.backingBean.setSelf(userDAO.create("erland"));
        server.userParted("#fest", "palle")

        then:
        1 * server.listener.userLeftChannel(serverDAO.getChannel(server.ircServer, "#fest"),
                                            serverDAO.getUser(server.ircServer, "palle"))
    }

    def "user quit"() {
        when:
        def channel1 = channelDAO.create("#fest")
        def channel2 = channelDAO.create("#pep")
        def channel3 = channelDAO.create("#irkksome")
        def user = userDAO.create("palle")
        channel1.users.put(user, "")
        channel2.users.put(user, "@")
        server.ircServer.connectedChannels = [channel1, channel2, channel3]
        server.userQuit("palle", "bye..")

        then:
        1 * server.listener.userQuit(serverDAO.getUser(server.ircServer, "palle"),
                {it.size() == 2})
    }

    def "channel message received"() {
        when:
        server.channelMessageReceived("#fest", "lars", "lalalala")

        then:
        1 * server.listener.messageReceived()
        serverDAO.getChannel(server.ircServer, "#fest").messages.size() == 1
    }

    def "show server"() {
        when:
        server.showServer()

        then:
        1 * server.listener.showServerInfo(server.ircServer)
    }

    def "error"() {
        when:
        server.ircError("433", "nick already in use")

        then:
        1 * server.listener.error("nick already in use")
    }
}