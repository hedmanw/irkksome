package se.alkohest.irkksome.ui.interaction.channel;

import se.alkohest.irkksome.model.entity.IrcChannel;

public interface ChannelPresenter {
    void sendMessage(String text);
    void smooothScrollToBottom();
    void changeChannel(IrcChannel channel);
    void messageReceived();
}
