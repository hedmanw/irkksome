package se.alkohest.irkksome.irc;

/**
 * Created by oed on 10/5/13.
 */
public class IrcProtocolStrings {

    public static final String PRIVMSG = "PRIVMSG";
    public static final String PING = "PING";
    public static final String PONG = "PONG";
    public static final String JOIN = "JOIN";
    public static final String PART = "PART";
    public static final String NICK = "NICK";
    public static final String PASS = "PASS";
    public static final String MODE = "MODE";
    public static final String USER = "USER";
    public static final String QUIT = "QUIT";
    public static final String NAMES = "NAMES";
    public static final String LIST = "LIST";
    public static final String WHOIS = "WHOIS";
    public static final String INVITE = "INVITE";

    // Numeric replies
    public static final String RPL_WELCOME = "001";
    public static final String RPL_WHOISUSER = "311";
    public static final String RPL_WHOISIDLE = "317";
    public static final String RPL_WHOISCHANNELS = "319";
    public static final String RPL_LIST = "322";
    public static final String RPL_NAMREPLY = "353";

    // Numeric errors
    public static final String ERR_NOSUCHNICK = "401";
    public static final String ERR_NOSUCHSERVER = "402";
    public static final String ERR_NOSUCHCHANNEL = "403";
    public static final String ERR_CANNOTSENDTOCHAN = "404";
    public static final String ERR_TOOMANYCHANNELS = "405";
    public static final String ERR_ERRONEUSNICKNAME = "432";
    public static final String ERR_NICKNAMEINUSE = "433";
    public static final String ERR_USERONCHANNEL = "443";
    public static final String ERR_PASSWDMISMATCH = "464";
    public static final String ERR_YOUREBANNEDCREEP = "465";
    public static final String ERR_CHANNELISFULL = "471";
    public static final String ERR_INVITEONLYCHAN = "473";
    public static final String ERR_BANNEDFROMCHAN = "474";
    public static final String ERR_BADCHANNELKEY = "475";

    // Flags
    public static final String FLAG_OWNER = "~";
    public static final String FLAG_SUPEROP = "&";
    public static final String FLAG_OP = "@";
    public static final String FLAG_HALFOP = "%";
    public static final String FLAG_VOICE = "+";
}
