use Irssi;

$VERSION = "0.2";
%IRSSI = (
    name            => "irkksome",
    description     => "Integrates irssi with the irkksome android app.",
    authors         => "oed",
    url             => "http://github.com/ircsex/irkksome",
    license         => "Beerware",
    changed         => "2014-07-29"
);

sub send_backlog {
    my ($server, $time) = @_;
    Irssi::print("Sending backlog..");
    signals_remove();
    Irssi::signal_add_first('print text', 'stop_signal');
    my $file = Irssi::settings_get_str('irkksome_log');
    open(LOG, "< $file");

    # TODO - only send messages after timestamp..
    while (my $line = <LOG>) {
        chomp $line;
        Irssi::signal_emit('server incoming', $server, $line);
    }
    close(LOG);
    clear_backlog();

    Irssi::signal_remove('print text', 'stop_signal');
    signals_add();
}

sub clear_backlog {
    unlink Irssi::settings_get_str('irkksome_log');
}

sub stop_signal {
    Irssi::signal_stop();
}

sub handle_command {
    my ($client, $args, $data) = @_;
    my $server = $client->{server};
    my ($command, $arg) = split(/ /, $args, 2);
    Irssi::print("command: $command | $arg");
    if ($command eq "backlog") {
        send_backlog($server, $arg);
    }
}

sub log_string {
    my ($string) = @_;
    Irssi::print($string);
    # TODO - add unix time to string
    my $file = Irssi::settings_get_str('irkksome_log');
    open(LOG, ">> $file");
    print LOG "$string\n";
    close(LOG);
}

sub public {
    my ($server, $msg, $nick, $address, $target) = @_;
    log_string(":$nick!$address PRIVMSG $target :$msg");
}

sub private {
    my ($server, $msg, $nick, $address) = @_;
    log_string(":$nick!$address PRIVMSG $server->{nick} :$msg")
}

sub own_public {
    my ($server, $msg, $target) = @_;
    log_string(":$server->{nick}! PRIVMSG $target :$msg");
}

sub own_private {
    my ($server, $msg, $target, $orig_target) = @_;
    #Irssi::print("strings: $msg $target $orig_target");
    log_string(":$server->{nick}! PRIVMSG $target :$msg");
}

sub join {
    my ($server, $channel, $nick, $address) = @_;
    log_string(":$nick!$address JOIN :$channel")
}

sub part {
    my ($server, $channel, $nick, $address, $reason) = @_;
    log_string(":$nick!$address PART $channel")
}

sub quit {
    my ($server, $nick, $address, $reason) = @_;
    log_string(":$nick!$address QUIT :$channel")
}

sub nick {
    my ($server, $new, $old, $address) = @_;
    log_string("$old!$address NICK :$new")
}

# Signals
sub signals_add {
    Irssi::signal_add('proxy client command', 'handle_command');
    Irssi::signal_add_last('message public', 'public');
    Irssi::signal_add_last('message private', 'private');
    Irssi::signal_add_last('message own_public', 'own_public');
    Irssi::signal_add_last('message own_private', 'own_private');
    Irssi::signal_add_last('message join', 'join');
    Irssi::signal_add_last('message part', 'part');
    Irssi::signal_add_last('message quit', 'quit');
    #Irssi::signal_add_last('message kick', 'kick');
    Irssi::signal_add_last('message nick', 'nick');
    Irssi::signal_add_last('message own_nick', 'nick');
    #Irssi::signal_add_last('message invite', 'invite');
    #Irssi::signal_add_last('message topic', 'topic');
}

sub signals_remove {
    Irssi::signal_remove('proxy client command', 'handle_command');
    Irssi::signal_remove('message public', 'public');
    Irssi::signal_remove('message private', 'private');
    Irssi::signal_remove('message own_public', 'own_public');
    Irssi::signal_remove('message own_private', 'own_private');
    Irssi::signal_remove('message join', 'join');
    Irssi::signal_remove('message part', 'part');
    Irssi::signal_remove('message quit', 'quit');
    #Irssi::signal_remove('message kick', 'kick');
    Irssi::signal_remove('message nick', 'nick');
    Irssi::signal_remove('message own_nick', 'nick');
    #Irssi::signal_remove('message invite', 'invite');
    #Irssi::signal_remove('message topic', 'topic');
}

signals_add();

# Settings
Irssi::settings_add_str('irkksome', 'irkksome_log', $ENV{"HOME"}.'/.irssi/irkksome');

