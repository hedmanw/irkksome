package se.alkohest.irkksome.ui.fragment.channel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.ui.fragment.HilightContainedFragment;
import se.alkohest.irkksome.ui.widget.ChatRecylerView;
import se.alkohest.irkksome.util.ColorProvider;
import se.alkohest.irkksome.util.DateFormatUtil;

public class ChannelFragment extends HilightContainedFragment {
    public static final String FRAGMENT_TAG = "channel";
    private static IrcChannel ircChannel;
    private ChatRecylerView chatRecylerView;
    private static OnMessageSendListener activity;
    private EditText messageEditText;

    public static ChannelFragment newInstance(IrcChannel ircChannel) {
        ChannelFragment fragment = new ChannelFragment();
        ChannelFragment.ircChannel = ircChannel;
        return fragment;
    }

    public ChannelFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(ircChannel.getName());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ChannelFragment.activity = (OnMessageSendListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_channel, container, false);

        messageEditText = ((EditText) inflatedView.findViewById(R.id.input_field));
        messageEditText.setHint("Send message to " + ircChannel.getName());
        messageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int keyCode, KeyEvent keyEvent) {
                if (keyCode == EditorInfo.IME_ACTION_SEND) {
                    activity.sendMessage(null);
                    return true;
                }
                return false;
            }
        });

        chatRecylerView = (ChatRecylerView) inflatedView.findViewById(R.id.recycler_view);
        chatRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatRecylerView.setAdapter(new ChatItemAdapter());

        chatRecylerView.setOnSizeChangedListener(new ChatRecylerView.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
                if (oldHeight > newHeight) {
                    instantScrollToBottom();
                }
            }
        });

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(messageEditText, 0);
        messageEditText.requestFocus();
        smooothScrollToBottom();

        return inflatedView;
    }

    public void changeChannel(IrcChannel channel) {
        ircChannel = channel;
        chatRecylerView.getAdapter().notifyDataSetChanged();
    }

    public void smooothScrollToBottom() {
        if (chatRecylerView.getAdapter().getItemCount() > 0 && chatRecylerView.isAtBottom()) {
            chatRecylerView.smoothScrollToPosition(chatRecylerView.getAdapter().getItemCount()-1);
        }
    }

    public void instantScrollToBottom() {
        if (chatRecylerView.getAdapter().getItemCount() > 0) {
            chatRecylerView.scrollToPosition(chatRecylerView.getAdapter().getItemCount()-1);
        }
    }

    public void receiveMessage() {
        chatRecylerView.getAdapter().notifyDataSetChanged();
        smooothScrollToBottom();
    }

    public interface OnMessageSendListener {
        public void sendMessage(View view);
    }

    private class ChatItemAdapter extends RecyclerView.Adapter<ChatItemHolder> {
        @Override
        public ChatItemHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message, parent, false);
            return new ChatItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ChatItemHolder holder, int pos) {
            IrcMessage ircMessage = ircChannel.getMessages().get(pos);
            holder.bindMessage(ircMessage);
        }

        @Override
        public int getItemCount() {
            return ircChannel.getMessages().size();
        }
    }

    private class ChatItemHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private final TextView timestamp;
        private final TextView author;
        private final TextView messageTextView;
        private IrcMessage ircMessage;

        public ChatItemHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            timestamp = (TextView) itemView.findViewById(R.id.chat_message_timestamp);
            author = (TextView) itemView.findViewById(R.id.chat_message_author);
            messageTextView = (TextView) itemView.findViewById(R.id.chat_message_messagecontent);
        }

        public void bindMessage(IrcMessage chatItem) {
            ircMessage = chatItem;
            timestamp.setText(DateFormatUtil.getTimeDay(ircMessage.getTimestamp()) + " ");
            author.setText(ircMessage.getAuthor() + ": ");
            author.setTextColor(ColorProvider.getInstance().getColor(ircMessage.getAuthor()));
            messageTextView.setText(ircMessage.getMessage());
        }

        @Override
        public boolean onLongClick(View view) {
            messageEditText.append(ircMessage.getAuthor() + ": ");
            return true;
        }
    }
}
