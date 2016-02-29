package se.alkohest.irkksome.ui.interaction.channel;

import se.alkohest.irkksome.model.entity.IrcChannel;

public interface ChannelView {
    void forceSmoothScrollToBottom();
    void instantScrollToBottom();
    void messageReceived();
    void changeChannel(IrcChannel channel);
}
