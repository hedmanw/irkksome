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
import se.alkohest.irkksome.util.DateFormatUtil;

public class ChannelFragment extends HilightContainedFragment {
    public static final String FRAGMENT_TAG = "channel";
    private static IrcChannel ircChannel;
    private RecyclerView messageRecyclerView;
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

        messageRecyclerView = (RecyclerView) inflatedView.findViewById(R.id.recycler_view);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        messageRecyclerView.setAdapter(new ChatItemAdapter());

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(messageEditText, 0);
        messageEditText.requestFocus();
        scrollToBottom();

        return inflatedView;
    }

    public void changeChannel(IrcChannel channel) {
        ircChannel = channel;
        messageRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private boolean hasBacklog() {
        return false;
    }

    public void scrollToBottom() {
        if (messageRecyclerView.getAdapter().getItemCount() > 0) {
            messageRecyclerView.smoothScrollToPosition(messageRecyclerView.getAdapter().getItemCount()-1);
        }
    }

    public void receiveMessage() {
        messageRecyclerView.getAdapter().notifyDataSetChanged();
        scrollToBottom();
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

    private class ChatItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView timestamp;
        private final TextView author;
        private final TextView messageTextView;
        private IrcMessage ircMessage;

        public ChatItemHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            timestamp = (TextView) itemView.findViewById(R.id.chat_message_timestamp);
            author = (TextView) itemView.findViewById(R.id.chat_message_author);
            messageTextView = (TextView) itemView.findViewById(R.id.chat_message_messagecontent);
        }

        public void bindMessage(IrcMessage chatItem) {
            ircMessage = chatItem;
            timestamp.setText(DateFormatUtil.getTimeDay(ircMessage.getTimestamp()) + " ");
            author.setText(ircMessage.getAuthor().getName() + ": ");
            author.setTextColor(ircMessage.getAuthor().getColor());
            messageTextView.setText(ircMessage.getMessage());
        }

        @Override
        public void onClick(View view) {
            messageEditText.append(ircMessage.getAuthor().getName() + ": ");
        }
    }
}
