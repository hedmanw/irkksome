package se.alkohest.irkksome.ui.fragment.channel;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrcMessage;

/**
 * Created by wilhelm 2014-11-01.
 */
public class InfoItem extends ChannelChatItem {
    private final IrcMessage message;
    private final int backgroundColor;

    public InfoItem(IrcMessage message, int backgroundColor) {
        super(ChatItemTypeEnum.INFO);
        this.message = message;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_info_message, null);
        }
        TextView timestamp = (TextView) convertView.findViewById(R.id.chat_message_timestamp);
        timestamp.setText(message.getDisplayTimestamp() + " ");
        TextView messageTextView = (TextView) convertView.findViewById(R.id.chat_message_messagecontent);
        messageTextView.setText(message.getMessage());
        convertView.setBackgroundColor(backgroundColor);
        return convertView;
    }
}
