package se.alkohest.irkksome.irc

import spock.lang.Specification

/**
 * Created by Wilhelm on 2013-09-22.
 */
class IrcProtocolAdapterTest extends Specification {
    String LINE_BREAK = "\r\n";
    IrcProtocolAdapter ipa
    IrcProtocolListener subscriber = Mock(IrcProtocolListener)
    Connection connection = Mock(Connection)

    def setup() {
        ipa = new IrcProtocolAdapter(connection);
        ipa.setListener(subscriber)
        connection.isConnected() >> true
    }

    def "test connect"() {
        when: "IPA connects to the server"
        ipa.connect("Hest", "Fest", "Alko Hest")

        then: "NICK and USER is sent to the socket"
        1 * connection.write("NICK Hest" + LINE_BREAK)
        1 * connection.write({ it.startsWith("USER Fest")})
    }

    def "test disconnect"() {
        when: "IPA disconnects from the server"
        ipa.disconnect(message)

        then: "QUIT is sent to the socket"
        1 * connection.write("QUIT :" + message + LINE_BREAK)

        where:
        message << ["bye", ":(", "so long fuckers"]
    }

    def "test sendChannelMessage"() {
        when:
        ipa.sendChannelMessage(channel, message)

        then:
        1 * connection.write("PRIVMSG " + channel + " :" + message + LINE_BREAK)

        where:
        channel << ["#fest", "#svinstia", "#party"]
        message << ["Jag kommer fan med", "HAH, aldrig!", "men jag vill :(:(:("]
    }

    def "test whois"() {
        when:
        ipa.whois(nick)

        then:
        1 * connection.write("WHOIS " + nick + LINE_BREAK)

        where:
        nick << ["Heissman", "Rascal", "oed"]
    }

    def "test invite"() {
        when:
        ipa.invite(nick, channel)

        then:
        1 * connection.write("INVITE " + nick + " " + channel + LINE_BREAK)

        where:
        nick << ["Heissman", "Rascal", "oed"]
        channel << ["#fest", "#svinstia", "#party"]
    }

    def "test getChannelsOnServer"() {
        when:
        ipa.listChannels()

        then:
        1 * connection.write("LIST" + LINE_BREAK)
    }

    def "test PING"() {
        when:
        def command = "PING :"
        ipa.handleReply(command + code)

        then:
        1 * connection.write("PONG :" + code + LINE_BREAK)

        where:
        code << ["F9F15D37", "G4GF3H23"]
    }
}
