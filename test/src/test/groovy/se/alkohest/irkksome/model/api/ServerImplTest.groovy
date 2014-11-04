package se.alkohest.irkksome.model.api

import se.alkohest.irkksome.ColorProviderMockSpecification
import se.alkohest.irkksome.irc.IrcProtocol
import se.alkohest.irkksome.model.api.dao.*

public class ServerImplTest extends ColorProviderMockSpecification {
    def backingServer = new IrcServerDAO().create("localhost")
    def data = new IrkksomeConnectionDAO().create()
    Server server
    def serverDAO = new IrcServerDAO()
    def channelDAO = new IrcChannelDAO()
    def messageDAO = new IrcMessageDAO()
    def userDAO = new IrcUserDAO()

    def setup() {
        data.nickname = "test"
        data.username = ""
        server = new ServerImpl(backingServer, data)
        server.ircProtocol = Mock(IrcProtocol)
        server.listener = Mock(ServerCallback)
        server.dropListener = Mock(ServerDropAcidListener)
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
        1 * server.listener.messageReceived(_)
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
        1 * server.listener.showServerInfo(server.ircServer, server.motd)
    }

    def "nick changed"() {
        when:
        server.ircServer.self = userDAO.create("fest")
        server.nickChanged("fest", "slutfest", new Date())
        server.nickChanged("old", "new", new Date())

        then:
        server.ircServer.getSelf().name == "slutfest"
        1 * server.listener.postInfoMessage(*_)
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
        server.userJoined("#fest", "palle", new Date())
        server.userJoined("#fest", "pelle", new Date())

        then:
        1 * server.listener.postInfoMessage(*_)
        1 * server.listener.setActiveChannel(serverDAO.getChannel(server.ircServer, "#fest"))
        server.activeChannel == serverDAO.getChannel(server.ircServer, "#fest")
    }

    def "user parted"() {
        when:
        server.backingBean.setSelf(userDAO.create("erland"));
        server.userParted("#fest", "palle", new Date())

        then:
        1 * server.listener.postInfoMessage(*_)
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
        server.userQuit("palle", "bye..", new Date())

        then:
        1 * server.listener.postInfoMessage(*_)
    }

    def "channel message received"() {
        when:
        server.backingBean.setSelf(userDAO.create("erland"));
        server.channelMessageReceived("#fest", "lars", "lalalala", new Date())
        server.setActiveChannel(serverDAO.getChannel(server.ircServer, "#fest"))
        server.channelMessageReceived("#fest", "lars", "lalalala", new Date())

        then:
        1 * server.listener.messageReceived(_)
        serverDAO.getChannel(server.ircServer, "#fest").messages.size() == 2
    }

    def "show server"() {
        when:
        server.showServer()

        then:
        1 * server.listener.showServerInfo(server.ircServer, server.motd)
    }

    def "test error msg"() {
        when:
        server.ircError("433", "nick already in use")

        then:
        1 * server.listener.error("nick already in use")
    }
}