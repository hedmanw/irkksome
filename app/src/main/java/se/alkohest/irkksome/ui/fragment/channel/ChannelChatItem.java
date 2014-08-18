package se.alkohest.irkksome.ui.fragment.channel;

import se.alkohest.irkksome.ui.AbstractListItem;

public abstract class ChannelChatItem extends AbstractListItem {
    public enum ChatItemTypeEnum {
        RECEIVED, SENT, TRACKBAR, INFO
    }

    protected ChatItemTypeEnum viewType;

    public ChatItemTypeEnum getViewType() {
        return viewType;
    }
}
