package se.alkohest.irkksome.ui.interaction.channel;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import se.alkohest.irkksome.ui.interaction.HilightContainedFragment;
import se.alkohest.irkksome.ui.widget.ChatRecylerView;
import se.alkohest.irkksome.util.ColorProvider;
import se.alkohest.irkksome.util.DateFormatUtil;

public class ChannelFragment extends HilightContainedFragment implements ChannelView {
    public static final String FRAGMENT_TAG = "channel";
    private static IrcChannel ircChannel;
    private ChatRecylerView chatRecylerView;
    private EditText messageEditText;
    private ChannelPresenter presenter;

    public static ChannelFragment newInstance(IrcChannel ircChannel) {
        ChannelFragment fragment = new ChannelFragment();
        ChannelFragment.ircChannel = ircChannel;
        return fragment;
    }

    public ChannelFragment() {
        presenter = new ChannelPresenterImpl(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(ircChannel.getName());
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
                    sendMessage();
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
                if (oldHeight > newHeight && chatRecylerView.isAtBottom()) {
                    instantScrollToBottom();
                }
            }
        });

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(messageEditText, 0);
        messageEditText.requestFocus();

        return inflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chatRecylerView.post(new Runnable() {
            @Override
            public void run() {
                forceSmoothScrollToBottom();
            }
        });
    }

    public void changeChannel(IrcChannel channel) {
        ircChannel = channel;
        messageEditText.setHint("Send message to " + ircChannel.getName());
        chatRecylerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void forceSmoothScrollToBottom() {
        if (chatRecylerView.getAdapter().getItemCount() > 0) {
            chatRecylerView.smoothScrollToPosition(chatRecylerView.getAdapter().getItemCount() - 1);
        }
    }

    @Override
    public void instantScrollToBottom() {
        if (chatRecylerView.getAdapter().getItemCount() > 0) {
            chatRecylerView.scrollToPosition(chatRecylerView.getAdapter().getItemCount() - 1);
        }
    }

    @Override
    public void messageReceived() {
        chatRecylerView.getAdapter().notifyDataSetChanged();
        if (chatRecylerView.isAtBottom()) {
            forceSmoothScrollToBottom();
        }
    }

    public void sendMessage() {
        EditText editText = (EditText) getActivity().findViewById(R.id.input_field);
        String text = editText.getText().toString();
        presenter.sendMessage(text);
        if (!text.isEmpty()) {
            editText.getText().clear();
        }
    }

    private class ChatItemAdapter extends RecyclerView.Adapter<ChatItemHolder> {
        private static final int TYPE_CHAT_MESSAGE = 0;
        private static final int TYPE_NICKCHANGE_MESSAGE = 1;
        private static final int TYPE_STATUS_MESSAGE = 2;

        @Override
        public ChatItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_CHAT_MESSAGE) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message, parent, false);
                return new MessageItemHolder(view);
            } else if (viewType == TYPE_NICKCHANGE_MESSAGE) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_info_message, parent, false);
                return new NickchangeMessageItemHolder(view);
            }
            else if (viewType == TYPE_STATUS_MESSAGE) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_info_message, parent, false);
                return new StatusMessageItemHolder(view);
            }
            throw new RuntimeException("There's no view type matching: " + viewType);
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

        @Override
        public int getItemViewType(int position) {
            IrcMessage.MessageTypeEnum ircMessageType = ircChannel.getMessages().get(position).getMessageType();
            if (ircMessageType == IrcMessage.MessageTypeEnum.SENT || ircMessageType == IrcMessage.MessageTypeEnum.RECEIVED) {
                return TYPE_CHAT_MESSAGE;
            } else if (ircMessageType == IrcMessage.MessageTypeEnum.NICKCHANGE) {
                return TYPE_NICKCHANGE_MESSAGE;
            }
            return TYPE_STATUS_MESSAGE;
        }
    }

    private class ChatItemHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        protected final TextView timestamp;
        protected final TextView messageTextView;
        protected IrcMessage ircMessage;

        public ChatItemHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            timestamp = (TextView) itemView.findViewById(R.id.chat_message_timestamp);
            messageTextView = (TextView) itemView.findViewById(R.id.chat_message_messagecontent);
        }

        public void bindMessage(IrcMessage chatItem) {
            ircMessage = chatItem;
            timestamp.setText(DateFormatUtil.getTimeDay(ircMessage.getTimestamp()) + " ");
            messageTextView.setText(ircMessage.getMessage());
        }

        @Override
        public boolean onLongClick(View view) {
            messageEditText.append(ircMessage.getAuthor() + ": ");
            return true;
        }
    }

    private class MessageItemHolder extends ChatItemHolder {
        private final TextView author;

        public MessageItemHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.chat_message_author);
        }

        @Override
        public void bindMessage(IrcMessage chatItem) {
            super.bindMessage(chatItem);
            author.setText(ircMessage.getAuthor() + ": ");
            author.setTextColor(ColorProvider.getInstance().getColor(ircMessage.getAuthor()));
        }
    }

    private class NickchangeMessageItemHolder extends ChatItemHolder {
        public NickchangeMessageItemHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindMessage(IrcMessage chatItem) {
            super.bindMessage(chatItem);
            messageTextView.setText(Html.fromHtml(formatNickChangeMessage(ircMessage.getMessage())));
        }

        private String formatNickChangeMessage(String message) {
            String oldNick = message.substring(0, message.indexOf(" "));
            String newNick = message.substring(message.lastIndexOf(" ") + 1);
            String middleMessage = message.substring(message.indexOf(" "), message.lastIndexOf(" ") + 1);
            int oldNickColor = ColorProvider.getInstance().getColor(oldNick);
            int newNickColor = ColorProvider.getInstance().getColor(newNick);
            return "<b><font color='" + oldNickColor + "'>" + oldNick + "</font></b>" +
                    middleMessage + "<b><font color='" + newNickColor + "'>" + newNick + "</font></b>";
        }
    }

    private class StatusMessageItemHolder extends ChatItemHolder {
        public StatusMessageItemHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindMessage(IrcMessage chatItem) {
            super.bindMessage(chatItem);
            int authorColor = ColorProvider.getInstance().getColor(ircMessage.getAuthor());
            timestamp.setTextColor(authorColor);
            messageTextView.setTextColor(authorColor);
        }
    }
}
