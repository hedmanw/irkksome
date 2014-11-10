package se.alkohest.irkksome.model.api

import se.alkohest.irkksome.ColorProviderMockSpecification
import se.alkohest.irkksome.irc.IrcProtocol
import se.alkohest.irkksome.model.api.dao.*

public class ServerImplTest extends ColorProviderMockSpecification {
    def backingServer = new IrcServerDAO().create("localhost")
    def data = new IrkksomeConnectionDAO().create()
    ServerImpl server
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
        server.serverDisconnectionListener = Mock(ServerConnectionListener)
        server.hilightListener = Mock(HilightListener)
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
        1 * server.ircProtocol.setNick("fest")
    }

    def "leave last channel"() {
        when:
        server.leaveChannel(channelDAO.create("#fest"))

        then:
        1 * server.ircProtocol.partChannel("#fest")
        server.activeChannel == null
        1 * server.listener.showServerInfo(server.getBackingBean(), _)
    }

    def "leave some channel"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#asdf", "Heissman", new Date())
        when:
        server.leaveChannel(channelDAO.create("#fest"))

        then:
        1 * server.ircProtocol.partChannel("#fest")
        server.activeChannel == serverDAO.getChannel(server.getBackingBean(), "#asdf")
        1 * server.listener.setActiveChannel(serverDAO.getChannel(server.getBackingBean(), "#asdf"))
    }

    def "leave query"() {
        when:
        server.leaveChannel(channelDAO.create("oed"))

        then:
        0 * server.ircProtocol.partChannel(_)
    }

    def "send message"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#asdf", "Heissman", new Date())
        def channel = serverDAO.getChannel(server.ircServer, "#asdf")

        when:
        server.sendMessage(channel, "Äre fest?")

        then:
        1 * server.ircProtocol.sendChannelMessage("#asdf", "Äre fest?")
        1 * server.listener.messageReceived( { it.toString() == "SENT:Heissman: Äre fest?" } )
    }

    def "server connected"() {
        when:
        server.serverConnectionEstablished()

        then:
        0 * server.listener._
        1 * server.connectionListener.connectionEstablished(server)
    }

    def "server Registered"() {
        when:
        server.serverRegistered("irc.chalmers.it", "fest")

        then:
        server.ircServer.getSelf().name == "fest"
        1 * server.listener.showServerInfo(server.ircServer, server.motd)
    }

    def "When your nick changes, post a message to all channels you're in"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#asdf", "Heissman", new Date())
        def channel = serverDAO.getChannel(server.ircServer, "#asdf")

        when:
        server.nickChanged("Heissman", "Hajsman", new Date())

        then:
        server.ircServer.getSelf().name == "Hajsman"
        channel.messages.size() == 1
        channel.messages.get(0).message == "You are now known as Hajsman"
        1 * server.listener.messageReceived( { it.toString() == "NICKCHANGE:Hajsman: You are now known as Hajsman" } )
    }

    def "When some nick changes, a message is received in the correct channel"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#asdf", "Heissman", new Date())
        server.userJoined("#asdf", "apa", new Date())
        def channel = serverDAO.getChannel(server.ircServer, "#asdf")

        when:
        server.nickChanged("apa", "loloped", new Date())

        then:
        channel.messages.size() == 2
        1 * server.listener.messageReceived({ it.toString() == "NICKCHANGE:loloped: apa is now known as loloped" })
    }

    def "When nicks change in a non-active channel, you're not notified"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#asdf", "Heissman", new Date())
        server.userJoined("#hest", "Heissman", new Date())
        server.userJoined("#asdf", "apa", new Date())
        def channel = serverDAO.getChannel(server.ircServer, "#asdf")

        when:
        server.nickChanged("apa", "loloped", new Date())

        then:
        channel.messages.size() == 2
        channel.messages.get(1).message == "apa is now known as loloped"
        0 * server.listener.messageReceived(_)
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

    def "When you join a channel, it is the active channel"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")

        when:
        server.userJoined("#asdf", "Heissman", new Date())

        then:
        1 * server.listener.setActiveChannel(serverDAO.getChannel(server.ircServer, "#asdf"))
        server.activeChannel == serverDAO.getChannel(server.ircServer, "#asdf")
    }

    def "When someone joins an active channel, a notification is sent"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#asdf", "Heissman", new Date())
        def channel = serverDAO.getChannel(server.ircServer, "#asdf")

        when:
        server.userJoined("#asdf", "apa", new Date())

        then:
        channel.messages.size() == 1
        1 * server.listener.messageReceived({ it.toString() == "JOIN:apa: apa joined the channel." })
    }

    def "When someone joins a channel, messages are sent"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#asdf", "Heissman", new Date())
        server.userJoined("#hest", "Heissman", new Date())
        def channel = serverDAO.getChannel(server.ircServer, "#asdf")

        when:
        server.userJoined("#asdf", "apa", new Date())

        then:
        channel.messages.size() == 1
        channel.messages.get(0).message == "apa joined the channel."
        0 * server.listener.messageReceived(_)
    }

    def "User parted in active channel sends notification"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#asdf", "Heissman", new Date())

        when:
        server.userParted("#asdf", "palle", new Date())

        then:
        1 * server.listener.messageReceived({ it.toString() == "PART:palle: palle left the channel." })
    }

    def "User parted in inactive channel posts message"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#asdf", "Heissman", new Date())
        server.userJoined("#hest", "Heissman", new Date())

        when:
        server.userParted("#asdf", "palle", new Date())

        then:
        0 * server.listener.messageReceived(_)
    }

    def "User quit in active channel sends notification"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#asdf", "Heissman", new Date())
        server.userJoined("#hest", "Heissman", new Date())
        server.userJoined("#hest", "hullebulle", new Date())

        when:
        server.userQuit("hullebulle", "No more bulle!", new Date())

        then:
        1 * server.listener.messageReceived({ it.toString() == "QUIT:hullebulle: hullebulle quit. (No more bulle!)" })
    }

    def "User quit in inactive channel posts message"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#asdf", "Heissman", new Date())
        server.userJoined("#asdf", "hullebulle", new Date())
        server.userJoined("#hest", "Heissman", new Date())

        when:
        server.userQuit("hullebulle", "No more bulle!", new Date())

        then:
        0 * server.listener.messageReceived(_)
    }

    def "channel message received in active channel"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#hest", "Heissman", new Date())
        server.userJoined("#asdf", "Heissman", new Date())
        server.userJoined("#asdf", "Korvryttarn", new Date())

        when:
        server.channelMessageReceived("#asdf", "Korvryttarn", "lalalala", new Date())

        then:
        1 * server.listener.messageReceived({ it.toString() == "RECEIVED:Korvryttarn: lalalala" })
    }

    def "channel message received in inactive channel"() {
        setup:
        server.serverRegistered("irc.chalmers.it", "Heissman")
        server.userJoined("#asdf", "Heissman", new Date())
        server.userJoined("#hest", "Heissman", new Date())
        server.userJoined("#asdf", "Korvryttarn", new Date())

        when:
        server.channelMessageReceived("#asdf", "Korvryttarn", "lalalala", new Date())

        then:
        0 * server.listener.messageReceived(_)
    }

    def "show server"() {
        when:
        server.showServer()

        then:
        1 * server.listener.showServerInfo(server.ircServer, server.motd)
        server.getActiveChannel() == null
    }

    def "test error msg"() {
        when:
        server.ircError("433", "nick already in use")

        then:
        1 * server.listener.error("nick already in use")
    }
}