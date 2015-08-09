package se.alkohest.irkksome.ui.interaction.channel;

import se.alkohest.irkksome.model.api.ServerManager;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.ui.CallbackHandler;

// TODO: The logic should be moved here from ChannelFragment at some point...right now, this is just a dunb extra wrapper with and interface around an interface with the same methods.
public class ChannelPresenterImpl implements ChannelPresenter {
    private ChannelView view;

    public ChannelPresenterImpl(ChannelView view) {
        this.view = view;
        CallbackHandler.getInstance().registerChannelPresenter(this);
    }

    @Override
    public void sendMessage(String text) {
        ServerManager.INSTANCE.getActiveServer().sendMessage(ServerManager.INSTANCE.getActiveServer().getActiveChannel(), text);
    }

    @Override
    public void smooothScrollToBottom() {
        view.smooothScrollToBottom();
    }

    @Override
    public void changeChannel(IrcChannel channel) {
        view.changeChannel(channel);
    }

    @Override
    public void messageReceived() {
        view.messageReceived();
    }
}
