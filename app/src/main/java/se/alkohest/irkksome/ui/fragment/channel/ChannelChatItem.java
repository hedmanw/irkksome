package se.alkohest.irkksome.ui.fragment.channel;

import se.alkohest.irkksome.ui.AbstractListItem;

public abstract class ChannelChatItem extends AbstractListItem {
    public enum ChatItemTypeEnum {
        RECEIVED, SENT, TRACKBAR, INFO
    }

    private ChatItemTypeEnum viewType;

    public ChannelChatItem(ChatItemTypeEnum viewType) {
        this.viewType = viewType;
    }

    public ChatItemTypeEnum getViewType() {
        return viewType;
    }
}
