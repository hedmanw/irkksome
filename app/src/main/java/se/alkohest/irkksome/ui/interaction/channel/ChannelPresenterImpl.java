package se.alkohest.irkksome.ui.interaction.channel;

import se.alkohest.irkksome.model.api.ServerManager;

public class ChannelPresenterImpl implements ChannelPresenter {
    private ChannelView view;

    public ChannelPresenterImpl(ChannelView view) {
        this.view = view;
    }

    @Override
    public void sendMessage(String text) {
        ServerManager.INSTANCE.getActiveServer().sendMessage(ServerManager.INSTANCE.getActiveServer().getActiveChannel(), text);
    }
}
