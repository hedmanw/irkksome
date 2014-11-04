package se.alkohest.irkksome.ui.fragment.channel;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.impl.IrcChatMessageEB;

/**
 * Created by wilhelm 2014-10-31.
 */
public class MessageItem extends ChannelChatItem {
    private final IrcChatMessageEB message;

    public MessageItem(IrcChatMessageEB message) {
        super(ChatItemTypeEnum.RECEIVED);
        this.message = message;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_message, null);
        }
        TextView timestamp = (TextView) convertView.findViewById(R.id.chat_message_timestamp);
        timestamp.setText(message.getDisplayTimestamp() + " ");
        TextView author = (TextView) convertView.findViewById(R.id.chat_message_author);
        author.setText(message.getAuthor().getName() + ": ");
        author.setTextColor(message.getAuthor().getColor());
        TextView messageTextView = (TextView) convertView.findViewById(R.id.chat_message_messagecontent);
        messageTextView.setText(message.getMessage());
        return convertView;
    }
}
